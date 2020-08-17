package com.ciel.scaconsumer.brex;

public class DubboSpiImpl implements DubboSpi {

    @Override
    public String flng(int ing) {
        return String.valueOf(ing).concat("xia");
    }
}
