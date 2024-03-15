package com.relish.app.drip.org.tensorflow;


import java.util.Iterator;


public final class Graph implements AutoCloseable {
    public long nativeHandle;
    public final Object nativeHandleLock;
    public int refcount;

    private static native long[] addGradients(long j, long[] jArr, int[] iArr, long[] jArr2, int[] iArr2, long[] jArr3, int[] iArr3);

    private static native long allocate();

    private static native void delete(long j);

    private static native void importGraphDef(long j, byte[] bArr, String str) throws IllegalArgumentException;

    public static native long[] nextOperation(long j, int i);

    private static native long operation(long j, String str);

    private static native byte[] toGraphDef(long j);

    static {
        TensorFlow.init();
    }

    public Graph() {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = allocate();
    }

    Graph(long j) {
        this.nativeHandleLock = new Object();
        this.refcount = 0;
        this.nativeHandle = j;
    }

    static int access$206(Graph graph) {
        int i = graph.refcount - 1;
        graph.refcount = i;
        return i;
    }

    @Override
    public void close() {
        synchronized (this.nativeHandleLock) {
            long j = this.nativeHandle;
            if (j != 0) {
                if (this.refcount > 0) {
                    try {
                        this.nativeHandleLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.currentThread().interrupt();
                    return;
                }
                delete(j);
                this.nativeHandle = 0;
            }
        }
    }

    public Operation operation(String str) {
        synchronized (this.nativeHandleLock) {
            long operation = operation(this.nativeHandle, str);
            if (operation == 0) {
                return null;
            }
            return new Operation(this, operation);
        }
    }

    public Iterator<Operation> operations() {
        return new OperationIterator(this);
    }

    public OperationBuilder opBuilder(String str, String str2) {
        return new OperationBuilder(this, str, str2);
    }

    public void importGraphDef(byte[] bArr) throws IllegalArgumentException {
        importGraphDef(bArr, "");
    }

    public void importGraphDef(byte[] bArr, String str) throws IllegalArgumentException {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("graphDef and prefix cannot be null");
        }
        synchronized (this.nativeHandleLock) {
            importGraphDef(this.nativeHandle, bArr, str);
        }
    }

    public byte[] toGraphDef() {
        byte[] graphDef;
        synchronized (this.nativeHandleLock) {
            graphDef = toGraphDef(this.nativeHandle);
        }
        return graphDef;
    }

    public Output<?>[] addGradients(Output<?>[] outputArr, Output<?>[] outputArr2, Output<?>[] outputArr3) {
        int[] iArr;
        long[] jArr;
        Output<?>[] outputArr4 = new Output[outputArr2.length];
        long[] jArr2 = new long[outputArr.length];
        int[] iArr2 = new int[outputArr.length];
        long[] jArr3 = new long[outputArr2.length];
        int[] iArr3 = new int[outputArr2.length];
        Reference ref = ref();
        for (int i2 = 0; i2 < outputArr.length; i2++) {
            jArr2[i2] = outputArr[i2].op().getUnsafeNativeHandle();
            iArr2[i2] = outputArr[i2].index();
        }
        for (int i3 = 0; i3 < outputArr2.length; i3++) {
            jArr3[i3] = outputArr2[i3].op().getUnsafeNativeHandle();
            iArr3[i3] = outputArr2[i3].index();
        }
        if (outputArr3 == null || outputArr3.length <= 0) {
            jArr = null;
            iArr = null;
        } else {
            long[] jArr4 = new long[outputArr3.length];
            int[] iArr4 = new int[outputArr3.length];
            for (int i4 = 0; i4 < outputArr3.length; i4++) {
                jArr4[i4] = outputArr3[i4].op().getUnsafeNativeHandle();
                iArr4[i4] = outputArr3[i4].index();
            }
            jArr = jArr4;
            iArr = iArr4;
        }
        long[] addGradients = addGradients(ref.nativeHandle(), jArr2, iArr2, jArr3, iArr3, jArr, iArr);
        int length = addGradients.length >> 1;
        if (length == outputArr4.length) {
            int i5 = length;
            int i = 0;
            while (i < length) {
                outputArr4[i] = new Output<>(new Operation(this, addGradients[i]), (int) addGradients[i5]);
                i++;
                i5++;
            }
            if (ref != null) {
                ref.close();
            }
            return outputArr4;
        }
        throw new IllegalStateException(length + " gradients were added to the graph when " + outputArr4.length + " were expected");
    }

    public Output<?>[] addGradients(Output<?> output, Output<?>[] outputArr) {
        return addGradients(new Output[]{output}, outputArr, null);
    }

    public Reference ref() {
        return new Reference();
    }

    private static final class OperationIterator implements Iterator<Operation> {
        private final Graph graph;
        private Operation operation = null;
        private int position = 0;

        OperationIterator(Graph graph2) {
            this.graph = graph2;
            advance();
        }

        private final void advance() {
            Reference ref = this.graph.ref();
            this.operation = null;
            try {
                long[] access$400 = Graph.nextOperation(ref.nativeHandle(), this.position);
                if (!(access$400 == null || access$400[0] == 0)) {
                    this.operation = new Operation(this.graph, access$400[0]);
                    this.position = (int) access$400[1];
                }
            } finally {
                ref.close();
            }
        }

        public boolean hasNext() {
            return this.operation != null;
        }

        @Override
        public Operation next() {
            Operation operation2 = this.operation;
            advance();
            return operation2;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() is unsupported.");
        }
    }


    public class Reference implements AutoCloseable {
        private boolean active;

        private Reference() {
            synchronized (Graph.this.nativeHandleLock) {
                boolean z = Graph.this.nativeHandle != 0;
                this.active = z;
                if (z) {
                    this.active = true;
                    Graph.this.refcount++;
                } else {
                    throw new IllegalStateException("close() has been called on the Graph");
                }
            }
        }

        @Override
        public void close() {
            synchronized (Graph.this.nativeHandleLock) {
                if (this.active) {
                    this.active = false;
                    if (Graph.access$206(Graph.this) == 0) {
                        Graph.this.nativeHandleLock.notifyAll();
                    }
                }
            }
        }

        public long nativeHandle() {
            long access$100;
            synchronized (Graph.this.nativeHandleLock) {
                access$100 = this.active ? Graph.this.nativeHandle : 0;
            }
            return access$100;
        }
    }
}
