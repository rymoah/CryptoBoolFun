package boolfun;

/**
 * Methods to compute and print the properties (attributes) of a Boolean function
 * represented as a BooleanFunction instance. In all methods below, it is assumed
 * that the BooleanFunction instance has already been initialized with one of
 * its three constructors (hence, the decimal code, truth table and hex code
 * attributes of the BooleanFunction instance have already been computed).
 * 
 * @author Luca Mariot
 * 
 */

import lowlevelfunc.*;

public class CryptoProperties {
    
    /**
     * Computes the Hamming weight of the truth table representation of a Boolean
     * function of n variables, and determines if it is balanced (that is, the
     * Hamming weight is 2^(n-1).
     * 
     * @param boolfun   A BooleanFunction object already initialized with one of
     *                  the three constructors provided, representing a Boolean
     *                  function
     * 
     */
    public static void computeWeightProp(BooleanFunction boolfun) {
        
        //Compute weight
        int weight = BinTools.hammingWeight(boolfun.getTtable());
        boolfun.setWeight(weight);
        
        //Check balancedness
        int tlength = boolfun.getTlength();
        if(weight == tlength/2) {
            
            boolfun.setBalanced(true);
            
        }
    }
    
    /**
     * Computes the ANF and the algebraic degree of a Boolean function.
     * 
     * @param boolfun   A BooleanFunction object already initialized with one of
     *                  the three constructors provided, representing a Boolean
     *                  function
     * 
     */
    public static void computeAlgProp(BooleanFunction boolfun) {
        
        //Since the Fast Moebius Transform method computes the ANF by directly
        //modifying the input array, the truth table array must first be cloned.
        boolean[] anfcoeffs = new boolean[boolfun.getTlength()];
        boolean[] ttable = boolfun.getTtable();
        System.arraycopy(ttable, 0, anfcoeffs, 0, anfcoeffs.length);
        
        //Call the FMT method to compute ANF coefficients and algebraic degree
        int algdeg = BoolTransf.computeFMT(anfcoeffs, 0, anfcoeffs.length);
        String anfexpr = BoolTransf.computeANFExpr(anfcoeffs, boolfun.getNvar());
        boolfun.setAnfcoeffs(anfcoeffs);
        boolfun.setAlgdeg(algdeg);
        boolfun.setAnfexpr(anfexpr);
        
    }
    
    /**
     * Computes the nonlinearity of a boolean function, given in input its
     * spectral radius and the number of variables. The formula is the following:
     * 
     * NL(f) = 2^(n-1) - sprad/2,
     * 
     * where n is the number of variables and sprad is the spectral radius.
     * 
     * @param sprad   the spectral radius of the function.
     * @param nvar    the number of variables of the function.
     * @return        the nonlinearity of the function
     */
    public static int computeNonlinearity(int sprad, int nvar) {

        int nl = (int)Math.pow(2, nvar-1) - sprad/2;

        return nl;

    }
    
    /**
     * Determine the order of correlation immunity or propagation criterion,
     * given the array of deviations in input.
     * 
     * @param devs  An int array of deviations from correlation immunity or
     *              propagation criterion
     * @return      The maximum index k such that devs[i]=0 for all i less than
     *              or equal to k
     */
    public static int computeOrder(int[] devs) {
        
        int k=0;
        
        //Note: order of the conditions in this while loop matter (check
        //if dev[k] is equal to 0 only if k is less than the length of the array)
        while(k<devs.length && devs[k]==0) {
            
            k++;
            
        }
        
        return k;
        
    }
    
    /**
     * Computes the Walsh Transform of a Boolean function and the related
     * cryptographic properties, that is spectral radius, nonlinearity and
     * correlation immunity of order k. For correlation immunity of order k, it
     * is necessary to pass the int matrix containing all indices of Hamming
     * weight between 1 and k.
     * 
     * @param boolfun   a BooleanFunction object representing a boolean function.
     * @param indices   An int matrix where each row i in [0..indices.length-1]
     *                  is an int array holding the integer representations of
     *                  the binary vectors of Hamming weight i+1. Used to
     *                  compute the deviations from correlation-immunity of
     *                  order k, where k=indices.length. See method
     *                  lowlevelfunc.CombTools.createIndices() to create this
     *                  matrix.
     */
    public static void computeWalshProp(BooleanFunction boolfun, int[][] indices) {
        
        //Since the Fast Walsh Transform method computes the transform by
        //directly modifying the input array, the polar truth table array must
        //first be cloned.
        int[] whcoeffs = new int[boolfun.getTlength()];
        int[] poltable = boolfun.getPoltable();
        System.arraycopy(poltable, 0, whcoeffs, 0, whcoeffs.length);
        
        //Call the FWT method to compute the Walsh transform, the spectral
        //radius  and the nonlinearity of the function
        int sprad = BoolTransf.computeFWT(whcoeffs, 0, whcoeffs.length);
        int nvar = boolfun.getNvar();
        int nlin = computeNonlinearity(sprad, nvar);
        boolfun.setWhcoeffs(whcoeffs);
        boolfun.setSprad(sprad);
        boolfun.setNlin(nlin);
        
        //Compute the array of deviations from correlation immunity and the
        //order of correlation immunity
        int[] cid = BoolTransf.computeDevs(whcoeffs, indices);
        boolfun.setCid(cid);
        int ciord = computeOrder(cid);
        boolfun.setCiord(ciord);
        
    }
    
    /**
     * Compute the sum of squares indicator (i.e., sum of the squared
     * autocorrelation spectrum).
     * 
     * @param vector    An int array representing the autocorrelation spectrum
     *                  of a boolean function
     * @return          The sum of the squared values in the vector,
     *                  corresponding to the sum of squares indicator of the
     *                  boolean function
     */
    public static int computeSSI(int[] vector) {
        
        int ssi = 0;
        
        for(int i=0; i<vector.length; i++) {
            
            ssi += (int)(Math.pow(vector[i],2));
            
        }
        
        return ssi;
        
    }
    
    /**
     * Computes the number of nonzero linear structures of a Boolean function,
     * given its autocorrelation function. A nonzero input vector a is a nonzero
     * linear structure if and only if |AC(a)|=2^n (where n is the number of
     * variables of the Boolean function, so 2^n equals vector.length).
     * 
     * @param vector    An int array holding the autocorrelation spectrum of a
     *                  boolean function
     * @return          The number of nonzero linear structures of the function
     */
    public static int countNZLinStruct(int[] vector) {
        
        int nzlstr = 0;
        
        for(int i=1; i<vector.length; i++) {
            if((int)Math.abs(vector[i]) == vector.length) {
                nzlstr++;
            }
        }
        
        return nzlstr;
        
    }
    
    /**
     * Computes the autocorrelation function of a Boolean function and the related
     * cryptographic properties, that is maximum autocorrelation value, sum of
     * squares indicator, and propagation criterion PC(k). For propagation
     * criterion PC(k), it is necessary to pass the int matrix containing all
     * indices of Hamming weight between 1 and k.
     * 
     * @param boolfun   a BooleanFunction object representing a boolean function.
     * @param indices   An int matrix where each row i in [0..indices.length-1]
     *                  is an int array holding the integer representations of
     *                  the binary vectors of Hamming weight i+1. Used to
     *                  compute the deviations from correlation-immunity of
     *                  order k, where k=indices.length. See method
     *                  lowlevelfunc.CombTools.createIndices() to create this
     *                  matrix.
     * @param mode      boolean flag indicating whether the Walsh transform
     *                  has still to be computed (true) or not (false)
     */
    public static void computeACProp(BooleanFunction boolfun, int[][] indices,
            boolean mode) {
        
        //Initialize accoeffs array depending on the boolean flag
        int[] accoeffs = new int[boolfun.getTlength()];
        if(mode) {
            
            //The Walsh transform has still to be computed. Copy the polar
            //truth table in accoeffs
            int[] poltable = boolfun.getPoltable();
            System.arraycopy(poltable, 0, accoeffs, 0, accoeffs.length);
            
        }   else {
            
            //Walsh transform already computed, copy its coefficients in accoeffs
            int[] whcoeffs = boolfun.getWhcoeffs();
            System.arraycopy(whcoeffs, 0, accoeffs, 0, accoeffs.length);
            
        }
        
        //Call computeAC() with accoeffs and mode. The flag mode has the same
        //semantic in computeACProp() and computeAC().
        int acmax = BoolTransf.computeAC(accoeffs, mode);
        boolfun.setAccoeffs(accoeffs);
        boolfun.setAcmax(acmax);
        
        //Compute Sum-of-Squares Indicator, propagation criteria deviations array,
        //order of propagation criterion and number of nonzero linear structures
        int ssi = computeSSI(accoeffs);
        boolfun.setSsi(ssi);
        int nzlin = countNZLinStruct(accoeffs);
        boolfun.setNlinstruct(nzlin);
        int[] pcd = BoolTransf.computeDevs(accoeffs, indices);
        boolfun.setPcd(pcd);
        int pcord = computeOrder(pcd);
        boolfun.setPcord(pcord);
        
    }
    
    /**
     * Wrapper method to compute all cryptographic properties of a Boolean
     * function.
     * 
     * @param boolfun   a BooleanFunction object representing a Boolean function
     * @param indices   An int matrix where each row i in [0..indices.length-1]
     *                  is an int array holding the integer representations of
     *                  the binary vectors of Hamming weight i+1. Used to
     *                  compute the deviations from correlation-immunity of
     *                  order k, where k=indices.length. See method
     *                  lowlevelfunc.CombTools.createIndices() to create this
     *                  matrix.
     */
    public static void computeAllCryptoProp(BooleanFunction boolfun,
            int[][] indices) {
        
        //Compute weight and balancedness
        computeWeightProp(boolfun);
        
        //Compute FMT-related attributes (ANF and algebraic degree)
        computeAlgProp(boolfun);
        
        //Compute FWT-related attributes
        computeWalshProp(boolfun, indices);
        
        //Compute AC-related attributes. The Walsh transform has already been
        //computed, so the flag mode is set to false to avoid recomputing it
        computeACProp(boolfun, indices, false);
        
    }
    
    /**
     * Print the decimal and hexadecimal representations of the truth table
     * of a Boolean function, and its number of variables
     * 
     * @param boolfun   a BooleanFunction object representing a Boolean function
     */
    public static void printFuncNum(BooleanFunction boolfun) {
        
        int nvar = boolfun.getNvar();
        System.out.println("\nFunction Decimal code: "+boolfun.getDeccode());
        System.out.println("Function Hex code: "+boolfun.getHexcode());
        System.out.println("Number of variables: "+nvar);
        
    }
    
    /**
     * Prints the truth table, Walsh spectrum and autocorrelation function
     * of a boolean function.
     * 
     * @param boolfun a BooleanFunction object representing a boolean function.
     */
    public static void printFuncTables(BooleanFunction boolfun) {
        
        //Print truth table
        int nvar = boolfun.getNvar();
        System.out.println("\nTruth table:");
        boolean[] ttable = boolfun.getTtable();
        for(int i=0; i<ttable.length; i++) {
            
            System.out.println("f("+
                    NumTools.bool2Bin(NumTools.dec2Bin(i, nvar))+") = "+
                    NumTools.singleBool2Bin(ttable[i]));
            
        }
        
        //Print Walsh spectrum.
        System.out.println("\nWalsh spectrum:");
        int[] whcoeffs = boolfun.getWhcoeffs();
        for(int i=0; i<whcoeffs.length; i++) {
            
            System.out.println("F("+
                    NumTools.bool2Bin(NumTools.dec2Bin(i, nvar))+") = "+
                    whcoeffs[i]);
        
        }
        
        //Print autocorrelation function.
        System.out.println("\nAutocorrelation function:");
        int[] accoeffs = boolfun.getAccoeffs();
        for(int i=0; i<accoeffs.length; i++) {
            
            System.out.println("r("+
                    NumTools.bool2Bin(NumTools.dec2Bin(i, nvar))+") = "+
                    accoeffs[i]);
        
        }
        
    }
    
    /**
     * Prints the algebraic normal form (ANF) of a boolean function.
     * 
     * @param boolfun a BooleanFunction object representing a boolean function.
     */
    public static void printANF(BooleanFunction boolfun) {
        
        System.out.println("\nAlgebraic Normal Form:");
        System.out.println(boolfun.getAnfexpr());
        
    }
    
    /**
     * Prints the cryptographic properties of a boolean function.
     * 
     * @param boolfun a BooleanFunction object representing a boolean function.
     * @param indprop an index representing the maximum order of the CI and PC
     *                properties to be printed.
     */
    public static void printCryptoProp(BooleanFunction boolfun, int indprop) {
        
        System.out.println("\n\nCryptographic Properties");
        System.out.print("Weight = "+boolfun.getWeight()+"; ");
        System.out.print("Balanced = "+boolfun.isBalanced()+"; ");
        System.out.println("Algebraic degree = "+boolfun.getAlgdeg());
        System.out.println("\nSpectral radius = "+boolfun.getSprad()+
                "; Nonlinearity = "+boolfun.getNlin());
        
        System.out.println("\nCorrelation Immunity Deviations up to order "
                +indprop+":");
        
        for(int i=0; i<indprop; i++) {
            System.out.println("CI("+(i+1)+") = "+boolfun.getCid()[i]);
        }
        
        if(boolfun.getCiord()>0) {
            
            if(boolfun.isBalanced()) {
                System.out.println("\nThe function is "+boolfun.getCiord()+
                        "-resilient");
            } else {
                System.out.println("\nThe function is correlation immune of"
                        + " order "+boolfun.getCiord());
            }
            
        }
        
        System.out.println("\nMaximum autocorrelation value: "+
                boolfun.getAcmax()+";\nSum of squares indicator: "+
                boolfun.getSsi()+";\nNumber of nonzero linear structures: "+
                boolfun.getNlinstruct());
        
        System.out.println("\nPropagation Criteria Deviations up to order "+
                indprop+":");
        
        for(int i=0; i<indprop; i++) {
            System.out.println("PC("+(i+1)+") = "+boolfun.getPcd()[i]);
        }
        
        if(boolfun.getPcord()>0) {
            
            System.out.println("\nThe function satisfies the propagation"
                    + " criterion PC("+boolfun.getPcord()+")");
            
        }
        
    }

}
