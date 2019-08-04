/**
 * Tests various cryptographic properties of a Boolean function passed as
 * command line argument and prints them.
 * 
 * @author Luca Mariot
 */

import java.math.BigInteger;
import lowlevelfunc.*;
import boolfun.*;

public class CheckPropSingleBoolFun {
    
    public static void main(String[] args) {
        
        //The arguments passed must be the function number (according to
        //Wolfram's convention) and the number of variables.
        if(args.length!= 4) {
            
            System.err.println("\nCheckPropSingleBoolFun");
            System.err.println("\nDescription: Computes various cryptographic "
                    + "properties of a Boolean function passed as a command line"
                    + " argument and prints them");
            
            System.err.println("\nUsage: java CheckPropSingleBoolFun nvar mode"
                    + " func_code verbose");
            
            System.err.println("\nwhere:");
            
            System.err.println("\n- nvar is the number of variables of the"
                    + " function ");
            System.err.println("- mode is a string specifying the encoding of"
                    + " the truth table of the function (bin: binary encoding,"
                    + " dec: decimal encoding, hex: hexadecimal encoding");
            System.err.println("- func_code is the truth table of the function,"
                    + " encoded according to the mode argument");
            System.err.println("- verbose is a boolean flag. If equal to true,"
                    + " verbose mode is adopted and all the truth table, Walsh"
                    + " coefficients and autocorrelation coefficients are"
                    + " printed along with the cryptographic properties. If set"
                    + " to false, only the cryptographic properties are printed");
            System.err.println("\nExample: java CheckPropSingleBoolFun 3 dec"
                    + " 150 true");
            System.err.println("Prints the cryptographic properties and the"
                    + " truth table, Walsh coefficients and autocorrelation"
                    + " coefficients of the Boolean function of 3 variables with"
                    + " decimal encoding 150.\n");
            
            System.exit(1);           
            
        }
        
        int nvar = Integer.parseInt(args[0]);
        String mode = args[1];
        String tableStr = args[2];
        BooleanFunction boolfun = null;
        
        //Read the truth table of the function depending on the encoding mode
        switch(mode) {
            
            case "bin" :    {
                
                //Binary mode selected, use second constructor
                boolean[] ttable = NumTools.bin2Bool(tableStr);
                boolfun = new BooleanFunction(ttable, nvar);
                
                break;
                
            }
            
            case "dec" :    {
                
                //Decimal mode selected, use first constructor
                BigInteger funcnum = new BigInteger(tableStr);
                boolfun = new BooleanFunction(funcnum, nvar);
                
                break;
                
            }
            
            case "hex" :    {
                
                //Hex mode selected, use third constructor
                boolfun = new BooleanFunction(tableStr, nvar);
                
                break;
            }
            
            default :   {
                
                //Error in the mode string, exit
                System.err.println("\nError: "+mode+" does not correspond to any"
                        + " valid encoding mode (use only bin, dec or hex!)\n");
                System.exit(2);
                
            }
            
        }
        
        //Read last command line argument for verbose mode
        boolean verbose = Boolean.parseBoolean(args[3]);
        
        //Creates the set of indices to check crypto properties. In particular,
        //we consider all weights from 1 to nvar
        int[][] indices = CombTools.createIndices(nvar);
        
        //Compute all cryptographic properties of the function
        CryptoProperties.computeAllCryptoProp(boolfun, indices);
        
        //Print the decimal and hexadecimal code of the function
        CryptoProperties.printFuncNum(boolfun);
        
        //If verbose mode is selected, print truth table, Walsh transform and
        //autocorrelation function of the boolean function
        if(verbose) {
            CryptoProperties.printFuncTables(boolfun);
        }
        
        //Print ANF and cryptographic properties of the function
        CryptoProperties.printANF(boolfun);
        CryptoProperties.printCryptoProp(boolfun, nvar);
        
        System.out.println("");
        
    }
    
}
