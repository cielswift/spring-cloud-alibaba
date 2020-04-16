package com.ciel.scatquick.anntion;

import java.util.ServiceLoader;

/**
 * 测试java spi机制
 */
public class SpiClass implements SpiInterface {

    @Override
    public String vue(String in) {
        System.out.println(in+"<<<");
        return in+">>";
    }

}
