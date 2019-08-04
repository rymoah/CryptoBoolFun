package boolfun;

/**
 * Utilities to compute various transforms (Walsh, autocorrelation, Moebius) of
 * Boolean functions.
 * 
 * @author Luca Mariot
 * 
 */

import lowlevelfunc.*;

public class BoolTransf {
    
    /**
     * Computes the Walsh Transform of a Boolean function using the Fast Walsh
     * Transform (FWT) algorithm, which requires O(NlogN) operations (N=2^n is
     * the length of the truth table). The method directly computes the spectrum
     * in the original vector, and it must be called with the initial parameters
     * (vector, 0, vector.length). The return value is the spectral radius of
     * the function (=maximum absolute value of the Walsh transform).
     * The reference for the algorithm is C. Carlet, "Cryptography and
     * Error-Correcting Codes", chapter 8 in Crama, Hammer, "Boolean Models and
     * Methods in Mathematics, Computer Science and Engineering", p. 272.
     * 
     * @param vector    An array of int representing the truth table of a
     *                  Boolean function in polar form.
     * @param start     The index of the truth table where to start computations.
     * @param length    Length of the portion of truth table where to perform
     *                  computations, starting from start
     * @return          The spectral radius of the function, computed as the
     *                  maximum absolute value of the Walsh transform
     */
    public static int calcFWT(int[] vector, int start, int length) {
        
        int half = length/2;
        
        //Main cycle: split vector in two parts (v0 e v1), 
        //update v0 as v0=v0+v1, and v1 as v1=v0-v1.
        for(int i=start; i<start+half; i++) {
            
            int temp = vector[i];
            vector[i] += vector[i+half];
            vector[i+half] = temp - vector[i+half];
            
        }
        
        //Recursive call on v0 and v1.
        if(half>1) {
            
            int val1 = calcFWT(vector,start,half);
            int val2 = calcFWT(vector,start+half,half);
            
            //At the end of the recursive calls, compare val1 and val2 to decide
            //what is the spectral radius in the portion of truth table included
            //between start and start+length
            if(val1 > val2) {
                    
                return val1;
                
            }
            else {
                
                return val2;
                
            }

        } else {
        
            //If we have reached half=1 (function of 2 variables),
            //return the highest coefficient in absolute value.
            if(Math.abs(vector[start]) > Math.abs(vector[start+half]))
                return Math.abs(vector[start]);
            else
                return Math.abs(vector[start+half]);           
            
        }
        
    }
    
    /**
     * Computes the inverse Walsh Transform using the Fast Walsh Transform (FWT)
     * algorithm, which requires O(NlogN) operations (N=2^n is the length of the
     * truth table). Starting from the Walsh transform vector of a Boolean
     * function, the method returns its truth table in polar form. The method
     * directly computes the truth table in the original vector, and
     * it must be called with the initial parameters (vector, 0, vector.length);
     * 
     * @param vector    an array of integers representing the Walsh transform of a
     *                  Boolean function.
     * @param start     the index of the Walsh transform where to start computations.
     * @param length    length of the portion of Walsh transform where to perform
     *                  computations, starting from start.
     * @return          The maximum absolute value in the output vector. This
     *                  can be used as the maximum autocorrelation coefficient
     *                  when computing the autocorrelation function via the
     *                  Wiener-Khintchine theorem (i.e., starting from the
     *                  (squared Walsh transform and applying the inverse Walsh
     *                  transform).
     * 
     */
    public static int calcInvFWT(int[] vector, int start, int length) {
        
        int half = length/2;
        
        //Main cycle: split vector in two parts (v0 e v1), 
        //update v0 as v0=(v0+v1)/2, and v1 as v1=(v0-v1)/2.
        //Division by 2 is a normalization factor.
        for(int i=start; i<start+half; i++) {
            int temp = vector[i];
            vector[i] = (int)((vector[i] + vector[i+half]) / 2);
            vector[i+half] = (int)((temp - vector[i+half]) / 2);
        }
        
        //Recursive call on v0 and v1.
        if(half>1) {
            
            int val1 = calcInvFWT(vector,start,half);
            int val2 = calcInvFWT(vector,start+half,half);
            
            //At the end of the recursive calls, compare val1 and val2 to decide
            //what is the spectral radius in the portion of truth table included
            //between start and start+length
            if(val1 > val2) {
                    
                    return val1;
                    
            }
            
            else {
                
                return val2;
                
            }

        } else {
        
            //If we have reached half=1 (function of 2 variables),
            //return the highest coefficient in absolute value
            
            //If we are checking the null position, return the index in the
            //first position. This is because this method is used to calculate
            //the autocorrelation function, whose value ACmax must be computed
            //only on the non-zero vectors.
            if(start == 0) {
                return Math.abs(vector[1]);
            } else {
                
                if(Math.abs(vector[start]) > Math.abs(vector[start+half])) {
                    return Math.abs(vector[start]);
                }
                else {
                    return Math.abs(vector[start+half]);           
                }
                
            }
        }
        
    }

    
    /**
     * Computes the autocorrelation function of a boolean function, using the
     * Wiener-Khintchine theorem. The general computation flow is the following:
     * 
     * 1) Given the truth table of f, compute the Fast Walsh Transform (FWT) of f
     * 2) Compute the square of the Walsh transform of f
     * 3) Apply the Inverse Walsh Transform to the squared Walsh transform of f.
     *    The result is the autocorrelation function of f.
     * 
     * If a boolean flag is set, this method performs all three steps above,
     * and assumes that the input vector holds the polar form of the truth table
     * of the function. Otherwise, only step 2 and 3 are performed, and it is
     * assumed that the input vector contains the Walsh transform of the
     * function. This flag can be used to save computations if the Walsh
     * transform has already been computed in advance.
     * 
     * Further, the method directly computes the autocorrelation function in the
     * original input vector, and it returns the maximum autocorrelation coefficient.
     * 
     * @param vector    An array of int representing either the polar form of
     *                  the truth table of the Boolean function or its Walsh
     *                  Transform, depending on the mode flag
     * @param mode      a Boolean flag specifying whether vector is the polar
     *                  truth table of the function (true) or the Walsh spectrum
     *                  (false), used to avoid unnecessary computations.
     * @return          The maximum autocorrelation coefficient of the function.
     */
    public static int calcAC(int[] vector, boolean mode) {
        
        //(Step 1): Compute the Walsh spectrum of the function, if vector
        //represents the truth table of the function.
        if(mode) {
            
            calcFWT(vector, 0, vector.length);
            
        }
        
        //Step 2: Square the Walsh spectrum
        for(int i=0; i<vector.length; i++) {
            vector[i] *= vector[i];
        }
        
        //Step 3: Compute the inverse WT of the squared spectrum
        //and return the maximum autocorrelation coefficient
        int acmax = calcInvFWT(vector, 0, vector.length);
        return acmax;
        
    }
    
    /**
     * Finds the maximum absolute values of a vector in the positions with
     * specified Hamming weights. Can be used to determine the correlation
     * immunity order t of a Boolean function (by giving in input the Walsh
     * spectrum) or its propagation criterion PC(l) (by giving in input the
     * autocorrelation function).
     * 
     * @param coeffs    An array of coefficients (it can be the Walsh spectrum
     *                  or the autocorrelation function of a Boolean function).
     * @param indices   An array containing arrays of indices whose binary 
     *                  representations have a specified Hamming weight (for
     *                  example, indices[i] contains all indices of Hamming
     *                  weight i). The starting weight is 1, so indices[0]
     *                  must contain the indices of weight 1.
     * @return          An array containing the maximum absolute values in coeffs
     *                  at the positions specified by indices.
     */
    public static int[] calcDevs(int[] coeffs, int[][] indices) {
        
        int[] devs = new int[indices.length];
        
        //Cycle through Hamming weights from 1 to indices.length
        for(int i=0; i<indices.length; i++) {
            
            //Cycle through inputs with Hamming weight i
            for(int j=0; j<indices[i].length; j++) {
                
                int absval = Math.abs(coeffs[indices[i][j]]);
                
                if(absval > devs[i]) {
                    devs[i] = absval;
                }
                
            }
            
            //Deviations of order i must take into account also deviations
            //of lower orders.
            for(int k=0; k<i; k++) {
                
                if(devs[k] > devs[i]) {
                    
                    devs[i] = devs[k];
                    
                }
                
            }
            
        }
        
        return devs;
        
    }
    
    /**
     * Computes the Moebius Transform of a Boolean function using the Fast
     * Moebius Transform (FMT) algorithm, which requires O(NlogN) operations
     * (N=2^n is the length of the truth table). The method directly computes
     * the spectrum in the original vector, and it must be called with the
     * initial parameters (vector, 0, vector.length). The return value of the
     * method is the algebraic degree of the function.
     * 
     * The reference for the algorithm is Carlet, "Cryptography and
     * Error-Correcting Codes", chapter 8 in Crama, Hammer, "Boolean Models and
     * Methods in Mathematics, Computer Science and Engineering", p. 263.
     * 
     * NOTICE: the Moebius transform is an involution, meaning that this method
     * can also be used to recover the truth table of a function starting from
     * its ANF vector.
     * 
     * @param vector    An array boolean representing the boolean function.
     * @param start     The index of the truth table where to start computations.
     * @param length    The length of the subvector where to perform computations
     *                  starting from start.
     * @return          The algebraic degree
     */
    public static int calcFMT(boolean[] vector, int start, int length) {
        
        int half = length/2;
        
        //Main cycle: split vector in two parts (v0 e v1),
        //update v1 as v1=v0 XOR v1.
        for(int i=start; i<start+half; i++) {
            vector[i+half] = vector[i]^vector[i+half];
        }
        
        //Recursive call on v0 and v1.
        if(half>1) {
            
            int val1 = calcFMT(vector,start,half);
            int val2 = calcFMT(vector,start+half,half);
            
            //At the end of the recursive calls, compare val1 and val2 to decide
            //what is the algebraic degree in the portion of ANF included
            //between start and start+length
            if(val1 > val2) {
                    
                    return val1;
                    
            }
            else {
                
                return val2;
                
            }

        } else {
        
            //If we have reached half=1 (function of 2 variables),
            //return the Hamming weight of the largest input with nonzero
            //coefficient in the current subvector. This is the algebraic degree
            //of the restriction of the function of 2 variables.
                
            //If both coefficient are zero, then the degree of the subfunction
            //is zero.
            if((vector[start] == false) && (vector[start+half] == false)) {
                
                return 0;
                
            } else {

                //Compute the length of the input vectors.
                int inplen = (int)(Math.log(vector.length)/Math.log(2));

                //If the coefficient of the higher vector is null,
                //then return the Hamming weight of the lower vector.
                if(vector[start+half] == false) {

                    boolean[] input = NumTools.dec2Bin(start, inplen);
                    int subdeg = BinTools.hammingWeight(input);
                    
                    return subdeg;

                } else {

                    //In all other cases, return the Hamming weight of the
                    //higher vector.
                    boolean[] input = NumTools.dec2Bin(start+half, inplen);
                    int subdeg = BinTools.hammingWeight(input);
                    
                    return subdeg;

                }
            }
            
        }
        
    }
    
    /**
     * Computes the algebraic expression of the ANF of a Boolean function,
     * represented as a multivariate polynomial.
     * 
     * @param anfcoeffs A boolean array holding the ANF coefficients of the function
     * @param nvar      Number of variables of the function
     * @return          A string representing the algebraic expression of the
     *                  ANF of the function, as a multivariate polynomial.
     */
    public static String calcANFExpr(boolean[] anfcoeffs, int nvar) {
        
        //Find the last nonzero coefficient in the ANF
        int last = 0;
        int k = anfcoeffs.length-1;
        
        while((last == 0) && (k>=0)) {
            
            if(anfcoeffs[k])
                last = k;
            
            k--;
            
        }
        
        //Initialize the ANF string
        String anfexpr = "f(";

        for(int i=0; i<nvar; i++) {
            anfexpr += "x"+(i+1);
            if(i<nvar-1) {
                anfexpr += ",";
            }
        }
        anfexpr += ") = ";
        
        if(anfcoeffs[0]) {
            anfexpr += "1 + ";
        }
        
        for(int i=1; i<=last; i++) {
            
            if(anfcoeffs[i]) {
                
                //Print the i-th variation of variables
                boolean[] input = NumTools.dec2Bin(i,nvar);
                
                for(int j=0; j<nvar; j++) {
                    
                    if(input[j]) {
                        
                        anfexpr += "x"+(j+1);
                        
                    }
                    
                }
                
                if(i<last) {
                    anfexpr += " + ";
                }
                
                
            }
            
        }
        
        return anfexpr;
        
    }
    
    public static void main(String[] args) {
        
        int[] polfunc = {1, -1, -1, -1, -1, 1, 1, 1};
        calcFWT(polfunc, 0, polfunc.length);
        
        System.out.println("Walsh spectrum:");
        for(int i=0; i<polfunc.length; i++) {
            System.out.println("F("+i+") = "+polfunc[i]);
        }
        
        calcInvFWT(polfunc, 0, polfunc.length);
        
        System.out.println("Original function from IWT:");
        for(int i=0; i<polfunc.length; i++) {
            System.out.println("F("+i+") = "+polfunc[i]);
        }
        
        
        
    }
    
}
