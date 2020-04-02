package com.ciel.scatquick.anntion;

import java.util.ServiceLoader;

public class Spi {

    public static void main(String[] args) {

        ServiceLoader<SpiInterface> serviceLoader = ServiceLoader.load(SpiInterface.class);

        serviceLoader.forEach(t -> {

            t.vue("a");
        });
    }

    static {
        System.out.println("aaaaaaaaaa");
    }
}
