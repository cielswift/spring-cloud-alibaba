package com.ciel.scatquick.zookeeper;

import org.apache.zookeeper.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class ZookClient {

    public static final String ADDRESS = "127.0.0.1:21810,127.0.0.1:21811,127.0.0.1:21812";

    //zkCli.cmd -server 127.0.0.1:21810 命令行连接
    //ls /  查看节点
    //create /ciel swift 创建节点并赋值 ;  get /ciel 取值 ; set /ciel xiapeixin 修改值
    //stat /ciel 查看节点状态 ;delete /ciel 删除节点 ; rmr 递归删除
    //ls /ciel watch ; get /ciel watch 监听节点和数据 只能监听一次

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        CountDownLatch countDown = new CountDownLatch(1);

        Watcher watcher= new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                System.out.println(String.format("触发路径 %s; 类型 %s,状态 %s",
                        event.getPath(),event.getType(),event.getState()));
                System.out.println(event.toString());

//                if (event.getState() == Event.KeeperState.SyncConnected) {
//                    System.err.println("eventType:"+event.getType());
//                    if(event.getType()==Event.EventType.None){
//                        countDown.countDown();
//                    }else if(event.getType()==Event.EventType.NodeCreated){
//                        System.out.println("listen:节点创建");
//                    }else if(event.getType()==Event.EventType.NodeChildrenChanged){
//                        System.out.println("listen:子节点修改");
//                    }
//                }
            }
        };

        ZooKeeper zookeeper = new ZooKeeper(ADDRESS, 2000,watcher);

        //判断节点是否存在
        //Stat exists = zookeeper.exists("/xia", watcher);

        // 创建节点
        String result = zookeeper.create("/xins", "fuck you mother".getBytes(),
                //任何人都能访问 //客户端断开连接后，znode不会自动删除。
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(result);

        // 获取节点
        byte[] bs = zookeeper.getData("/xins", true, null);
        System.out.println("创建节点后的数据是:" + new String(bs));

        // 修改节点
       zookeeper.setData("/xins", "FUCKYOUMOTHER".getBytes(), -1);
       bs = zookeeper.getData("/xins", true, null);
       System.out.println("修改节点后的数据是:" + new String(bs));

        // 删除节点
        zookeeper.delete("/xins", -1);
        System.out.println("节点删除成功");


    }
}
