package com.relish.app.drip.org.tensorflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


public final class NativeLibrary {
    private static final boolean DEBUG = (System.getProperty("org.tensorflow.NativeLibrary.DEBUG") != null);
    private static final String JNI_LIBNAME = "tensorflow_jni";

    public static void load() {
        if (!isLoaded() && !tryLoadLibrary()) {
            String mapLibraryName = System.mapLibraryName(JNI_LIBNAME);
            String makeResourceName = makeResourceName(mapLibraryName);
            log("jniResourceName: " + makeResourceName);
            InputStream resourceAsStream = NativeLibrary.class.getClassLoader().getResourceAsStream(makeResourceName);
            String maybeAdjustForMacOS = maybeAdjustForMacOS(System.mapLibraryName("tensorflow_framework"));
            String makeResourceName2 = makeResourceName(maybeAdjustForMacOS);
            log("frameworkResourceName: " + makeResourceName2);
            InputStream resourceAsStream2 = NativeLibrary.class.getClassLoader().getResourceAsStream(makeResourceName2);
            if (resourceAsStream != null) {
                try {
                    File createTemporaryDirectory = createTemporaryDirectory();
                    createTemporaryDirectory.deleteOnExit();
                    String canonicalPath = createTemporaryDirectory.getCanonicalPath();
                    if (resourceAsStream2 != null) {
                        extractResource(resourceAsStream2, maybeAdjustForMacOS, canonicalPath);
                    } else {
                        log(makeResourceName2 + " not found. This is fine assuming " + makeResourceName + " is not built to depend on it.");
                    }
                    System.load(extractResource(resourceAsStream, mapLibraryName, canonicalPath));
                } catch (IOException e) {
                    throw new UnsatisfiedLinkError(String.format("Unable to extract native library into a temporary file (%s)", e.toString()));
                }
            } else {
                throw new UnsatisfiedLinkError(String.format("Cannot find TensorFlow native library for OS: %s, architecture: %s. See https://github.com/tensorflow/tensorflow/tree/master/tensorflow/java/README.md for possible solutions (such as building the library from source). Additional information on attempts to find the native library can be obtained by adding org.tensorflow.NativeLibrary.DEBUG=1 to the system properties of the JVM.", os(), architecture()));
            }
        }
    }

    private static boolean tryLoadLibrary() {
        try {
            System.loadLibrary(JNI_LIBNAME);
            return true;
        } catch (UnsatisfiedLinkError e) {
            log("tryLoadLibraryFailed: " + e.getMessage());
            return false;
        }
    }

    private static boolean isLoaded() {
        try {
            TensorFlow.version();
            log("isLoaded: true");
            return true;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    private static String maybeAdjustForMacOS(String str) {
        if (!System.getProperty("os.name").contains("OS X") || NativeLibrary.class.getClassLoader().getResource(makeResourceName(str)) != null || !str.endsWith(".dylib")) {
            return str;
        }
        return str.substring(0, str.length() - ".dylib".length()) + ".so";
    }

    private static String extractResource(InputStream inputStream, String str, String str2) throws IOException {
        File file = new File(str2, str);
        file.deleteOnExit();
        String file2 = file.toString();
        log("extracting native library to: " + file2);
        log(String.format("copied %d bytes to %s", Long.valueOf(copy(inputStream, file)), file2));
        return file2;
    }

    private static String os() {
        String lowerCase = System.getProperty("os.name").toLowerCase();
        if (lowerCase.contains("linux")) {
            return "linux";
        }
        if (lowerCase.contains("os x") || lowerCase.contains("darwin")) {
            return "darwin";
        }
        if (lowerCase.contains("windows")) {
            return "windows";
        }
        return lowerCase.replaceAll("\\s", "");
    }

    private static String architecture() {
        String lowerCase = System.getProperty("os.arch").toLowerCase();
        return lowerCase.equals("amd64") ? "x86_64" : lowerCase;
    }

    private static void log(String str) {
        if (DEBUG) {
            PrintStream printStream = System.err;
            printStream.println("org.tensorflow.NativeLibrary: " + str);
        }
    }

    private static String makeResourceName(String str) {
        return "org/tensorflow/native/" + String.format("%s-%s/", os(), architecture()) + str;
    }

    private static long copy(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            byte[] bArr = new byte[1048576];
            long j = 0;
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    return j;
                }
                fileOutputStream.write(bArr, 0, read);
                j += (long) read;
            }
        } finally {
            fileOutputStream.close();
            inputStream.close();
        }
    }

    private static File createTemporaryDirectory() {
        File file = new File(System.getProperty("java.io.tmpdir"));
        String sb2 = "tensorflow_native_libraries-" + System.currentTimeMillis() + "-";
        for (int i = 0; i < 1000; i++) {
            File file2 = new File(file, sb2 + i);
            if (file2.mkdir()) {
                return file2;
            }
        }
        throw new IllegalStateException("Could not create a temporary directory (tried to make " + sb2 + "*) to extract TensorFlow native libraries.");
    }

    private NativeLibrary() {
    }
}
