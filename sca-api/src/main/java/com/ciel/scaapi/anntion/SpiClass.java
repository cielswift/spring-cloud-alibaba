package com.ciel.scaapi.anntion;

public class SpiClass implements SpiInterface {
    @Override
    public String vue(String in) {
        System.out.println(in+"<<<");
        return in+">>";
    }
}
