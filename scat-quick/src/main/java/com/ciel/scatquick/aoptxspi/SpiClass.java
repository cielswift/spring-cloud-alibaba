package com.ciel.scatquick.aoptxspi;

public class SpiClass implements SpiInterface {

    @Override
    public String vue(String in) {

        System.out.println("SPI 机制的加载调用");

        return in+">> SPI";
    }
}
