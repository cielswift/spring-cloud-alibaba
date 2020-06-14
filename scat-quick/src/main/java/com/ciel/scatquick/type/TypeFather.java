package com.ciel.scatquick.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeFather implements Fatherz<String, Integer> {

    public static void main(String[] args) {

       // Type type = TypeFather.class.getGenericSuperclass(); //获取父类的类型

        Type type = TypeFather.class.getGenericInterfaces()[0]; //获取接口类型

        if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;

            Type[] types = parameterizedType.getActualTypeArguments(); //获取泛型

            if (types.length > 0) {
                String simpleName = ((Class) types[0]).getSimpleName();

                System.out.println(simpleName);
            }
        }
    }


    @Override
    public String monery(String s) {
        return s.concat("_____");
    }
}
