package com.hotsauce.meem;

import java.util.ArrayList;

/*
Utility class for miscellaneous util methods.
 */

public class Utils {

    public Integer[] integerArrayListToIntegerList(ArrayList<Integer> list) {
        /*
            This method converts an ArrayList of type Integer to a Integer array
         */
        Integer[] l = new Integer[list.size()];
        for (int i = 0 ; i < list.size() ; i ++) {
            l[i] = list.get(i);
        }
        return l;
    }
}
