package com.esri.android.panama;

/**
 * Created by semillero on 07/01/2015.
 */
public class StringUtils {

    /**
     * Returns whether or not the string is not empty.
     *
     * @param str The String to check for emptiness
     * @return whether or not the string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Returns whether or not the string is empty. Note: returns true if the string has a value of "null".
     *
     * @param str the String to check for emptiness
     * @return whether or not the string is empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
