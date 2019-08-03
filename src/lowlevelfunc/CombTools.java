package lowlevelfunc;

/**
 * Utilities to generate combinations of objects (particularly bitstrings with
 * a specified Hamming weight) and compute their cardinalities. The bitstrings
 * are sorted in LSBF (Least Significant Bit First) order, unless otherwise
 * specified.
 * 
 * @author  Luca Mariot
 * @version 1.0
 */
import java.math.BigInteger;
import java.io.*;

public class CombTools {
    
    /**
     * Computes the factorial of an integer number.
     * 
     * @param num   an integer number
     * @return fact the factorial of num
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
     * @return bCoeff   the binomial coefficient (n,k)
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
     * Generates all the (s+t)-bit strings with Hamming weight t, in decimal
     * notation. The algorithm is described in Knuth, "The Art of Computer
     * Programming, pre-Fascicle 3A" (Algorithm L, p. 4).
     * 
     * @param s         number of 0s in the bitstrings
     * @param t         number of 1s in the bitstrings
     * @return combset  array of integers representing the bitstrings of length
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
            int deccomb = BinTools.bin2Dec(conf);
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
     * Programming, pre-Fascicle 3A" (Algorithm L, p. 4).
     * 
     * @param s         number of 0s in the bitstrings
     * @param t         number of 1s in the bitstrings
     * @return combset  array of integers representing the bitstrings of length
     *                  (s+t) and Hamming weight t
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
     * Remap a binary string by substituting the 0s with val1 and the 1s with val2.
     * 
     * @param comb
     * @param val1
     * @param val2
     * @return 
     */
    public static int[] remapCombination(boolean[] comb, int val1, int val2) {
        
        int[] toRet = new int[comb.length];
        
        for(int i=0; i<comb.length; i++) {
            
            if(comb[i]) {
                
                toRet[i] = val2;
                
            } else {
                
                toRet[i] = val1;
                
            }
            
        }
        
        return toRet;
        
    }
    
    /**
     * Remap a binary string of length n and weight t to a multiset permutation
     * of length n where:
     * 
     * - the n-t 0s are remapped according to a multiset permutation of length n-k
     * - the t 1s are remapped to a new value
     * 
     * @param comb
     * @param msperm
     * @param newval
     * @return 
     */
    public static int[] remapMultisetPerm(boolean[] comb, int t, int[] msperm, int newval) {
        
        int n = comb.length;
        int[] toRet = new int[n];
        
        //Step 1: build the maps of 0s and 1s of the binary string
        int[][] ozmaps = BoolFunReps.buildZerosOnesMaps(comb, t);
        
        //Step 2: remap the 0s of the binary string with the values of the multiset permutation
        for(int i=0; i<ozmaps[0].length; i++) {
            
            toRet[ozmaps[0][i]] = msperm[i];
            
        }
        
        //Step 3: remap the 1s of the binary string with the new value
        for(int i=0; i<ozmaps[1].length; i++) {
            
            toRet[ozmaps[1][i]] = newval;
            
        }
        
        return toRet;        
        
    }
    
    /**
     * Generate all words over an alphabet of k symbols where each symbol
     * occurs f_k times.
     * 
     * @param alphabet
     * @param frequencies
     * @return 
     */
    public static int[][] genFreqWords(int[] alph, int[] freq) {
        
        int k = alph.length;
        
        //Step 1: compute the partial sums of the frequencies
        int[] psums = new int[k];
        int n = 0;
        
        for(int i=0; i<k; i++) {
            
            n += freq[i];
            psums[i] = n;
            
        }
        
        int[][][] wordlists = new int[k][][];   //three dimensional matrix
                                                //holding the lists of words.
                                                //The last matrix at index k-1
                                                //is the list to be returned
        int[] sizes = new int[k];               //sizes of the lists.
        
        //initialize the sizes of the lists.
        sizes[0] = 1;
        
        //Step 2: generate the words in a bottom-up way. Notice that the first
        //list of words (that is, the first matrix at index 0 of wordlists)
        //must be the empty list, so we start from 1
        for(int i=1; i<k; i++) {
            
            //Step 2a: generate the binary strings of length psums[i+1] and
            //weight freq[i], using Knuth's algorithm.
            int s = psums[i-1];        //number of 0s in the string
            int t = freq[i];       //number of 1s in the string
            
            //Compute the size of the current list and instantiate the matrix to hold it
            int wordlength = s+t;
            sizes[i] = sizes[i-1] * binCoeff(s+t, t);
            wordlists[i] = new int[sizes[i]][wordlength];

            //Begin binary string generation
            int index = 0; //index for the binary strings (range between 0 and (s+t, t) binomial coefficient

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

                for(int l=0; l<t; l++) {
                    conf[comb[l]] = true;
                }

                //Visit the current binary string by generating all new multiset permutations
                if(i==1) {
                    
                    //In this case, there are no previous list of words, so
                    //simply map the current binary string to a string over the
                    //first two symbols of the alphabet
                    wordlists[i][index] = remapCombination(conf, alph[i-1], alph[i]);
                    
                } else {
                    
                    //Else, we have to take all multiset permutations in the previous
                    //list and match them with the current binary string
                    for(int z=0; z<wordlists[i-1].length; z++) {
                        
                        int[][] prevlist = wordlists[i-1];
                        int shiftindex = index*wordlists[i-1].length;   //position of the current block to generate
                        wordlists[i][shiftindex+z] = remapMultisetPerm(conf, t, prevlist[z], i+1);
                        
                    }
                    
                }
                
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
            
        }
        
        //Return the last list
        return wordlists[k-1];
        
    }
    
    /** 
     * Generates all the balanced boolean functions with nvar number of
     * variables, and checks the following cryptographic properties: correlation
     * immunity, propagation criterion, nonlinearity, algebraic degree and
     * number of nonzero linear structures.
     * 
     * NOTE: this method should be used only if the number of variables is
     * less than or equal to 5.
     * 
     * @param nvar  number of variables of the boolean functions
     * 
     */
    public static void checkBalFunc(int nvar, PrintWriter[] pwr) 
            throws IOException {
        
        int size = (int)Math.pow(2, nvar);
        int t = size/2;
        
        //Build the lists of indices to check cryptographic properties
        int[][] indices1 = new int[2][];
        indices1[0] = CombTools.genBinCombs(2, 1);
        indices1[1] = CombTools.genBinCombs(1, 2);
        
        /*int[][] indices2 = new int[4][];
        indices2[0] = CombTools.genBinCombs(4, 1);
        indices2[1] = CombTools.genBinCombs(3, 2);
        indices2[2] = CombTools.genBinCombs(2, 3);
        indices2[3] = CombTools.genBinCombs(1, 4);*/
        
        //Initialisation
        int[] comb = new int[t+2]; //the two additional cells are sentinels.
        for(int j=0; j<t; j++) {
            comb[j] = j;
        }
        comb[t] = size;
        comb[t+1] = 0;
        
        int j = 0;
        
        while(j<t) {
            
            boolean[] conf = new boolean[size];
            
            //Create the combination
            for(int k=0; k<t; k++) {
                conf[comb[k]] = true;
            }
            
            //Chaos-related properties (permutivity)
            boolean[] ip = new boolean[nvar];
            for(int i=0; i<nvar; i++) {

                ip[i] = CheckPermutivity.checkPerm(conf,nvar,i);

            }
            
            //Computes the various cryptographic properties.
            
            //FWT-related properties (nonlinearity, correlation-immunity)
            BigInteger deccomb = BinTools.bin2DecBig(conf); 
            int[] polconf = BinTools.bin2Pol(conf);
            BoolTransf.calcFWT(polconf, 0, polconf.length);
            int sprad = BoolTransf.findMaxCoeff(polconf, false);
            int nlin = BoolTransf.calcNL(sprad, nvar);
            int[] cid = BoolTransf.calcDevs(polconf, indices1);
            
            //Autocorrelation-related properties (PC(l), number 
            //of linear structures)
            BoolTransf.calcAC(polconf, true);
            int acmax = BoolTransf.findMaxCoeff(polconf, true);
            int[] pcd = BoolTransf.calcDevs(polconf, indices1);
            int nzls = BoolTransf.countNZLinStruct(polconf);
            
            //FMT-related properties (algebraic degree)
            BoolTransf.calcFMT(conf, 0, conf.length);
            int algdeg = BoolTransf.calcAlgDeg(conf, indices1);
            
            //Print the results to the specified files
            /*if(nlin == 12) {
                
                pwr[0].println("Rule "+deccomb);
                pwr[0].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[0].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[0].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]);
                        //"; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[0].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[0].println("Degree = "+algdeg);
                pwr[0].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[0].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[0].println("");
                
            }
            
            if(cid[0] == 0) {
                
                pwr[1].println("Rule "+deccomb);
                pwr[1].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[1].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[1].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]);
                        //"; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[1].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[1].println("Degree = "+algdeg);
                pwr[1].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[1].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[1].println("");
                
            }
            
            if(cid[1] == 0) {
                
                pwr[2].println("Rule "+deccomb);
                pwr[2].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[2].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[2].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]);
                        //"; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[2].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[2].println("Degree = "+algdeg);
                pwr[2].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[2].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[2].println("");
                
            } */
            
            if(pcd[0] == 0) {
                
                pwr[3].println("Rule "+deccomb);
                pwr[3].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[3].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[3].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]);
                        //"; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[3].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[3].println("Degree = "+algdeg);
                pwr[3].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                /*pwr[3].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);*/
                pwr[3].println("");
                
            }
            
            /*if(pcd[1] == 0) {
                
                pwr[4].println("Rule "+deccomb);
                pwr[4].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[4].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[4].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[4].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[4].println("Degree = "+algdeg);
                pwr[4].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[4].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[4].println("");
                
            }
            
            if(pcd[2] == 0) {
                
                pwr[5].println("Rule "+deccomb);
                pwr[5].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[5].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[5].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[5].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[5].println("Degree = "+algdeg);
                pwr[5].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[5].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[5].println("");
                
            }
        
            if(pcd[3] == 0) {
                
                pwr[6].println("Rule "+deccomb);
                pwr[6].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[6].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[6].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[6].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[6].println("Degree = "+algdeg);
                pwr[6].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[6].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[6].println("");
                
            }
            
            if(ip[0]) {
                
                pwr[7].println("Rule "+deccomb);
                pwr[7].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[7].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[7].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[7].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[7].println("Degree = "+algdeg);
                pwr[7].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[7].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[7].println("");
                
            }
            
            if(ip[ip.length-1]) {
                
                pwr[8].println("Rule "+deccomb);
                pwr[8].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[8].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[8].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[8].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[8].println("Degree = "+algdeg);
                pwr[8].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[8].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[8].println("");
                
            }
            
            if(ip[0] && ip[ip.length-1]) {
                
                pwr[9].println("Rule "+deccomb);
                pwr[9].println("Spectral Radius = "+sprad+"; Nonlin = "+nlin);
                pwr[9].println("CI(1) = "+cid[0]+"; CI(2) = "+cid[1]);
                pwr[9].println("PC(1) = "+pcd[0]+"; PC(2) = "+pcd[1]+
                        "; PC(3) = "+pcd[2]+"; PC(4) = "+pcd[3]);
                pwr[9].println("ACMax = "+acmax+"; #NZLS = "+nzls);
                pwr[9].println("Degree = "+algdeg);
                pwr[9].println("Left Permutive = "+ip[0]+"; Right Permutive = "+
                        ip[ip.length-1]);
                pwr[9].println("2-Permutive = "+ip[1]+"; 3-Permutive = "+ip[2]+
                        "; 4-Permutive = "+ip[3]);
                pwr[9].println("");
                
            }*/
            
            j=0;
            while((comb[j]+1)==comb[j+1]) {
                comb[j] = j;
                j++;
            }
            
            if(j<t) {
                comb[j]++;
            }
            
        }
      
    }
    
    /** Generates LR-permutive functions of nvar variables in a given interval.
     * The algorithm enumerates all the variations included between index start
     * and index end of the 2^(n-2) graphs of four nodes with ring topology.
     * In these graphs, two nodes are connected by an edge iff they represent
     * inputs which must have complementary output values in order to
     * satisfy the LR-permutivity condition.
     * 
     * @param nvar  Number of variables of the boolean functions
     * @param pwr   PrintWriter used to print the results to file
     * @param start Index of the first LR-permutive function to be generated.
     * @param end   Index of the last LR-permutive function to be generated.
     * 
     */
    public static void genLRPermFunc(int nvar, PrintWriter[] pwr, BigInteger start,
            BigInteger end) throws IOException {
        
        int funclength = (int)Math.pow(2, nvar);
        int ngraphs = (int)Math.pow(2,nvar-2);
        int half = (int)Math.pow(2, nvar-1);
        BigInteger index = new BigInteger("0");
        index = start;
        
        //Build the lists of indices to check cryptographic properties
        int[][] indices = new int[4][];
        indices[0] = CombTools.genBinCombs(6, 1);
        indices[1] = CombTools.genBinCombs(5, 2);
        indices[2] = CombTools.genBinCombs(4, 3);
        indices[3] = CombTools.genBinCombs(3, 4);
        
        while(index.compareTo(end) < 0) {
            
            boolean[] func = new boolean[funclength];
            boolean[] graphconf = BinTools.dec2Bin(index, ngraphs);
            
            for(int j=0; j<ngraphs; j++) {
                
                //Instantiate nodes on the j-th graph
                func[2*j] = graphconf[j];
                func[(2*j)+half+1] = graphconf[j];
                func[(2*j)+1] = !graphconf[j];
                func[(2*j)+half] = !graphconf[j];
                
            }
            
            //Compute cryptographic properties.
            
            //FWT-related properties
            int[] polconf = BinTools.bin2Pol(func);
            BoolTransf.calcFWT(polconf, 0, polconf.length);
            int sprad = BoolTransf.findMaxCoeff(polconf, false);
            int nlin = BoolTransf.calcNL(sprad, nvar);
            int[] cid = BoolTransf.calcDevs(polconf, indices);
            
            BoolTransf.calcAC(polconf, true);
            int acmax = BoolTransf.findMaxCoeff(polconf, true);
            int[] pcd = BoolTransf.calcDevs(polconf, indices);
            int nzls = BoolTransf.countNZLinStruct(polconf);
            
            //FMT-related properties (algebraic degree)
            BoolTransf.calcFMT(func, 0, func.length);
            int algdeg = BoolTransf.calcAlgDeg(func, indices);
            
            //Print the function
            BigInteger funcnum = BinTools.bin2DecBig(func);
            
            if(nlin == 56) {
                
                //Case nonlinearity = 56
                pwr[0].print("Index "+index+" ");
                pwr[0].print("Rule "+funcnum+" ");
                pwr[0].print("NL = "+nlin+" ");
                pwr[0].print("DG = "+algdeg+" ");
                pwr[0].print("LS = "+nzls+" ");
                pwr[0].print("CI1 = "+cid[0]+" CI2 = "+cid[1]+" CI3 = "+cid[2]+" CI4 = "+cid[3]+" ");
                pwr[0].print("PC1 = "+pcd[0]+" PC2 = "+pcd[1]+" PC3 = "+pcd[2]+" PC4 = "+pcd[3]+"\n");  
                
            }
            
            if((cid[1] == 0) && (cid[3] != 0) && ((nlin > 48) || ((nlin == 48) && (algdeg == 4)))) {
                
                //Case resiliency order = 2, nonlinearity > 48 or
                //nonlinearity = 48 and algebraic degree = 4.
                pwr[1].print("Index "+index+" ");
                pwr[1].print("Rule "+funcnum+" ");
                pwr[1].print("NL = "+nlin+" ");
                pwr[1].print("DG = "+algdeg+" ");
                pwr[1].print("LS = "+nzls+" ");
                pwr[1].print("CI1 = "+cid[0]+" CI2 = "+cid[1]+" CI3 = "+cid[2]+" CI4 = "+cid[3]+" ");
                pwr[1].print("PC1 = "+pcd[0]+" PC2 = "+pcd[1]+" PC3 = "+pcd[2]+" PC4 = "+pcd[3]+"\n");  
                
            }
            
            if(pcd[0] == 0) {
                
                //Case PC(1)
                pwr[2].print("Index "+index+" ");
                pwr[2].print("Rule "+funcnum+" ");
                pwr[2].print("NL = "+nlin+" ");
                pwr[2].print("DG = "+algdeg+" ");
                pwr[2].print("LS = "+nzls+" ");
                pwr[2].print("CI1 = "+cid[0]+" CI2 = "+cid[1]+" CI3 = "+cid[2]+" CI4 = "+cid[3]+" ");
                pwr[2].print("PC1 = "+pcd[0]+" PC2 = "+pcd[1]+" PC3 = "+pcd[2]+" PC4 = "+pcd[3]+"\n");  
                
            }
            
            //Update index
            index = index.add(new BigInteger("1"));
            
        }
        
        
    }
    
    /**
     * Generates all ind-permutive functions of nvar variables.
     * 
     * @param nvar number of variables of the functions
     * @param ind  index of the variable which satisfies permutivity.
     */
    public static void GenerateIpermFunc(int nvar, int ind) {
        
        int ngraph = (int)Math.pow(2,nvar-1);
        int nfunc = (int)Math.pow(2, ngraph);
        int funclength = (int)Math.pow(2,nvar);
        
        for(int i=nfunc-1; i>=0; i--) {
            
            boolean[] conf = BinTools.dec2BinMod(i, ngraph);
            boolean[] func = new boolean[funclength];
            
            for(int j=0; j<conf.length; j++) {
                
                boolean[] input1 = BinTools.dec2BinMod(j, nvar);
                boolean[] input2 = new boolean[nvar];
                System.arraycopy(input1, 0, input2, 0, input1.length);
                input2[ind] = !input2[ind];
                int j2 = BinTools.bin2Dec(input2);
                func[j] = conf[j];
                func[j2] = !conf[j];
                
            }
            
            System.out.println(BinTools.bin2Dec(func));
            
        }
        
    }   
    
    public static void main(String[] args) {
        
        boolean[][] cubweights = genBinCombsBin(3, 3);
        
        for (int i=0; i<cubweights.length; i++) {
            
            System.out.println(BinTools.bin2Dec(cubweights[i])+" -> "+BinTools.bool2Bin(cubweights[i]));
            
        }
        
        System.out.println("");
        
        boolean[][] quadweights = genBinCombsBin(4, 2);
        
        for (int i=0; i<quadweights.length; i++) {
            
            System.out.println(BinTools.bin2Dec(quadweights[i])+" -> "+BinTools.bool2Bin(quadweights[i]));
            
        }
        
        System.out.println("");
        
        boolean[][] oneweights = genBinCombsBin(5, 1);
        
        for (int i=0; i<oneweights.length; i++) {
            
            System.out.println(BinTools.bin2Dec(oneweights[i])+" -> "+BinTools.bool2Bin(oneweights[i]));
            
        }
        
    }
    
}
