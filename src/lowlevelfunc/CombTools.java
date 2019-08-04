package lowlevelfunc;

/**
 * Utilities to generate combinations of objects (particularly bitstrings with
 * a specified Hamming weight) and compute their cardinalities. The bitstrings
 * are sorted in MSBF (Most Significant Bit First) order.
 * 
 * @author  Luca Mariot
 * 
 */

public class CombTools {
    
    /**
     * Computes the factorial of an integer number.
     * 
     * @param num   an integer number
     * @return      the factorial of num
     */
    public static int factorial(int num) {
        
        int fact = 1;
        
        if((num==0) || (num==1)) {            
            return fact;   
        }
        
        for(int i=2; i<=num; i++) {
            fact *= i;
        }
        
        return fact;
        
    }
    
    /**
     * Computes the binomial coefficient (n,k).
     * 
     * @param n         the size of the set from which combinations are drawn.
     * @param k         the size of the combinations.
     * @return          the binomial coefficient (n,k)
     */
    public static int binCoeff(int n, int k) {
        
        long numerator = 1;
        
        for(int i=n-k+1; i<=n; i++) {
            numerator *= i;
        }
        
        long denominator = factorial(k);
        int bCoeff = (int)(numerator/denominator);
        
        return bCoeff;
        
    }
    
    /**
     * Generates all (s+t)-bitstrings with Hamming weight t, in decimal
     * notation. The algorithm is described in Knuth, "The Art of Computer
     * Programming, pre-Fascicle 4A" (Algorithm L, p. 4).
     * 
     * @param s         number of 0s in the bitstrings
     * @param t         number of 1s in the bitstrings
     * @return          array of integers representing the bitstrings of length
     *                  (s+t) and Hamming weight t
     */
    public static int[] genBinCombs(int s, int t) {

        int size = binCoeff(s+t, t);
        int[] combset = new int[size];
        
        int index = 0; //index for the set combs.
        
        //Initialisation
        int[] comb = new int[t+2]; //the two additional cells are sentinels.
        for(int j=0; j<t; j++) {
            comb[j] = j;
        }
        comb[t] = s+t;
        comb[t+1] = 0;
        
        int j = 0;
        
        while(j<t) {
            
            boolean[] conf = new boolean[s+t];
            
            for(int k=0; k<t; k++) {
                conf[comb[k]] = true;
            }
            
            //Convert the combination in decimal notation and
            //copy it in the final set.
            int deccomb = NumTools.bin2DecInt(conf);
            combset[index] = deccomb;
            index++;            
            
            j=0;
            while((comb[j]+1)==comb[j+1]) {
                comb[j] = j;
                j++;
            }
            
            if(j<t) {
                comb[j]++;
            }
            
        }
        
        return combset;
      
    }
    
    /**
     * Generates all the (s+t)-bit strings with Hamming weight t, in binary
     * notation. The algorithm is described in Knuth, "The Art of Computer
     * Programming, pre-Fascicle 4A" (Algorithm L, p. 4).
     * 
     * @param s         number of 0s in the bitstrings
     * @param t         number of 1s in the bitstrings
     * @return          an array of boolean arrays, where each array is composed
     *                  of s false values and t true values
     */
    public static boolean[][] genBinCombsBin(int s, int t) {

        int size = binCoeff(s+t, t);
        boolean[][] combset = new boolean[size][];
        
        int index = 0; //index for the set combs.
        
        //Initialisation
        int[] comb = new int[t+2]; //the two additional cells are sentinels.
        for(int j=0; j<t; j++) {
            comb[j] = j;
        }
        comb[t] = s+t;
        comb[t+1] = 0;
        
        int j = 0;
        
        while(j<t) {
            
            boolean[] conf = new boolean[s+t];
            
            for(int k=0; k<t; k++) {
                conf[comb[k]] = true;
            }
            
            //Copy the combination in the final set.
            //int deccomb = BinTools.bin2Dec(conf);
            combset[index] = conf;
            index++;            
            
            j=0;
            while((comb[j]+1)==comb[j+1]) {
                comb[j] = j;
                j++;
            }
            
            if(j<t) {
                comb[j]++;
            }
            
        }
        
        return combset;
      
    }
    
    /**
     * Creates a set of indices ordered by increasing Hamming weights. Given
     * the maximum weight as an input parameter, the method creates an int
     * matrix, where for all i between 0 (included) and maxweight (excluded) the
     * i-th row is an array holding the int indices whose binary representation
     * have Hamming weight i+1.
     * 
     * Notice that, in the order above, weight 0 is not considered (so indices[0]
     * will hold the indices of Hamming weight 1).
     * 
     * This method can be used in the computation of cryptographic properties
     * such as correlation immunity and propagation criteria, since they are
     * defined by subsets of binary vectors up to a certain Hamming weight.
     * 
     * 
     * @param maxweight     Maximum Hamming weight to be reached in the creation
     *                      of the indices
     * @return              An int matrix where each row i in [0..maxweight-1] is
     *                      an int array holding the integer representations of
     *                      the binary vectors of Hamming weight i+1
     * 
     */
    public static int[][] createIndices(int maxweight) {
        
        int[][] indices = new int[maxweight][];
        for(int i=0; i<maxweight; i++) {
            
            indices[i] = CombTools.genBinCombs(maxweight-i-1, i+1);
            
        }
        
        return indices;
        
    }
    
}
