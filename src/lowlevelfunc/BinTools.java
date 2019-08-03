package lowlevelfunc;

/**
 *
 * Utilities to convert and manipulate binary strings and numbers. The
 * conversion methods come in two versions, one using int and the other
 * using BigInteger (to convert bigger numbers). The order used in the
 * bitstrings is LSBF (Least Significant Bit First).
 * 
 * @author  Luca Mariot
 * @version 1.0
 */

import java.math.BigInteger;
import java.util.Vector;

public class BinTools {
    
    /**
     * 
     * Returns a binary string in polar form (0 -> 1, 1 -> -1)
     * 
     * @param   vect    a boolean array representing the binary string
     * @return  toRet   an int array representing the polar form of the string                  string
     */
    public static int[] bin2Pol(boolean[] vect) {

        int[] toRet = new int[vect.length];

        for(int i=0; i<vect.length; i++) {
            if(vect[i])
                toRet[i] = -1;
            else
                toRet[i] = 1;
        }

        return toRet;
    }
    
    /**
     * 
     * Returns a polar string in binary form (1 -> 0, -1 -> 1)
     * 
     * @param   vect    a boolean array representing the binary string
     * @return  toRet   an int array representing the polar form of the string                  string
     */
    public static boolean[] pol2Bin(int[] vect) {

        boolean[] toRet = new boolean[vect.length];

        for(int i=0; i<vect.length; i++) {
            if(vect[i]==1)
                toRet[i] = false;
            else
                toRet[i] = true;
        }

        return toRet;
    }   
    

    /**
     * Converts a binary string in a decimal number (BigInteger version).
     * 
     * @param   bNum a binary string (LSBF order)
     * @return  dNum the conversion of bNum as a decimal number
     */
    public static BigInteger bin2DecBig(boolean[] bNum) {
        
        BigInteger dNum = new BigInteger("0");
        
        for(int i=0; i<bNum.length; i++) {
            
             if(bNum[i]) {
                 BigInteger toAdd = new BigInteger("2");
                 toAdd = toAdd.pow(i);
                 dNum = dNum.add(toAdd);
             }
             
        }

        return dNum;
        
    }

    /**
     * Converts a binary string in a decimal number(int version).
     * 
     * @param   bNum a binary string (LSBF order)
     * @return  dNum the conversion of bNum as a decimal number
     */
    public static int bin2Dec(boolean[] bNum) {
        
        int dNum = 0;
        
        for(int i=0; i<bNum.length; i++) {
            
             if(bNum[i]) {
                 dNum += Math.pow(2, i);
             }
             
        }

        return dNum;

    }

    /**
     * Converts a decimal number in a binary string (BigInteger version).
     * 
     * @param   dNum    a decimal number
     * @param   length  the length of the binary string necessary to hold dNum
     * @return  bNum    the conversion of dNum as a binary string
     */
    public static boolean[] dec2Bin(BigInteger dNum, int length) {
        
        boolean[] bNum = new boolean[length];
        BigInteger temp = dNum;
        BigInteger two = new BigInteger("2");
        int i = 0;

        while(temp.compareTo(BigInteger.ZERO) != 0) {

            BigInteger mod = temp.remainder(two);
            temp = temp.divide(two);

            if(mod.compareTo(BigInteger.ONE) == 0) {
                bNum[i] = true;
            }

            i++;

        }

        return bNum;

    }

    /**
     * Converts a decimal number in a binary string (int version).
     * 
     * @param   dNum    a decimal number
     * @param   length  the length of the binary string necessary to hold dNum
     * @return  bNum    the conversion of dNum as a binary string
     */
    public static boolean[] dec2BinMod(int dNum, int length) {
        
        boolean[] bNum = new boolean[length];
        int temp = dNum;
        int i = 0;

        while(temp!=0) {

            int mod = temp%2;
            temp = temp/2;

            if(mod==1) {
                bNum[i] = true;
            }

            i++;
        }

        return bNum;

    }
    
    /**
     * Converts a decimal number in a n-ary string (BigInteger version).
     * 
     * @param   dNum    a decimal number
     * @param   length  the length of the binary string necessary to hold dNum
     * @return  bNum    the conversion of dNum as a binary string
     */
    public static int[] dec2Nary(BigInteger dNum, int length, int n) {
        
        int[] enNum = new int[length];
        BigInteger temp = dNum;
        BigInteger en = new BigInteger(Integer.toString(n));
        int i = 0;

        while(temp.compareTo(BigInteger.ZERO) != 0) {

            BigInteger mod = temp.remainder(en);
            temp = temp.divide(en);

            //System.out.println(mod);
            enNum[i] = Integer.parseInt(mod.toString());

            i++;

        }

        return enNum;

    }
    
    /**
     * Converts a decimal number in a n-ary string (int version).
     * 
     * @param   dNum    a decimal number
     * @param   length  the length of the binary string necessary to hold dNum
     * @return  bNum    the conversion of dNum as a binary string
     */
    public static int[] dec2NaryInt(int dNum, int length, int n) {
        
        int[] enNum = new int[length];
        int temp = dNum;
        int i = 0;

        while(temp != 0) {

            int mod = temp % n;
            temp = temp / n;

            //System.out.println(mod);
            enNum[i] = mod;

            i++;

        }

        return enNum;

    }
    
    /**
     * Converts a n-ary string in a decimal number (BigInteger version).
     * 
     * @param   enNum an n-ary string (LSBF order)
     * @param   n radix
     * @return  dNum the conversion of bNum as a decimal number
     */
    public static BigInteger nary2DecBig(int[] enNum, int n) {
        
        BigInteger dNum = new BigInteger("0");
        BigInteger en = new BigInteger(Integer.toString(n));
        
        for(int i=0; i<enNum.length; i++) {

            BigInteger toAdd = en;
            toAdd = toAdd.pow(i);
            toAdd = toAdd.multiply(new BigInteger(Integer.toString(enNum[i])));
            dNum = dNum.add(toAdd);
             
        }

        return dNum;
        
    }
    
    /**
     * Converts a n-ary string in a decimal number (int version).
     * 
     * @param   enNum an n-ary string (LSBF order)
     * @param   n radix
     * @return  dNum the conversion of bNum as a decimal number
     */
    public static int nary2DecInt(int[] enNum, int n) {
        
        int dNum = 0;
        
        for(int i=0; i<enNum.length; i++) {

            dNum = dNum + enNum[i]* (int)Math.pow(n, i);
             
        }

        return dNum;
        
    }

    /**
     * Converts a binary string represented as a boolean array in a
     * corresponding string of 0s and 1s.
     * 
     * @param   boolstr the binary string represented as a boolean array
     * @return  binstr  the binary string represented as string of 0s and 1s.
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

    /**
     * Converts a single boolean value in a 0 (false) or 1 (true).
     * 
     * @param   bval a boolean value.  
     * @return  1 if bval=true, 0 otherwise
     */
    public static int singleBool2Bin(boolean bval) {
        
        if(bval)
            return 1;
        else
            return 0;
        
    }

   /**
    * Converts a byte in a boolean array of length 8. The conversion is done
    * by first transforming the byte in an unsigned int (since Java does not
    * support unsigned bytes).
    * 
    * @param   byteval a byte value.
    * @return  a boolean array representing byteval (Big Endian order)
    */
   public static boolean[] byte2Bin(byte byteval) {

       int bInt = byteval & 0xFF;
       return dec2BinMod(bInt, 8);

   }
   
   public static boolean[] char2Bin(char charval) {

       int bInt = (int)charval;
       return dec2BinMod(bInt, 8);

   }

   /**
    * Converts a byte array in a boolean array.
    * 
    * @param   byteStr a byte array
    * @return  bitStr  the conversion of byteStr as a boolean array
    */
   public static boolean[] byteStr2bitStr(byte[] byteStr) {

       boolean[] bitStr = new boolean[8*byteStr.length];
       int j=0;
       for(int i=0; i<byteStr.length; i++) {
           
           boolean[] bits = byte2Bin(byteStr[i]);
           System.arraycopy(bits, 0, bitStr, j, bits.length);
           j += 8;
           
       }

       return bitStr;
       
   }

   /**
    * Converts a boolean array in a byte value.
    * 
    * @param   bin a boolean array of length 8 (Big Endian order)
    * @return  a byte value representing bin
    */
   public static byte bin2Byte(boolean[] bin) {
       
       assert(bin.length==8);
       return (byte)bin2Dec(bin);
       
   }

   /**
    * Converts a boolean array (of length a multiple of 8) in a byte array.
    * 
    * @param   bitStr  a boolean array
    * @return  byteStr  the conversion of bitStr as a byte array
    */
   public static byte[] bitStr2byteStr(boolean[] bitStr) {
       
       assert(bitStr.length % 8 == 0);

       boolean[] buff = new boolean[8];
       byte[] byteStr = new byte[bitStr.length/8];

       for(int i=0; i<bitStr.length; i+=8) {
           
           System.arraycopy(bitStr, i, buff, 0, buff.length);
           byteStr[i/8] = bin2Byte(buff);
           
       }

       return byteStr;
       
   }

   /**
    * Converts a byte string in a hexadecimal string.
    * 
    * @param   byteStr  a byte array
    * @return  hexCode  the conversion of byteStr as a hexadecimal string
    */
   public static String byte2Hex(byte[] byteStr) {
       
       String hexCode = "";

       for(int i=0; i<byteStr.length; i++) {
           
           //padding
           if(byteStr[i]<16 && byteStr[i]>=0) {
               hexCode += "0";
           }
           
           hexCode += Integer.toHexString(byteStr[i] & 0xFF);
           
       }

       return hexCode;
       
   }

   /**
    * Converts a long in a byte array.
    * 
    * @param longNum    a long number 
    * @return byteStr   a byte array representing the conversion of longNum
    */
   public static byte[] long2Bytes(long longNum) {

       byte[] byteStr = new byte[8];

       for(int i= 0; i < 8; i++) {
            byteStr[7 - i] = (byte)(longNum >>> (i * 8));
       }

    return byteStr;

   }
   
   public static int[] bin2Int(boolean[] binstring) {
       
       int[] toRet = new int[binstring.length];
       
       for(int i=0; i<binstring.length; i++) {
           
           if(binstring[i]) {
               toRet[i] = 1;
           } else {
               toRet[i] = 0;
           }
           
       }
       
       return toRet;
       
   }

   /**
    * Compute the bitwise XOR between two boolean arrays of equal length.
    * 
    * @param seq1 a boolean array (first operand)
    * @param seq2 a boolean array (second operand)
    * @return xoredSeq the bitwise XOR of seq1 and seq2
    */
   public static boolean[] xorBits(boolean[] seq1, boolean[] seq2) {

       assert(seq1.length == seq2.length);
       boolean[] xoredSeq = new boolean[seq1.length];

       for(int i=0; i<seq1.length; i++) {
           xoredSeq[i] = seq1[i] ^ seq2[i];
       }

       return xoredSeq;
       
   }
   
   public static Vector<Integer> findDifPos(boolean[] table1,
           boolean[] table2) {
       
       Vector<Integer> difpos = new Vector<Integer>();
       
       for(int i=0; i<table1.length; i++) {
           
           if(table1[i] != table2[i]) {
                difpos.add(i);
           }
           
       }
       
       difpos.trimToSize();
       return difpos;
       
   }

   /**
    * Verifies whether a boolean array is balanced (that is, it contains an
    * equal number of true (1) and false (0).
    * 
    * @param   bVect a byte array
    * @return  true if bVect is balanced, false if it is not.
    */
   public static boolean isBalanced(boolean[] bVect) {


        if(bVect.length % 2 != 0) {
            return false;
        }

        int ones = 0;

        for(int i=0; i<bVect.length; i++) {

            if(bVect[i]) {
                ones++;
            }

        }

        if(ones == bVect.length/2) {

            return true;

        } else {

            return false;

        }
    }
   
    public static int compUnbal(boolean[] func) {
        
        int unb = 0;
        
        for(int i=0; i<func.length; i++) {
            
            if(func[i]) {
                unb++;
            }
            
        }
        
        return unb;
        
    }

    /**
     * Computes the 1-complement of a boolean array
     * 
     * 
     * @param   binStr a boolean array
     * @return  compl  the 1-complement of binStr.
     */
    public static boolean[] complement(boolean[] binStr) {
        boolean[] compl = new boolean[binStr.length];

        for(int i=0; i<compl.length; i++) {
            compl[i] = !binStr[i];
        }

        return compl;
    }   
    
    /**
     * Inverts the order of a boolean array.
     * 
     * 
     * @param   binStr  a boolean array
     * @return  rev     the reverse of binStr
     */
    public static boolean[] reverse(boolean[] binStr) {
            
        boolean[] rev = new boolean[binStr.length];

        for(int i=0; i<rev.length; i++) {

            rev[i] = binStr[binStr.length-1-i];

        }

        return rev;

    }
    
    /**
     * Returns the mirror image of a boolean function, represented as a boolean
     * array.
     * 
     * 
     * @param   binStr   a boolean array representing the boolean function (LSBF)
     * @param   inlength the length of the input (number of variables).
     * @return  revin    the mirror image of binStr
     */
    public static boolean[] revInput(boolean[] binStr, int inlength) {
            
        boolean[] revin = new boolean[binStr.length];

        for(int i=0; i<revin.length; i++) {

            boolean[] index = dec2BinMod(i, inlength);
            boolean[] revindex = reverse(index);
            int rindex = bin2Dec(revindex);               
            revin[rindex] = binStr[i];

        }

        return revin;

    }
    
    /**
     * Computes the Hamming weight of a boolean vector.
     * 
     * @boolean[] binStr    The boolean vector whose Hamming weight has to be
     *                      computed.
     * @return              The Hamming weight of binStr.
     */
    public static int hwt(boolean[] binStr) {
        
        int weight = 0;
        
        for(int i=0; i<binStr.length; i++) {
            
            if(binStr[i])
                weight++;
            
        }
        
        return weight;
        
    }
    
    public static String double2BinString(double dnum) {
        
        long lnum = Double.doubleToLongBits(dnum);
        String binstring = Long.toBinaryString(lnum);
        
        return binstring;
        
    }
    
    public static double binString2Double(String binstring) {
        
        long lnum = Long.parseLong(binstring, 2);
        double dnum = Double.longBitsToDouble(lnum);
        
        return dnum;
        
    }
    
    public static boolean[][] compRestr(boolean[] func, int ind, int nvar) {
        
        int n = nvar;
        int lim1 = (int)Math.pow(2,n-ind);
        int lim2 = (int)Math.pow(2,ind-1);
        int lim3 = (int)Math.pow(2,ind);
        Vector<Boolean> restr0 = new Vector<Boolean>();
        Vector<Boolean> restr1 = new Vector<Boolean>();
        
        for(int i=0; i<lim1; i++) {
            
            for(int j=0; j<lim2; j++) {
                
                restr0.add(func[(lim3*i)+j]);
                restr1.add(func[(lim3*i)+lim2+j]);
                
            }
            
        }
        
        restr0.trimToSize();
        restr1.trimToSize();
        
        boolean[][] toRet = new boolean[2][func.length/2];
        
        for(int i=0; i<func.length/2; i++) {
            
            toRet[0][i] = restr0.elementAt(i);
            toRet[1][i] = restr1.elementAt(i);
            
        }
        
        return toRet;
        
    }
    
    /** Compute the scalar product between two boolean vectors-
     * 
     * @param vect1
     * @param vect2
     * @return 
     */
    public static boolean scalarProduct(boolean[] vect1, boolean[] vect2) {
        
        boolean prod = false;
        
        for(int i=0; i<vect2.length; i++) {
            
            prod ^= vect1[i] && vect2[i];
            
        }
        
        return prod;
        
    }
    
    /**
     * Compute the multiplication between a boolean square matrix and a boolean vector
     * 
     * @param matrix
     * @param vector
     * @return 
     */
    public static boolean[] matVectMult(boolean[][] matrix, boolean[] vector) {
        
        boolean[] result = new boolean[vector.length];
        
        for(int i=0; i<vector.length; i++) {
            
            result[i] = scalarProduct(matrix[i], vector);

        }
        
        return result;
        
    }
    
    public static boolean[] String2BoolStr(String binstr) {
        
        boolean[] toRet = new boolean[binstr.length()];
        
        for(int i=0; i<binstr.length(); i++) {
            
            if(binstr.charAt(i)=='1') {
                toRet[i] = true;
            } else {
                toRet[i] = false;
            }
        }
        
        return toRet;
        
    }
    
    public static boolean[] cyclicShift(boolean[] vector) {
        
        boolean[] shifted = new boolean[vector.length];
        
        for(int i=1; i<vector.length; i++) {
            
            shifted[i-1] = vector[i];
            
        }
        
        shifted[shifted.length-1] = vector[0];
        
        return shifted;
        
    }
    
    public static void nCyclicShift(boolean[] vector, int n) {
        
        for(int i=0; i<n; i++) {
            
            vector = cyclicShift(vector);
        }
    }
    
    public static void main(String[] args) {
        
        double dnum = Double.parseDouble(args[0]);
        
        String bnum = double2BinString(dnum);
        System.out.println("Bitstring: "+bnum);
        double ndnum = binString2Double(bnum);
        System.out.println("Reconverted double: "+ndnum);
        
    }

}
