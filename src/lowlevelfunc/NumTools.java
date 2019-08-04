package lowlevelfunc;

/**
 *
 * Utilities to convert numbers and strings in different bases and format. Where
 * overloading is not possible, the conversion methods come in two versions, one
 * using int and the other using BigInteger (to convert bigger numbers).
 * The order used in the strings is MSBF (Most Significant Bit First).
 * 
 * @author  Luca Mariot
 * 
 */

import java.math.BigInteger;
import java.util.Vector;

public class NumTools {
    
    /**
     * Convert a boolean array in an int array of 0s (-> false) and 1s (-> true)
     * 
     * @param boolStr   A boolean array to be converted in int array
     * @return          An int array representing the conversion of boolStr in
     *                  0s (false) and 1s (true)
     */
    public static int[] bool2Int(boolean[] boolStr) {
        
        int[] intStr = new int[boolStr.length];
        
        for(int i=0; i<boolStr.length; i++) {
            
            if(boolStr[i]) {
                
                intStr[i] = 1;
                
            }
            
        }
        
        return intStr;
        
    }
    
    /**
     * Convert an int array composed of only 0s and 1s in a boolean array
     * (0 -> false, 1 -> true).
     * 
     * @param intStr    An int array composed only of 0s and 1s to be converted
     *                  in a boolean array
     * @return          A boolean array representing the conversion of intStr
     *                  (0 -> false, 1 -> true)
     */
    public static boolean[] int2Bool(int[] intStr) {
        
        boolean[] boolStr = new boolean[intStr.length];
        
        for(int i=0; i<intStr.length; i++) {
            
            if(intStr[i] == 1) {
                
                boolStr[i] = true;
                
            }
            
        }
        
        return boolStr;
        
    }
    
        /**
     * 
     * Returns a binary string in polar form (false -> 1, true -> -1)
     * 
     * @param   boolStr a boolean array representing the binary string
     * @return          an int array representing the polar form of the string
     */
    public static int[] bin2Pol(boolean[] boolStr) {

        int[] polStr = new int[boolStr.length];

        for(int i=0; i<boolStr.length; i++) {
            if(boolStr[i])
                polStr[i] = -1;
            else
                polStr[i] = 1;
        }

        return polStr;
    }
    
    /**
     * 
     * Returns a polar string in binary form (1 -> false, -1 -> true)
     * 
     * @param   polStr     a boolean array representing the binary string
     * @return             an int array representing the polar form of the string
     */
    public static boolean[] pol2Bin(int[] polStr) {

        boolean[] boolStr = new boolean[polStr.length];

        for(int i=0; i<polStr.length; i++) {
            
            if(polStr[i]==-1) {
            
                boolStr[i] = true;
                
            }
            
        }

        return boolStr;
        
    }
    
    /**
     * Converts a decimal number (BigInteger format) in a n-ary array of int.
     * 
     * @param   dNum    The BigInteger decimal number to convert
     * @param   length  The length of the array necessary to hold the n-ary
     *                  representation of dNum
     * @param   n       The radix for conversion
     * @return          An array of int holding the n-ary representation of dNum
     */
    public static int[] dec2Nary(BigInteger dNum, int length, int n) {
        
        int[] enNum = new int[length];
        BigInteger temp = dNum;
        BigInteger en = new BigInteger(Integer.toString(n));
        int i = 0;

        //Main loop: continue to divide temp by n until we reach zero, and
        //save the remainders in the n-ary array enNum
        while(temp.compareTo(BigInteger.ZERO) != 0) {

            BigInteger mod = temp.remainder(en);
            temp = temp.divide(en);
            //the array is filled in reverse order, since we are using MSBF
            enNum[enNum.length-i-1] = Integer.parseInt(mod.toString());

            i++;

        }

        return enNum;

    }
    
    /**
     * Converts a decimal number in int format in a n-ary array of int.
     * 
     * @param   dNum    The int decimal number to convert
     * @param   length  The length of the array necessary to hold the n-ary
     *                  representation of dNum
     * @param   n       The conversion radix
     * @return          An array of int holding the n-ary representation of dNum
     */
    public static int[] dec2Nary(int dNum, int length, int n) {
        
        int[] enNum = new int[length];
        int temp = dNum;
        int i = 0;

        //Main loop: continue to divide temp by n until we reach zero, and
        //save the remainders in the n-ary array enNum
        while(temp != 0) {

            int mod = temp % n;
            temp = temp / n;
            //the array is filled in reverse order, since we are using MSBF
            enNum[enNum.length-i-1] = mod;

            i++;

        }

        return enNum;

    }
    
    /**
     * Wrapper method to convert a BigInteger number in binary, represented as
     * a boolean array. The method first calls dec2Nary with radix 2 and then
     * transforms the resulting int array in a boolean array.
     * 
     * @param   dNum    The BigInteger decimal number to convert in binary
     * @param   length  The length of the array necessary to hold the binary
     *                  representation of dNum
     * @return          An array of boolean holding the binary representation of
     *                  dNum (0 -> false, 1 -> true)
     */
    public static boolean[] dec2Bin(BigInteger dNum, int length) {
        
        int[] binNum = dec2Nary(dNum, length, 2);
        boolean[] boolNum = int2Bool(binNum);
        
        return boolNum;
        
    }
    
    /**
     * Wrapper method to convert a int number in binary, represented as
     * a boolean array. The method first calls dec2Nary with radix 2 and then
     * transforms the resulting int array in a boolean array.
     * 
     * @param   dNum    The int number to convert in binary
     * @param   length  The length of the array necessary to hold the binary
     *                  representation of dNum
     * @return          An array of boolean holding the binary representation of
     *                  dNum (0 -> false, 1 -> true)
     */
    public static boolean[] dec2Bin(int dNum, int length) {
        
        int[] binNum = dec2Nary(dNum, length, 2);
        boolean[] boolNum = int2Bool(binNum);
        
        return boolNum;
        
    }
    
    /**
     * Wrapper method to convert a BigInteger number in hexadecimal, represented
     * as a String. The method first calls dec2Nary() with radix 16 and then
     * transforms the resulting int array in a String by calling nAry2Hex().
     * 
     * @param dNum      The BigInteger number to convert in hexadecimal
     * @param length    The length of the array necessary to hold the hexadecimal
     *                  representation of dNum
     * @return          An string holding the hexadecimal conversion of dNum
     */
    public static String dec2Hex(BigInteger dNum, int length) {
        
        int[] hexNum = dec2Nary(dNum, length, 16);
        String hexStr = nAry2Hex(hexNum);
        
        return hexStr;
        
    }
    
    /**
     * Wrapper method to convert an int number in hexadecimal, represented
     * as a String. The method first calls dec2Nary() with radix 16 and then
     * transforms the resulting int array in a String by calling nAry2Hex().
     * 
     * @param dNum      The int number to convert in hexadecimal
     * @param length    The length of the array necessary to hold the hexadecimal
     *                  representation of dNum
     * @return          An string holding the hexadecimal conversion of dNum
     */
    public static String dec2Hex(int dNum, int length) {
        
        int[] hexNum = dec2Nary(dNum, length, 16);
        String hexStr = nAry2Hex(hexNum);
        
        return hexStr;
        
    }
    
    /**
     * Converts a n-ary string in a decimal number (BigInteger version).
     * 
     * @param   enNum an array of int holding the n-ary representation of a number (MSBF order)
     * @param   n radix of the n-ary number
     * @return  a BigInteger representing the conversion of enNum as a decimal number
     */
    public static BigInteger nAry2DecBig(int[] enNum, int n) {
        
        BigInteger dNum = new BigInteger("0");
        BigInteger en = new BigInteger(Integer.toString(n));
        
        for(int i=0; i<enNum.length; i++) {

            BigInteger toAdd = en;
            toAdd = toAdd.pow(i);
            //The number is converted in reverse order, since it is represented in MSBF
            toAdd = toAdd.multiply(new BigInteger(Integer.toString(enNum[enNum.length-i-1])));
            dNum = dNum.add(toAdd);
             
        }

        return dNum;
        
    }
    
    /**
     * Converts a n-ary string in a decimal number (int version).
     * 
     * @param   enNum an array of int holding the n-ary representation of a number (MSBF order)
     * @param   n radix of the n-ary number
     * @return  an int representing the conversion of enNum as a decimal number
     */
    public static int nAry2DecInt(int[] enNum, int n) {
        
        int dNum = 0;
        
        for(int i=enNum.length-1; i>=0; i--) {
            
            //The number is converted in reverse order, since it is represented in MSBF
            dNum = dNum + enNum[enNum.length-i-1]* (int)Math.pow(n, i);
             
        }

        return dNum;
        
    }
    
    /**
     * Convert an hex number in its n-ary form (n=16), as an array of int.
     * 
     * @param hexnum    String representation of the hexadecimal number
     * @return          An array of int holding the 16-ary representation of hexnum.
     */
    public static int[] hex2nAry(String hexnum) {
        
        int[] nary = new int[hexnum.length()];
        String hexdigits = "0123456789ABCDEF";
        
        for(int i=0; i<hexnum.length(); i++) {
            
            char c = hexnum.charAt(i);
            nary[i] = hexdigits.indexOf(c);
            
        }
        
        return nary;
        
    }
    
    /**
     * Convert an array of int in base 16 in the corresponding hex number.
     * 
     * @param nary      An array of int holding the 16-ary representation of a hex number.
     * @return          A string corresponding to the hex number represented by nary
     */
    public static String nAry2Hex(int[] nary) {
        
        String hexnum = "";
        String hexdigits = "0123456789ABCDEF";
        
        for(int i=0; i<nary.length; i++) {
            
            hexnum += hexdigits.charAt(nary[i]);
            
        }
        
        return hexnum;
        
    }
    
    /**
     * Wrapper method to convert an hex number in String form to a BigInteger
     * decimal number.
     * 
     * @param hexnum    String representation of the hexadecimal number
     * @return          A BigInteger holding the decimal representation of hexnum
     */
    public static BigInteger hex2DecBig(String hexnum) {
        
        //Convert the string in n-ary int format, n=16
        int[] nary = hex2nAry(hexnum);
        
        //Convert the n-ary array in a BigInteger number
        BigInteger dNum = nAry2DecBig(nary, 16);
        
        return dNum;
        
    }
    
    /**
     * Wrapper method to convert an hex number in String form to an int
     * decimal number.
     * 
     * @param hexnum    String representation of the hexadecimal number
     * @return          An int holding the decimal representation of hexnum
     */
    public static int hex2DecInt(String hexnum) {
        
        //Convert the string in n-ary int format, n=16
        int[] nary = hex2nAry(hexnum);
        
        //Convert the n-ary array in an int number
        int dNum = nAry2DecInt(nary, 16);
        
        return dNum;
        
    }
    
    /**
     * Convert an hex string in a binary (boolean) array.
     * 
     * @param hexnum    String representation of the hexadecimal number
     * @return          An array of boolean holding the binary representation of hexnum.
     */
    public static boolean[] hex2Bin(String hexnum) {
        
        //The length of the boolean array is 4 times that of the hex string,
        //since each hex digit corresponds to 4 bits
        boolean[] boolNum = new boolean[hexnum.length()*4];
        int[] nary = hex2nAry(hexnum);
        
        //Convert each hex digit (in int form) in a group of 4 bits
        for(int i=0; i<nary.length; i++) {
            
            //Convert the single hex digit in binary form
            boolean[] bdigit = dec2Bin(nary[i], 4);
            
            //Copy the 4 bits in the corresponding portion of boolNum, which
            //starts at index i*4
            System.arraycopy(bdigit, 0, boolNum, i*4, bdigit.length);
            
        }
        
        
        return boolNum;
        
    }
    
    /**
     * Wrapper method to convert a binary number represented in a boolean array
     * in a decimal number (BigInteger format).
     * 
     * @param bNum      A boolean array holding the binary number to convert
     * @return          A BigInteger representing the binary number in decimal notation
     */
    public static BigInteger bin2DecBig(boolean[] bNum) {

        int[] nary = bool2Int(bNum);
        BigInteger dNum = nAry2DecBig(nary, 2);
        
        return dNum;
        
    }
    
    /**
     * Wrapper method to convert a binary number represented in a boolean array
     * in a decimal number (int format).
     * 
     * @param bNum      A boolean array holding the binary number to convert
     * @return          An int representing bNum in decimal notation
     */
    public static int bin2DecInt(boolean[] bNum) {

        int[] nary = bool2Int(bNum);
        int dNum = nAry2DecInt(nary, 2);
        
        return dNum;
        
    }
    
    /**
     * Wrapper method to convert a binary number represented in a boolean array
     * in a hexadecimal number (String format).
     * 
     * @param bNum      A boolean array holding the binary number to convert
     * @return          A string representing bNum in hexadecimal format
     */
    public static String bin2Hex(boolean[] bNum) {
        
        BigInteger dNum = bin2DecBig(bNum);
        int hexlen = (int)bNum.length/4;
        int[] hexnary = dec2Nary(dNum, hexlen, 16);
        String hexnum = nAry2Hex(hexnary);
        
        return hexnum;
        
    }
    
    /**
     * Converts a single boolean value in a 0 (false) or 1 (true).
     * 
     * @param   bval a boolean value.  
     * @return       1 if bval is true, 0 otherwise
     */
    public static int singleBool2Bin(boolean bval) {
        
        if(bval)
            return 1;
        else
            return 0;
        
    }

    /**
     * Converts a binary string represented as a boolean array in a
     * corresponding string of 0s and 1s.
     * 
     * @param   boolstr the binary string represented as a boolean array
     * @return          the binary string represented as string of 0s and 1s.
     */
    public static String bool2Bin(boolean[] boolstr) {
            
        String binstr = "";

        for(int i=0; i<boolstr.length; i++) {
                
            if(boolstr[i])
                binstr += "1";
            else
                binstr += "0";
            
        }

        return binstr;
            
    }

}
