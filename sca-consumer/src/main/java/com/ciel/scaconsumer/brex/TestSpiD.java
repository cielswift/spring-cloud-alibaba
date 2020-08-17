package com.ciel.scaconsumer.brex;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * dubbo spi 机制
 */
public class TestSpiD {

    public static void main(String[] args) {

        ExtensionLoader<DubboSpi> loader = ExtensionLoader.getExtensionLoader(DubboSpi.class);

        DubboSpi cooling = loader.getExtension("cooling");

        System.out.println(cooling.flng(8));
        
    }
}