package com.shaopeng.gateway.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * map to object
 *
 * Create by liushaopeng on 2019/4/13  10:30
 */
public class MapObjUtil {

    /**
     * 将<code> Map<String,String> </code>转换成对象
     * 只支持基本类型的对象的dto，从http请求过来的转化专用
     *
     * @param map       http参数对象集合
     * @param beanClass 被转换的class类型
     * @param <T>       泛型T
     * @return
     * @throws Exception
     */
    public static <T> T map2Object(Map<String, String> map, Class<T> beanClass) throws Exception {
        if (map == null || map.size() <= 0)
            return null;

        T obj = beanClass.newInstance();


        //获取关联的所有类，本类以及所有父类
        Class oo = beanClass;
        List<Class> clazzs = new ArrayList<Class>();
        while (true) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
            if (oo == null || oo == Object.class) break;
        }

        Map<String, Field> nameFieldMap = new HashMap<String, Field>();
        for (int i = 0; i < clazzs.size(); i++) {
            Field[] fields = clazzs.get(i).getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod)) {
                    continue;
                }
                nameFieldMap.put(field.getName(), field);
            }
        }

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String originName = entry.getKey();
            String name = camelName(originName);
            Field field = nameFieldMap.get(name);
            if (null == field) {
                continue;
            }
            setFieldValue(field, (String) entry.getValue(), obj);

        }


        return obj;
    }


    private static String camelName(String paramName) {
        String[] spilts = paramName.split("_");
        if (spilts.length == 1) {
            return paramName;
        }

        StringBuilder sb = new StringBuilder(spilts[0]);
        for (int i = 1; i < spilts.length; i++) {
            String midName = upperFirstChar(spilts[i]);
            sb.append(midName);
        }
        return sb.toString();
    }

    private static String upperFirstChar(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    private static void setFieldValue(Field field, String valueStr, Object obj) throws IllegalAccessException {
        Class fieldClass = field.getType();

        field.setAccessible(true);
        if (Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass)) {
            if ("1".equals(valueStr)) {
                field.set(obj, true);
                return;
            }
            field.set(obj, Boolean.parseBoolean(valueStr));
        } else if (Byte.class.equals(fieldClass) || byte.class.equals(fieldClass)) {
            field.set(obj, Byte.valueOf(valueStr));
        } else if (Character.class.equals(fieldClass) || char.class.equals(fieldClass)) {
            field.set(obj, valueStr.charAt(0));
        } else if (Double.class.equals(fieldClass) || double.class.equals(fieldClass)) {
            field.set(obj, Double.valueOf(valueStr));
        } else if (Float.class.equals(fieldClass) || float.class.equals(fieldClass)) {
            field.set(obj, Float.valueOf(valueStr));
        } else if (Integer.class.equals(fieldClass) || int.class.equals(fieldClass)) {
            field.set(obj, Integer.valueOf(valueStr));
        } else if (Long.class.equals(fieldClass) || long.class.equals(fieldClass)) {
            field.set(obj, Long.valueOf(valueStr));

        } else if (Short.class.equals(fieldClass) || short.class.equals(fieldClass)) {
            field.set(obj, Short.valueOf(valueStr));
        } else {
            field.set(obj, valueStr);
        }

    }

}
