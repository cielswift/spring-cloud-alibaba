package com.ciel.scatquick.anntion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@RepAction(value = "bb")
@RepAction(value = "aa")
public class J8JVM<@RepAction("cc") T> {
    public static void main(String[] args) throws IOException {

        RepAction annotation = J8JVM.class.getAnnotation(RepAction.class); //null

        RepAction[] annotationsByType = J8JVM.class.getAnnotationsByType(RepAction.class); //获取多个重复的注解

        Annotation[] annotations = J8JVM.class.getAnnotations(); //这里看到 其实是RepActions ,重复注解的容器

        System.out.println(annotation);

        Arrays.stream(annotationsByType).forEach(System.out::println);

        Arrays.stream(annotations).forEach(System.out::println);

        try (FileInputStream file = new FileInputStream(new File(J8JVM.class.getResource("./J8JVM.class").toString()))) { //自动关闭流

        } catch (Exception e) {

        }


        //java 运算符号

        int a = 1 << 30;

        int b = 64 >> 2;

        int c = 6 ^ 3;
//0110 //相同的为0，不同的为1
//0011
//0101
        int d = 6 & 3;
//0110 //一个为0即为0
//0011
//0010
        int e = 6 | 3;
//0110 //一个为1即为1
//0011
//0111
        System.out.println(e);

        //jps
        //jstack 9950

        // jhsdb jmap --heap --pid 1284

//        jstat -参数 线程id 执行时间（单位毫秒） 执行次数
//        jstat -gc 19098 200 5  # 每隔200ms查看一次GC和堆的相关信息, 共查看5次

//        S0C：第一个幸存区的大小
//        S1C：第二个幸存区的大小
//        S0U：第一个幸存区的使用大小
//        S1U：第二个幸存区的使用大小
//        EC：伊甸园区的大小
//        EU：伊甸园区的使用大小
//        OC：老年代大小
//        OU：老年代使用大小
//        MC：方法区大小
//        MU：方法区使用大小
//        CCSC:压缩类空间大小
//        CCSU:压缩类空间使用大小
//        YGC：年轻代垃圾回收次数
//        YGCT：年轻代垃圾回收消耗时间
//        FGC：老年代垃圾回收次数
//        FGCT：老年代垃圾回收消耗时间
//        GCT：垃圾回收消耗总时间

//        -Xms:初始堆大小，JVM启动的时候，给定堆空间大小。
//
//        -Xmx:最大堆大小，JVM运行过程中，如果初始堆空间不足的时候，最大可以扩展到多少。
//
//        -Xmn：设置年轻代大小。整个堆大小=年轻代大小+年老代大小+元空间大小。元空间一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
//
//        -Xss： 设置每个线程的Java栈大小。JDK5.0以后每个线程Java栈大小为1M，以前每个线程堆栈大小为256K。根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
//
//        -XX:NewSize=n:设置年轻代大小
//
//                -XX:NewRatio=n:设置年轻代和年老代的比值。如:为3，表示年轻代与年老代比值为1：3，年轻代占整个年轻代+年老代和的1/4
//
//                -XX:SurvivorRatio=n:年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
//
//                -XX:MaxPermSize=n:设置持久代大小。不推荐手工设置。
//
//        -XX:MaxTenuringThreshold：设置垃圾最大年龄。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代。对于年老代比较多的应用，可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象再年轻代的存活时间，增加在年轻代即被回收的概率。

    }

}