package com.roche.poc.controller;

public class BinarySearch {

    public static boolean binarySearchRecursive(int[] array, int x, int left, int right) {
        if(left > right) {
            return false;
        }

        int mid = (left + right) / 2;
        if(array[mid] == x) return true;
        if(x < array[mid]) {
            return binarySearchRecursive(array, x, left, mid - 1);
        } else {
            return binarySearchRecursive(array, x, mid + 1, right);
        }
    }

    public static boolean binarySearchRecursiveWhile(int[] array, int x, int left, int right) {
        while(left <= right) {
            int mid = left + ((right - left) / 2);
            if (array[mid] == x) return true;
            if (x < array[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }

    public static void main(String[] a) {
        BinarySearch binarySearch = new BinarySearch();
        binarySearch.binarySearchRecursiveWhile(new int[]{0,1,2,3,4, 5}, 3, 0, 4);
    }

}
