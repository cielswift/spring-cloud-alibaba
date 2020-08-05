package com.ciel.scatquick.jvmdb;

import org.ehcache.xml.model.Heap;
import sun.tools.jar.resources.jar;

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

//    Heap Configuration:   #堆的内存配置
//            MinHeapFreeRatio         = 0
//    MaxHeapFreeRatio         = 100
//            # 堆内存的最大值
//            MaxHeapSize       = 8589934592 (8192.0MB)
//            # 年轻代内存的大小
//            NewSize           = 3221225472 (3072.0MB)
//            # 年轻代内存的最大值
//            MaxNewSize        = 3221225472 (3072.0MB)
//            # 老年代内存的大小
//            OldSize           = 5368709120 (5120.0MB)
//            # 老年代和年轻代空间大小的比率
//    # 因为设置Xmn参数，该设置未生效
//            NewRatio                 = 2
//   #Eden区和一个Survivor区的空间大小的比率
//            SurvivorRatio            = 4
//    # 元空间第一次触发垃圾回收的内存大小
//            MetaspaceSize       = 268435456 (256.0MB)
//            # 元空间内存的最大值
//            MaxMetaspaceSize    = 536870912 (512.0MB)
//    G1HeapRegionSize    = 0 (0.0MB)
//
//    Heap Usage: # 堆的使用情况
//    PS Young Generation
//    Eden Space: # Eden区内存的使用情况
//            capacity = 2147483648 (2048.0MB)
//    used     = 901945720 (860.16MB)
//    free     = 1245537928 (1187.83MB)
//            42.000120505690575% used
//    From Space: # Survivor的From区内存的使用情况
//            capacity = 536870912 (512.0MB)
//    used     = 0 (0.0MB)
//    free     = 536870912 (512.0MB)
//            0.0% used
//    To Space: # Survivor的To区内存的使用情况
//            capacity = 536870912 (512.0MB)
//    used     = 0 (0.0MB)
//    free     = 536870912 (512.0MB)
//            0.0% used
//    PS Old Generation # 老年代内存的使用情况
//            capacity = 5368709120 (5120.0MB)
//    used     = 0 (0.0MB)
//    free     = 5368709120 (5120.0MB)
//            0.0% used
 //   -------------------------------------------------------------------------------------------
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

  //  java -server -Xmx8G -Xms8G -Xmn3G -XX:SurvivorRatio=4 -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=512M -jar one-more-study-0.0.1-SNAPSHOT.jar

    //-Xms2048m -Xmx2048m
//  -Xms6g 初始堆大小，JVM启动的时候，给定堆空间大小。
//   -Xmx6g 最大堆大小，JVM运行过程中，如果初始堆空间不足的时候，最大可以扩展到多少。
//   -Xmn2g 设置年轻代大小。整个堆大小=年轻代大小+年老代大小+元空间大小。元空间一般固定大小为64m，
//                所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
//   -XX:NewRatio=1 指定老年代和年轻代空间大小的比率。默认为2，即老年代和年轻代空间大小的比率为2:1，
//                      年轻代占整个堆内存空间大小的1/3下面的例子是把老年代和年轻代空间大小的比率设置为1 比Xmn优先级低;
//    -XX:SurvivorRatio=4 指定Eden区和一个Survivor区的空间大小的比率。默认为8，即Eden区和一个Survivor区的空间大小为8:1，
//                  因为一共有两个Survivor区，所以Eden区占年轻代内存大小的80%。
//                  下面的例子是把Eden区和一个Survivor区的空间大小的比率设置为4
//   -XX:MetaspaceSize=256M 指定元空间第一次触发垃圾回收的内存大小的阈值
//   -XX:MaxMetaspaceSize=512M 指定元空间所分配内存的最大值
//   -Xss： 设置每个线程的Java栈大小。JDK5.0以后每个线程Java栈大小为1M，以前每个线程堆栈大小为256K。
//                  根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。
//                  但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
//
//
//    -XX:MaxTenuringThreshold：设置垃圾最大年龄。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代。
//    对于年老代比较多的应用，可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，
//    这样可以增加对象再年轻代的存活时间，增加在年轻代即被回收的概率。




}
