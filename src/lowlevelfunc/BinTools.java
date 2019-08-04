package lowlevelfunc;

/**
 * Utilities and tools to manipulate binary strings, represented as boolean
 * arrays.
 * 
 * @author Luca Mariot
 */

public class BinTools {
    
    /**
    * Compute the bitwise XOR between two boolean arrays of equal length.
    * 
    * @param seq1   a boolean array (first operand)
    * @param seq2   a boolean array (second operand)
    * @return       the bitwise XOR of seq1 and seq2
    */
   public static boolean[] xorBits(boolean[] seq1, boolean[] seq2) {

       boolean[] xoredSeq = new boolean[seq1.length];

       for(int i=0; i<seq1.length; i++) {
           xoredSeq[i] = seq1[i] ^ seq2[i];
       }

       return xoredSeq;
       
   }
   
   /**
     * Computes the Hamming weight of a boolean array (=number of positions
     * equal to true).
     * 
     * @param binStr    The boolean vector whose Hamming weight has to be
     *                  computed.
     * @return          The Hamming weight of binStr.
     */
    public static int hammingWeight(boolean[] binStr) {
        
        int weight = 0;
        
        for(int i=0; i<binStr.length; i++) {
            
            if(binStr[i])
                weight++;
            
        }
        
        return weight;
        
    }
    
    /**
     * Computes the Hamming distance between two boolean arrays of equal length
     * (= number of positions in which the two arrays differ).
     * 
     * @param binStr1   First boolean array
     * @param binStr2   Second boolean array
     * @return          The Hamming distance between binStr1 and BinStr2
     */
    public static int hammingDist(boolean[] binStr1, boolean[] binStr2) {
        
        //Compute the bitwise XOR of the two strings
        boolean[] xor = xorBits(binStr1, binStr2);
        
        //Compute the Hamming weight of the XOR, which is equal to the Hamming
        //distance of the two strings
        int hdist = hammingWeight(xor);
        
        return hdist;
        
    }

   /**
    * Verifies whether a boolean array is balanced (that is, it contains an
    * equal number of true (1) and false (0).
    * 
    * @param   binStr a byte array
    * @return  true if binStr is balanced, false if it is not.
    */
   public static boolean isBalanced(boolean[] binStr) {

        int hwt = hammingWeight(binStr);

        if(hwt == binStr.length/2) {

            return true;

        } else {

            return false;

        }
    }

    /**
     * Computes the 1-complement of a boolean array
     * 
     * 
     * @param   binStr a boolean array
     * @return         the 1-complement of binStr.
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
     * @return          the reverse of binStr
     */
    public static boolean[] reverse(boolean[] binStr) {
            
        boolean[] rev = new boolean[binStr.length];

        for(int i=0; i<rev.length; i++) {

            rev[i] = binStr[binStr.length-1-i];

        }

        return rev;

    }
    
    /**
     * Compute the scalar product between two boolean vectors of equal length.
     * If the length of the vectors is n, the scalar product is computed through
     * the following formula:
     * 
     * vect1.vect2 = XOR_{i=1}^{n} (vect1[i] AND vect2[i])
     * 
     * @param vect1     First boolean vector
     * @param vect2     Second boolean vector
     * @return          The scalar product between vect1 and vect2, which is
     *                  equal to the XOR of the bitwise ANDs between vect1 and
     *                  vect2
     */
    public static boolean scalarProduct(boolean[] vect1, boolean[] vect2) {
        
        boolean prod = false;
        
        for(int i=0; i<vect2.length; i++) {
            
            prod ^= vect1[i] && vect2[i];
            
        }
        
        return prod;
        
    }
    
}
