package com.relish.app.drip.org.tensorflow;

public interface Operand<T> {
    Output<T> asOutput();
}
