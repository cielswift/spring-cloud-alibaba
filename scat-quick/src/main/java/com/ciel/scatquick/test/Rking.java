package com.ciel.scatquick.test;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rking {

    public static void main(String[] args) {

        long st = System.currentTimeMillis();
        List<UUID> collect = Stream.generate(UUID::randomUUID).parallel()
                .limit(200000).collect(Collectors.toList());
        collect.forEach( t -> {int a = 5;});
        System.out.println("time:" + (System.currentTimeMillis()-st));

    
        task(0);

        String reg = "\\d{2}\\w";

        String str = "29c";

        System.out.println(str.matches(reg));

        Pattern pa = Pattern.compile(reg);
        Matcher matcher = pa.matcher(str);

        if(matcher.find()){
            System.out.println(matcher.group(0));
        }
    }

    public static void task(int x){
        System.out.println(x+=1);
        task(x);
    }
}
