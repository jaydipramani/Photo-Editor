package com.relish.app.drip.org.tensorflow;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;
import java.util.HashMap;


public final class Tensor<T> implements AutoCloseable {
    private static HashMap<Class<?>, DataType> classDataTypes;
    private DataType dtype;
    private long nativeHandle;
    private long[] shapeCopy = null;

    private static native long allocate(int i, long[] jArr, long j);

    private static native long allocateNonScalarBytes(long[] jArr, Object[] objArr);

    private static native long allocateScalarBytes(byte[] bArr);

    private static native ByteBuffer buffer(long j);

    private static native void delete(long j);

    private static native int dtype(long j);

    private static native void readNDArray(long j, Object obj);

    private static native boolean scalarBoolean(long j);

    private static native byte[] scalarBytes(long j);

    private static native double scalarDouble(long j);

    private static native float scalarFloat(long j);

    private static native int scalarInt(long j);

    private static native long scalarLong(long j);

    private static native void setValue(long j, Object obj);

    private static native long[] shape(long j);

    static {
        HashMap<Class<?>, DataType> hashMap = new HashMap<>();
        classDataTypes = hashMap;
        hashMap.put(Integer.TYPE, DataType.INT32);
        classDataTypes.put(Integer.class, DataType.INT32);
        classDataTypes.put(Long.TYPE, DataType.INT64);
        classDataTypes.put(Long.class, DataType.INT64);
        classDataTypes.put(Float.TYPE, DataType.FLOAT);
        classDataTypes.put(Float.class, DataType.FLOAT);
        classDataTypes.put(Double.TYPE, DataType.DOUBLE);
        classDataTypes.put(Double.class, DataType.DOUBLE);
        classDataTypes.put(Byte.TYPE, DataType.STRING);
        classDataTypes.put(Byte.class, DataType.STRING);
        classDataTypes.put(Boolean.TYPE, DataType.BOOL);
        classDataTypes.put(Boolean.class, DataType.BOOL);
        TensorFlow.init();
    }

    public static <T> Tensor<T> create(Object obj, Class<T> cls) {
        DataType fromClass = DataType.fromClass(cls);
        if (objectCompatWithType(obj, fromClass)) {
            return (Tensor<T>) create(obj, fromClass);
        }
        throw new IllegalArgumentException("DataType of object does not match T (expected " + fromClass + ", got " + dataTypeOf(obj) + ")");
    }

    public static Tensor<?> create(Object obj) {
        return create(obj, dataTypeOf(obj));
    }

    private static Tensor<?> create(Object obj, DataType dataType) {
        Tensor<?> tensor = new Tensor<>(dataType);
        long[] jArr = new long[numDimensions(obj, dataType)];
        ((Tensor) tensor).shapeCopy = jArr;
        fillShape(obj, 0, jArr);
        if (((Tensor) tensor).dtype != DataType.STRING) {
            long allocate = allocate(((Tensor) tensor).dtype.c(), ((Tensor) tensor).shapeCopy, (long) (elemByteSize(((Tensor) tensor).dtype) * numElements(((Tensor) tensor).shapeCopy)));
            ((Tensor) tensor).nativeHandle = allocate;
            setValue(allocate, obj);
        } else {
            long[] jArr2 = ((Tensor) tensor).shapeCopy;
            if (jArr2.length != 0) {
                ((Tensor) tensor).nativeHandle = allocateNonScalarBytes(jArr2, (Object[]) obj);
            } else {
                ((Tensor) tensor).nativeHandle = allocateScalarBytes((byte[]) obj);
            }
        }
        return tensor;
    }

    public static Tensor<Integer> create(long[] jArr, IntBuffer intBuffer) {
        Tensor<Integer> allocateForBuffer = allocateForBuffer(DataType.INT32, jArr, intBuffer.remaining());
        allocateForBuffer.buffer().asIntBuffer().put(intBuffer);
        return allocateForBuffer;
    }

    public static Tensor<Float> create(long[] jArr, FloatBuffer floatBuffer) {
        Tensor<Float> allocateForBuffer = allocateForBuffer(DataType.FLOAT, jArr, floatBuffer.remaining());
        allocateForBuffer.buffer().asFloatBuffer().put(floatBuffer);
        return allocateForBuffer;
    }

    public static Tensor<Double> create(long[] jArr, DoubleBuffer doubleBuffer) {
        Tensor<Double> allocateForBuffer = allocateForBuffer(DataType.DOUBLE, jArr, doubleBuffer.remaining());
        allocateForBuffer.buffer().asDoubleBuffer().put(doubleBuffer);
        return allocateForBuffer;
    }

    public static Tensor<Long> create(long[] jArr, LongBuffer longBuffer) {
        Tensor<Long> allocateForBuffer = allocateForBuffer(DataType.INT64, jArr, longBuffer.remaining());
        allocateForBuffer.buffer().asLongBuffer().put(longBuffer);
        return allocateForBuffer;
    }

    public static <T> Tensor<T> create(Class<T> cls, long[] jArr, ByteBuffer byteBuffer) {
        return (Tensor<T>) create(DataType.fromClass(cls), jArr, byteBuffer);
    }

    private static Tensor<?> create(DataType dataType, long[] jArr, ByteBuffer byteBuffer) {
        int i;
        if (dataType != DataType.STRING) {
            int elemByteSize = elemByteSize(dataType);
            if (byteBuffer.remaining() % elemByteSize == 0) {
                i = byteBuffer.remaining() / elemByteSize;
            } else {
                throw new IllegalArgumentException(String.format("ByteBuffer with %d bytes is not compatible with a %s Tensor (%d bytes/element)", Integer.valueOf(byteBuffer.remaining()), dataType.toString(), Integer.valueOf(elemByteSize)));
            }
        } else {
            i = byteBuffer.remaining();
        }
        Tensor<?> allocateForBuffer = allocateForBuffer(dataType, jArr, i);
        allocateForBuffer.buffer().put(byteBuffer);
        return allocateForBuffer;
    }


    public <U> Tensor<U> expect(Class<U> cls) {
        DataType fromClass = DataType.fromClass(cls);
        if (fromClass.equals(this.dtype)) {
            return (Tensor<U>) this;
        }
        throw new IllegalArgumentException("Cannot cast from tensor of " + this.dtype + " to tensor of " + fromClass);
    }

    private static <T> Tensor<T> allocateForBuffer(DataType dataType, long[] jArr, int i) {
        int numElements = numElements(jArr);
        if (dataType != DataType.STRING) {
            if (i == numElements) {
                i = elemByteSize(dataType) * numElements;
            } else {
                throw incompatibleBuffer(i, jArr);
            }
        }
        Tensor<T> tensor = new Tensor<>(dataType);
        ((Tensor) tensor).shapeCopy = Arrays.copyOf(jArr, jArr.length);
        ((Tensor) tensor).nativeHandle = allocate(((Tensor) tensor).dtype.c(), ((Tensor) tensor).shapeCopy, (long) i);
        return tensor;
    }

    @Override
    public void close() {
        long j = this.nativeHandle;
        if (j != 0) {
            delete(j);
            this.nativeHandle = 0;
        }
    }

    public DataType dataType() {
        return this.dtype;
    }

    public int numDimensions() {
        return this.shapeCopy.length;
    }

    public int numBytes() {
        return buffer().remaining();
    }

    public int numElements() {
        return numElements(this.shapeCopy);
    }

    public long[] shape() {
        return this.shapeCopy;
    }

    public float floatValue() {
        return scalarFloat(this.nativeHandle);
    }

    public double doubleValue() {
        return scalarDouble(this.nativeHandle);
    }

    public int intValue() {
        return scalarInt(this.nativeHandle);
    }

    public long longValue() {
        return scalarLong(this.nativeHandle);
    }

    public boolean booleanValue() {
        return scalarBoolean(this.nativeHandle);
    }

    public byte[] bytesValue() {
        return scalarBytes(this.nativeHandle);
    }

    public <U> U copyTo(U u) {
        throwExceptionIfTypeIsIncompatible(u);
        readNDArray(this.nativeHandle, u);
        return u;
    }

    public void writeTo(IntBuffer intBuffer) {
        if (this.dtype == DataType.INT32) {
            intBuffer.put(buffer().asIntBuffer());
            return;
        }
        throw incompatibleBuffer(intBuffer, this.dtype);
    }

    public void writeTo(FloatBuffer floatBuffer) {
        if (this.dtype == DataType.FLOAT) {
            floatBuffer.put(buffer().asFloatBuffer());
            return;
        }
        throw incompatibleBuffer(floatBuffer, this.dtype);
    }

    public void writeTo(DoubleBuffer doubleBuffer) {
        if (this.dtype == DataType.DOUBLE) {
            doubleBuffer.put(buffer().asDoubleBuffer());
            return;
        }
        throw incompatibleBuffer(doubleBuffer, this.dtype);
    }

    public void writeTo(LongBuffer longBuffer) {
        if (this.dtype == DataType.INT64) {
            longBuffer.put(buffer().asLongBuffer());
            return;
        }
        throw incompatibleBuffer(longBuffer, this.dtype);
    }

    public void writeTo(ByteBuffer byteBuffer) {
        byteBuffer.put(buffer());
    }

    public String toString() {
        return String.format("%s tensor with shape %s", this.dtype.toString(), Arrays.toString(shape()));
    }

    static Tensor<?> fromHandle(long j) {
        Tensor<?> tensor = new Tensor<>(DataType.fromC(dtype(j)));
        ((Tensor) tensor).shapeCopy = shape(j);
        ((Tensor) tensor).nativeHandle = j;
        return tensor;
    }

    public long getNativeHandle() {
        return this.nativeHandle;
    }

    private Tensor(DataType dataType) {
        this.dtype = dataType;
    }

    private ByteBuffer buffer() {
        return buffer(this.nativeHandle).order(ByteOrder.nativeOrder());
    }

    private static IllegalArgumentException incompatibleBuffer(Buffer buffer, DataType dataType) {
        return new IllegalArgumentException(String.format("cannot use %s with Tensor of type %s", buffer.getClass().getName(), dataType));
    }

    private static IllegalArgumentException incompatibleBuffer(int i, long[] jArr) {
        return new IllegalArgumentException(String.format("buffer with %d elements is not compatible with a Tensor with shape %s", Integer.valueOf(i), Arrays.toString(jArr)));
    }

    private static int numElements(long[] jArr) {
        int i = 1;
        for (long j : jArr) {
            i *= (int) j;
        }
        return i;
    }


    public static  class AnonymousClass1 {
        static final int[] $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType;

        static {
            int[] iArr = new int[DataType.values().length];
            $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType = iArr;
            try {
                iArr[DataType.FLOAT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.INT32.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.DOUBLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.INT64.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.BOOL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.UINT8.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[DataType.STRING.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private static int elemByteSize(DataType dataType) {
        switch (AnonymousClass1.$SwitchMap$com$photoz$photoeditor$pro$drip$org$tensorflow$DataType[dataType.ordinal()]) {
            case 1:
            case 2:
                return 4;
            case 3:
            case 4:
                return 8;
            case 5:
            case 6:
                return 1;
            case 7:
                throw new IllegalArgumentException("STRING tensors do not have a fixed element size");
            default:
                throw new IllegalArgumentException("DataType " + dataType + " is not supported yet");
        }
    }

    private static void throwExceptionIfNotByteOfByteArrays(Object obj) {
        if (!obj.getClass().getName().equals("[[B")) {
            throw new IllegalArgumentException("object cannot be converted to a Tensor as it includes an array with null elements");
        }
    }

    private static Class<?> baseObjType(Object obj) {
        Class<?> cls = obj.getClass();
        while (cls.isArray()) {
            cls = cls.getComponentType();
        }
        return cls;
    }

    private static DataType dataTypeOf(Object obj) {
        return dataTypeFromClass(baseObjType(obj));
    }

    private static DataType dataTypeFromClass(Class<?> cls) {
        DataType dataType = classDataTypes.get(cls);
        if (dataType != null) {
            return dataType;
        }
        throw new IllegalArgumentException("cannot create Tensors of type " + cls.getName());
    }

    private static int numDimensions(Object obj, DataType dataType) {
        int numArrayDimensions = numArrayDimensions(obj);
        return (dataType != DataType.STRING || numArrayDimensions <= 0) ? numArrayDimensions : numArrayDimensions - 1;
    }

    private static int numArrayDimensions(Object obj) {
        Class cls = obj.getClass();
        int i = 0;
        while (cls.isArray()) {
            cls = cls.getComponentType();
            i++;
        }
        return i;
    }

    private static void fillShape(Object obj, int i, long[] jArr) {
        if (!(jArr == null || i == jArr.length)) {
            int length = Array.getLength(obj);
            if (length != 0) {
                if (jArr[i] == 0) {
                    jArr[i] = (long) length;
                } else if (jArr[i] != ((long) length)) {
                    throw new IllegalArgumentException(String.format("mismatched lengths (%d and %d) in dimension %d", Long.valueOf(jArr[i]), Integer.valueOf(length), Integer.valueOf(i)));
                }
                for (int i2 = 0; i2 < length; i2++) {
                    fillShape(Array.get(obj, i2), i + 1, jArr);
                }
                return;
            }
            throw new IllegalArgumentException("cannot create Tensors with a 0 dimension");
        }
    }

    private static boolean objectCompatWithType(Object obj, DataType dataType) {
        Class<?> baseObjType = baseObjType(obj);
        DataType dataTypeFromClass = dataTypeFromClass(baseObjType);
        int numDimensions = numDimensions(obj, dataTypeFromClass);
        if (!baseObjType.isPrimitive() && baseObjType != String.class && numDimensions != 0) {
            throw new IllegalArgumentException("cannot create non-scalar Tensors from arrays of boxed values");
        } else if (dataTypeFromClass.equals(dataType)) {
            return true;
        } else {
            if (dataTypeFromClass == DataType.STRING && dataType == DataType.UINT8) {
                return true;
            }
            return false;
        }
    }

    private void throwExceptionIfTypeIsIncompatible(Object obj) {
        int numDimensions = numDimensions();
        int numDimensions2 = numDimensions(obj, this.dtype);
        if (numDimensions2 != numDimensions) {
            throw new IllegalArgumentException(String.format("cannot copy Tensor with %d dimensions into an object with %d", Integer.valueOf(numDimensions), Integer.valueOf(numDimensions2)));
        } else if (objectCompatWithType(obj, this.dtype)) {
            long[] jArr = new long[numDimensions];
            fillShape(obj, 0, jArr);
            for (int i = 0; i < jArr.length; i++) {
                if (jArr[i] != shape()[i]) {
                    throw new IllegalArgumentException(String.format("cannot copy Tensor with shape %s into object with shape %s", Arrays.toString(shape()), Arrays.toString(jArr)));
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("cannot copy Tensor with DataType %s into an object of type %s", this.dtype.toString(), obj.getClass().getName()));
        }
    }
}
