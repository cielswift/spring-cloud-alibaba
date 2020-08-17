package com.ciel.scaconsumer.brex;

import org.apache.dubbo.common.extension.SPI;

/**
 * dubbo spi机制
 */
@SPI
public interface DubboSpi {

    public String flng(int ing);
}
