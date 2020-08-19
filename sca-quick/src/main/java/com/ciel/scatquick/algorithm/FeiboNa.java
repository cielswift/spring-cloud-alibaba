package com.ciel.scatquick.algorithm;

/**
 * 斐波那契数列
 */
public class FeiboNa {

    public static void main(String[] args) {

        int[] febon = febon(30);

        for (int i : febon) {
            System.out.print(i + "_");
        }
    }

    public static int[] febon(int len){
        int [] afs = new int[len];
        for (int i = 0; i < afs.length; i++) {
            if(i==0){
                afs[i]=0;
            }else if(i == 1){
                afs[i] = 1;
            }else{
                afs[i] = afs[i-1]+afs[i-2];
            }
        }
        return afs;
    }
}
