package com.ciel.scatquick.algorithm;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 折中查找
 */
public class LockMid {
    public static void main(String[] args) {

        int[] arr = {1, 3, 9, 10, 50};

        int ofSort = binarySearch(10, arr);

        System.out.println(ofSort);

    }

    public static int binarySearch(int findElem,int[] arr) {
        int low = 0;
        int high = arr.length - 1;
        int mid;
        stop: while (low <= high) {
            mid = (low + high) / 2;

            if (arr[mid] == findElem) {

                return mid;
            }

            //如果要查找的元素findElem小于中间位置的元素mid，指向数组的较大端的high索引重新指向中间索引mid的左边（mid-1）
            if (findElem < arr[mid]) {
                high = mid - 1;
            }

            //如果要查找的元素findElem大于中间位置的元素mid，指向数组的较小端的low索引重新指向中间索引mid的右边（mid+1）
            if (findElem > arr[mid]) {
                low = mid + 1;
            }
        }
        return -1;
    }

}
