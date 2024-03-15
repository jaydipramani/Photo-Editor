package com.relish.app.drip.org.tensorflow;

import com.relish.app.drip.org.tensorflow.types.UInt8;

import java.util.HashMap;
import java.util.Map;

public enum DataType {
    FLOAT(1),
    DOUBLE(2),
    INT32(3),
    UINT8(4),
    STRING(7),
    INT64(9),
    BOOL(10);
    
    private static Map<Class<?>, DataType> typeCodes;
    private static DataType[] values;
    private final int value;

    static {
        DataType dataType = FLOAT;
        DataType dataType2 = DOUBLE;
        DataType dataType3 = INT32;
        DataType dataType4 = UINT8;
        DataType dataType5 = STRING;
        DataType dataType6 = INT64;
        DataType dataType7 =BOOL;

        typeCodes = null;
        values = null;
        values = values();
        HashMap hashMap = new HashMap();
        typeCodes = hashMap;
        hashMap.put(Float.class, dataType);
        typeCodes.put(Double.class, dataType2);
        typeCodes.put(Integer.class, dataType3);
        typeCodes.put(UInt8.class, dataType4);
        typeCodes.put(Long.class, dataType6);
        typeCodes.put(Boolean.class, dataType7);
        typeCodes.put(String.class, dataType5);
    }

    private DataType(int i) {
        this.value = i;
    }

    public int c() {
        return this.value;
    }

    static DataType fromC(int i) {
        DataType[] dataTypeArr = values;
        for (DataType dataType : dataTypeArr) {
            if (dataType.value == i) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("DataType " + i + " is not recognized in Java (version " + TensorFlow.version() + ")");
    }

    public static DataType fromClass(Class<?> cls) {
        DataType dataType = typeCodes.get(cls);
        if (dataType != null) {
            return dataType;
        }
        throw new IllegalArgumentException(cls.getName() + " objects cannot be used as elements in a TensorFlow Tensor");
    }
}
