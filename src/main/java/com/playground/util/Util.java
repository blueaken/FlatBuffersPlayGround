package com.playground.util;

import com.google.flatbuffers.FlatBufferBuilder;
import com.playground.flatbuffers.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.ByteBuffer;

// @coderabbitai review
class Input {
    String type;
    Integer offset;

    Input(String type, Integer offset){
        this.type = type;
        this.offset = offset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}

public class Util {

    public static String FLATBUFFERS_CLASS_PATH = "com.playground.flatbuffers.";
    public static Map<String, Input> valueOffsetsMap = new HashMap<>();
    public static Map<String, Set> objectFieldsMap = new HashMap<>();

    public static String decode(String encodedString) {
        String[] bufferStringArr = encodedString.split(",");
        String rootType = bufferStringArr[0];
        String bufferString = bufferStringArr[1];

        byte[] responseBuf = Base64.getDecoder().decode(bufferString);
        ByteBuffer buffer = ByteBuffer.wrap(responseBuf);

        System.out.println("=================================");
        System.out.println("Decoded buffer is like following: ");
        System.out.println();

        //使用反射调用相应的构造函数创建对象，根据root_type动态调用相应的构造函数
        try {
            // start root type class FlatBufferBuilder
            Class<?> clazz = Class.forName(FLATBUFFERS_CLASS_PATH + rootType);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Method getRootMethod = clazz.getMethod("getRootAs" + rootType, ByteBuffer.class);
            instance = getRootMethod.invoke(instance, buffer);

            Method[] methods = clazz.getDeclaredMethods();
            for (String key : valueOffsetsMap.keySet()) {
                for (Method method : methods) {
                    if (method.getName().equals(key)) {
                        if (method.getParameterTypes().length == 0) {
                            // Scalar || Object
                            Method getMethod = clazz.getMethod(key);
                            Object value = getMethod.invoke(instance);
                            Class<?> returnType = method.getReturnType();
                            switch (returnType.getName()) {
                                case "short":
                                    System.out.println(key + ": " + (Short) value);
                                    break;
                                case "java.lang.String":
                                    System.out.println(key + ": " + (String) value);
                                    break;
                                case "int":
                                    System.out.println(key + ": " + (Integer) value);
                                    break;
                                default:
                                    // Object
                                    Class<?> objectClazz = Class.forName(returnType.getName());
                                    Object objectInstance = objectClazz.cast(value);

                                    Method[] objectMethods = objectClazz.getDeclaredMethods();
                                    Set<String> objectFields = objectFieldsMap.get(key);
                                    for (String objectField : objectFields) {
                                        for (Method objectMethod : objectMethods) {
                                            if (objectMethod.getName().equals(objectField)) {
                                                Method objectGetMethod = objectClazz.getMethod(objectField);
                                                Object objectValue = objectGetMethod.invoke(objectInstance);
                                                Class<?> objectReturnType = objectGetMethod.getReturnType();

                                                switch (objectReturnType.getName()) {
                                                    case "short":
                                                        System.out.println(key + "'s "+ objectField +" is: " + (Short) objectValue);
                                                        break;
                                                    case "java.lang.String":
                                                        System.out.println(key + "'s "+ objectField +" is: "+ (String) objectValue);
                                                        break;
                                                    case "int":
                                                        System.out.println(key + "'s "+ objectField +" is: " + (Integer) objectValue);
                                                        break;
                                                    default:
                                                        System.out.println("Not supported data type in Object Decode block!");
                                                }
                                            }
                                        }
                                    }

                            }
                        } else if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == int.class) {
                            // Vector
                            Method getLengthMethod = clazz.getMethod(key + "Length");
                            Object length = getLengthMethod.invoke(instance);
                            System.out.println("This " + rootType + " has "+ length + " " + key + ":");
                            Method getMethod = clazz.getMethod(key, int.class);
                            Class<?> returnType = getMethod.getReturnType();
                            key = key.substring(0, key.length()-1); // remove last char "s" for array key
                            for (int i = 0; i < (Integer)length; i++) {
                                Object value = getMethod.invoke(instance, i);
                                switch (returnType.getName()) {
                                    case "short":
                                    case "String":
                                    case "int":
                                        System.out.println(key + " " + (i+1) +  " is " + value);
                                        break;
                                    default:
                                        //object
                                        Class<?> objectClazz = Class.forName(returnType.getName());
                                        Object objectInstance = objectClazz.cast(value);

                                        Method[] objectMethods = objectClazz.getDeclaredMethods();
                                        Set<String> objectFields = objectFieldsMap.get(key);
                                        for (String objectField : objectFields) {
                                            for (Method objectMethod : objectMethods) {
                                                if (objectMethod.getName().equals(objectField)) {
                                                    Method objectGetMethod = objectClazz.getMethod(objectField);
                                                    Object objectValue = objectGetMethod.invoke(objectInstance);
                                                    Class<?> objectReturnType = objectGetMethod.getReturnType();

                                                    switch (objectReturnType.getName()) {
                                                        case "short":
                                                            System.out.println(key + " " + (i+1) + "'s "+ objectField +" is: " + (Short) objectValue);
                                                            break;
                                                        case "java.lang.String":
                                                            System.out.println(key + " " + (i+1) + "'s "+ objectField +" is: "+ (String) objectValue);
                                                            break;
                                                        case "int":
                                                            System.out.println(key + " " + (i+1) + "'s "+ objectField +" is: " + (Integer) objectValue);
                                                            break;
                                                        default:
                                                            System.out.println("Not supported data type in Object Decode block!");
                                                    }
                                                }
                                            }
                                        }

                                }

                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in decode reflection block.");
            e.printStackTrace();
        }

        System.out.println("=================================");

        return "Decode complete.";
    }

    public static String encode(String inputJsonString) {
        JSONObject obj = new JSONObject(inputJsonString);
        String rootType = obj.getString("root_type");

        String res = null;
        //使用反射调用相应的构造函数创建对象，根据root_type动态调用相应的构造函数
        try {
            Class<?> clazz = Class.forName(FLATBUFFERS_CLASS_PATH + rootType);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            FlatBufferBuilder builder = new FlatBufferBuilder(1024);

            buildOffsetValues(obj, clazz, instance, builder);

            Method startMethod = clazz.getMethod("start" + rootType, FlatBufferBuilder.class);
            startMethod.invoke(instance, builder);
            parseOffsetMap(obj, clazz, instance, builder);
            Method endMethod = clazz.getMethod("end" + rootType, FlatBufferBuilder.class);
            int offset = (Integer) endMethod.invoke(instance, builder);

            // 调用finish方法生成Root Type对象缓冲区Base64编码字符串
            builder.finish(offset);
            byte[] requestBuf = builder.sizedByteArray();
            res = Base64.getEncoder().encodeToString(requestBuf);
            // add rootType into the result string
            res = rootType + "," + res;
        } catch (Exception e) {
            System.out.println("Exception in encode reflection block.");
            e.printStackTrace();
        }

        return  res;
    }

    private static void parseOffsetMap(JSONObject jsonObject, Class<?> clazz, Object instance, FlatBufferBuilder builder) {
        for (String key : jsonObject.keySet()) {
            if (key.equals("root_type")) {
                continue;
            }
//            Object value = jsonObject.get(key);

                try {
                    Input valueMap = valueOffsetsMap.get(key);
                    switch (valueMap.type) {
                        case "int":
                        case "array":
                        case "object":
                        {
                            Method addMethod = clazz.getMethod("add" + capitalizeFirstLetter(key), FlatBufferBuilder.class, int.class);
                            addMethod.invoke(instance, builder, valueMap.getOffset().intValue());
                            break;
                        }
                        case "short":
                        {
                            Method addMethod = clazz.getMethod("add" + capitalizeFirstLetter(key), FlatBufferBuilder.class, short.class);
                            addMethod.invoke(instance, builder, valueMap.getOffset().shortValue());
                            break;
                        }
                        default:
                            Method addMethod = clazz.getMethod("add" + capitalizeFirstLetter(key), FlatBufferBuilder.class, int.class);
                            addMethod.invoke(instance, builder, valueMap.getOffset());
                    }

                } catch (Exception e) {
                    System.out.println("Exception in parseObject reflection block.");
                    e.printStackTrace();
                }

        }
    }

    private static void buildOffsetValues(JSONObject jsonObject, Class<?> clazz, Object instance, FlatBufferBuilder builder) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(key)) {
                        try {
                            Class<?> returnType = method.getReturnType();
                            Class<?> objectClazz = Class.forName(returnType.getName());
                            Object objectInstance = objectClazz.getDeclaredConstructor().newInstance();
                            buildObjectValues(key, (JSONObject) value, objectClazz, objectInstance, builder, false);
                        } catch (Exception e) {
                            System.out.println("Exception in buildOffsetValues JSONObject reflection block.");
                            e.printStackTrace();
                        }
                    }
                }

            } else if (value instanceof JSONArray) {
                buildArrayValue(key, (JSONArray) value, clazz, instance, builder);
            } else {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals("add" + capitalizeFirstLetter(key))) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        // 1st parameter is FB builder, 2nd parameter is the type of the value
                        String inputType = parameterTypes[1].getName();
                        int offset = Integer.MIN_VALUE;

                        switch (inputType) {
                            case "short":
                                offset = (Integer) value;
                                Input input = new Input(inputType, offset);
                                valueOffsetsMap.put(key, input);
                                break;
                            case "int":
                                if (value instanceof java.lang.String) {
                                    offset = builder.createString((String) value);
                                    input = new Input(inputType, offset);
                                    valueOffsetsMap.put(key, input);
                                } else if (value instanceof Integer) {
                                    offset = (Integer) value;
                                    input = new Input(inputType, offset);
                                    valueOffsetsMap.put(key, input);
                                }
                                break;
                            default:
                                offset = (Integer) value;
                                input = new Input(inputType, offset);
                                valueOffsetsMap.put(key, input);
                        }

                    }

                }
            }
        }
    }

    private static int buildObjectValues(String key, JSONObject jsonObject, Class<?> clazz, Object instance, FlatBufferBuilder builder, boolean isArray) {
        Set<String> objectFields = new HashSet<>();
        Map<Integer, Input> objectValueOffsetMap = new HashMap<>();
        int res = Integer.MIN_VALUE;
        if (isArray) {
            // for array remove the last 's' char
            key = key.substring(0, key.length()-1);
        }
        try {
            for (String objectKey : jsonObject.keySet()) {
                objectFields.add(objectKey);
                Object value = jsonObject.get(objectKey);

                if (value instanceof JSONObject) {
                    buildObjectValues(key, (JSONObject) value, clazz, instance, builder, isArray);
                } else if (value instanceof JSONArray) {
                    buildArrayValue(key, (JSONArray) value, clazz, instance, builder);
                } else {
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods){
                        if (method.getName().equals("create" + capitalizeFirstLetter(key))) {
                            Parameter[] parameters = method.getParameters();
                            for (int i = 0; i < parameters.length; i++) {
                                String parameterName = parameters[i].getName();
                                String parameterType = parameters[i].getType().getName();
                                if (parameterName.startsWith(objectKey)) {
                                    int valueOffset = Integer.MIN_VALUE;
                                    if (value instanceof java.lang.String) {
                                        valueOffset = builder.createString((String) value);
                                        Input input = new Input(parameterType, valueOffset);
                                        objectValueOffsetMap.put(i, input);
                                    } else if (value instanceof Integer) {
                                        valueOffset = (Integer) value;
                                        Input input = new Input(parameterType, valueOffset);
                                        objectValueOffsetMap.put(i, input);
                                    } else if (value instanceof Short) {
                                        valueOffset = (Short) value;
                                        Input input = new Input(parameterType, valueOffset);
                                        objectValueOffsetMap.put(i, input);
                                    }
                                }
                            }

                        }
                    }
                }
            }

            objectFieldsMap.putIfAbsent(key, objectFields);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                if (method.getName().equals("create" + capitalizeFirstLetter(key))) {

                    int len = objectValueOffsetMap.keySet().size(); // parameter length must > 0, since an object must have at least one parameter
                    Object[] args = new Object[len+1];
                    args[0]  = builder;
                    for (int i = 0; i < len; i++) {
                        int curIndex = i+1; // the 1st parameter is always the builder
                        String inputType = objectValueOffsetMap.get(curIndex).getType();
                        if (inputType.equals("short")) {
                            args[curIndex] = objectValueOffsetMap.get(curIndex).getOffset().shortValue();
                        } else {
                            args[curIndex] = objectValueOffsetMap.get(curIndex).getOffset();
                        }
                    }
                    Object objectOffset = method.invoke(instance, args);
                    if (isArray) {
                        res = (Integer)objectOffset;
                    } else {
                        Input input = new Input("object", (Integer)objectOffset);
                        valueOffsetsMap.put(key, input);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in buildObjectValues reflection block.");
            e.printStackTrace();
        }
        return res;
    }

    private static void buildArrayValue (String key, JSONArray jsonArray, Class<?> clazz, Object instance, FlatBufferBuilder builder) {
        int len = jsonArray.length();
        int[] offsets = new int[len];

        for (int i = 0; i < len; i++) {
            Object item = jsonArray.get(i);
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().equals(key)) {
                    Class<?> returnType = method.getReturnType();
                    switch (returnType.getName()) {
                        case "java.lang.String":
                            offsets[i] = builder.createString((String) item);
                            break;
                        case "int":
                            offsets[i] = (Integer) item;
                            break;
                        case "short":
                            offsets[i] = (Short) item;
                            break;
                        default:
                            // object
                            try {
                                Class<?> objectClazz = Class.forName(returnType.getName());
                                Object objectInstance = objectClazz.getDeclaredConstructor().newInstance();
                                offsets[i] = buildObjectValues(key, (JSONObject) item, objectClazz, objectInstance, builder, true);
                            } catch (Exception e) {
                                System.out.println("Exception in buildArrayValue JSONObject reflection block.");
                                e.printStackTrace();
                            }
                            break;
                    }
                }

            }
        }

        try {
            Method createMethod = clazz.getMethod("create" + capitalizeFirstLetter(key) + "Vector", FlatBufferBuilder.class, int[].class);
            Object offset = createMethod.invoke(instance, builder, offsets);

            Input input = new Input("array", (Integer)offset);
            valueOffsetsMap.put(key, input);

        } catch (Exception e) {
            System.out.println("Exception in buildArrayValue reflection block.");
            e.printStackTrace();
        }

    }

    private static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
