package com.ciel.scatquick.jvmdb;

public class Jvm {
    /*
    那么，顺理成章的，应该建立两块Survivor区，刚刚新建的对象在Eden中，经历一次Minor GC，
    Eden中的存活对象就会被移动到第一块survivor space S0，Eden被清空；等Eden区再满了，就再触发一次Minor GC，
    Eden和S0中的存活对象又会被复制送入第二块survivor space S1（这个过程非常重要
    ，因为这种复制算法保证了S1中来自S0和Eden两部分的存活对象占用连续的内存空间，避免了碎片化的发生）。
    S0和Eden被清空，然后下一轮S0与S1交换角色，如此循环往复。如果对象的复制次数达到16次，该对象就会被送到老年代中

    上述机制最大的好处就是，整个过程中，永远有一个survivor space是空的，另一个非空的survivor space无碎片。
     */

    public static void main(String[] args) {

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

    }

//  -XX:+PrintGCDetails -Xloggc:gc.log 打印gc日志 访问https://www.gceasy.io/


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

    //-Xms2048m -Xmx2048m
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

//             -Xss：设置一个线程栈的大小；
//            -Xms：设置堆的最小值（初始值）；
//            -Xmx：设置堆的最大值；
//            -Xmn：设置年轻代的大小，表示NewSize=MaxNewSize=Xmn，一般建议该参数来设置新生代大小，避免新生代扩容，优先级低于-XX:NewSize和-XX:MaxNewSize参数；
//            -XX:NewSize：设置年轻代最小值（初始值）；
//            -XX:MaxNewSize：设置年轻代最大值；
//            -XX:NewRatio：设置年轻代和年老代的比值。如：为3，表示年轻代:年老代=1:3，年轻代占整个年轻代年老代和的1/4；
//            -XX:SurvivorRatio：设置年轻代中Eden区与两个Survivor区的比值。如：3，表示Eden:Survivor=3:2，一个Survivor区占整个年轻代的1/5；
//            -XX:PermSize： 设置方法区初始大小（JDK1.7及以前）；
//            -XX:MaxPermSize： 设置方法区最大大小（JDK1.7及以前）；
//            -XX:MetaspaceSize： 设置元数据区初始值（JDK1.8及以后）；
//            -XX:MaxMetaspaceSize：设置 元数据区最大值（JDK1.8及以后）；
//            -XX:MaxDirectMemorySize：设置直接内存大小，默认与堆内存最大值一样（-Xmx）；


}
