package boolfun;

/**
 * Class which represents a generic Boolean function of n variables, along with
 * its representations (truth table, ANF), spectra (Walsh, autocorrelation) and
 * cryptographic properties (balancedness, algebraic degree, nonlinearity,
 * resiliency, propagation criterion, number of nonzero linear structures).
 * 
 * @author Luca Mariot
 * 
 */

import java.math.BigInteger;
import lowlevelfunc.NumTools;

public class BooleanFunction {
    
    //Representation-related attributes.
    private boolean[] ttable;       //Boolean representation of the truth table
    private int[] poltable;         //Polar representation of the truth table
    private BigInteger deccode;     //Decimal representation of the truth table
    private String hexcode;         //Hex representation of the truth table
    private int nvar;               //Number of variables
    private int tlength;            //Length of the truth table (2^nvar)
    private int hlength;            //Length of the hex code representation (tlength/4)
    
    
    //Transforms-related attributes.
    private boolean[] anfcoeffs;    //Coeffients of the Algebraic Normal Form (ANF)
    private String anfexpr;         //String representation of the ANF
    private int[] whcoeffs;         //Coefficients of the Walsh Transform
    private int sprad;              //Spectral radius
    private int[] accoeffs;         //Coefficients of the autocorrelation function
    private int acmax;              //Maximum autocorrelation coefficient
    
    //Crypto-related attributes.
    private int weight;             //Hamming weight of the truth table
    private boolean balanced;       //Balancedness flag of the function
    private int algdeg;             //Algebraic degree
    private int nlin;               //Nonlinearity
    private int ssi;                //Sum of square indicator
    private int nlinstruct;         //Number of nonzero linear structures
    private int[] cid;              //Correlation-immunity deviations array
    private int[] pcd;              //Propagation criteria deviations array
    private int ciord;              //Correlation-immunity order
    private int pcord;              //Propagation criterion order
    
    /* CONSTRUCTORS */
    //Note: in the constructors below, only the representation-related attributes
    //are effectively initialized. Transform-related and crypto-related attributes
    //must be explicitly computed at a later stage
    
    /** 
     * First constructor, decimal representation-based.
     * 
     * @param deccode   decimal representation of the truth table of the function
     * @param nvar      number of variables of the function
     */
    public BooleanFunction(BigInteger deccode, int nvar) {
        
        this.deccode = deccode;
        this.nvar = nvar;
        tlength = (int)Math.pow(2, nvar);
        hlength = tlength/4;
        
        hexcode = NumTools.dec2Hex(deccode, hlength);
        ttable = NumTools.dec2Bin(deccode, tlength);
        poltable = NumTools.bin2Pol(ttable);
        
    }
    
    /** 
     * Second constructor, truth table representation-based.
     * 
     * @param ttable    truth table representation of the function (MSBF order)
     * @param nvar      number of variables of the function
     */
    public BooleanFunction(boolean[] ttable, int nvar) {
        
        this.ttable = ttable;
        this.nvar = nvar;
        tlength = ttable.length;
        hlength = tlength/4;
        
        deccode = NumTools.bin2DecBig(ttable);
        hexcode = NumTools.bin2Hex(ttable);
        poltable = NumTools.bin2Pol(ttable);
        
    }

    /**
     * Third constructor, hexcode representation-based.
     * 
     * @param hexcode   hex representation of the truth table of the function
     * @param nvar      number of variables of the function
     */
    public BooleanFunction(String hexcode, int nvar) {
        
        this.hexcode = hexcode;
        this.nvar = nvar;
        hlength = hexcode.length();
        tlength = (int)Math.pow(2, nvar);
        
        deccode = NumTools.hex2DecBig(hexcode);
        ttable = NumTools.hex2Bin(hexcode);
        poltable = NumTools.bin2Pol(ttable);
        
    }
    
    
    /* GETTERS */

    public int[] getAccoeffs() {
        return accoeffs;
    }

    public int getAcmax() {
        return acmax;
    }

    public int getAlgdeg() {
        return algdeg;
    }

    public boolean[] getAnfcoeffs() {
        return anfcoeffs;
    }

    public String getAnfexpr() {
        return anfexpr;
    }

    public BigInteger getDeccode() {
        return deccode;
    }

    public String getHexcode() {
        return hexcode;
    }

    public int getHlength() {
        return hlength;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public int getNlinstruct() {
        return nlinstruct;
    }

    public int getNvar() {
        return nvar;
    }

    public int[] getPoltable() {
        return poltable;
    }

    public int getSprad() {
        return sprad;
    }

    public boolean[] getTtable() {
        return ttable;
    }

    public int getWeight() {
        return weight;
    }

    public int[] getWhcoeffs() {
        return whcoeffs;
    }

    public int getNlin() {
        return nlin;
    }

    public int[] getCid() {
        return cid;
    }

    public int[] getPcd() {
        return pcd;
    }

    public int getTlength() {
        return tlength;
    }

    public int getCiord() {
        return ciord;
    }

    public int getPcord() {
        return pcord;
    }
    
    public int getSsi() {
        return ssi;
    }
    
    
    /* SETTERS */

    public void setAccoeffs(int[] accoeffs) {
        this.accoeffs = accoeffs;
    }

    public void setAcmax(int acmax) {
        this.acmax = acmax;
    }

    public void setAlgdeg(int algdeg) {
        this.algdeg = algdeg;
    }

    public void setAnfcoeffs(boolean[] anfcoeffs) {
        this.anfcoeffs = anfcoeffs;
    }

    public void setAnfexpr(String anfexpr) {
        this.anfexpr = anfexpr;
    }

    public void setDeccode(BigInteger deccode) {
        this.deccode = deccode;
    }

    public void setNlinstruct(int nlinstruct) {
        this.nlinstruct = nlinstruct;
    }

    public void setNvar(int nvar) {
        this.nvar = nvar;
    }

    public void setPoltable(int[] poltable) {
        this.poltable = poltable;
    }

    public void setSprad(int sprad) {
        this.sprad = sprad;
    }

    public void setTtable(boolean[] ttable) {
        this.ttable = ttable;
    }

    public void setWhcoeffs(int[] whcoeffs) {
        this.whcoeffs = whcoeffs;
    }

    public void setNlin(int nlin) {
        this.nlin = nlin;
    }

    public void setCid(int[] cid) {
        this.cid = cid;
    }

    public void setPcd(int[] pcd) {
        this.pcd = pcd;
    }

    public void setSsi(int ssi) {
        this.ssi = ssi;
    }

    public void setHexcode(String hexcode) {
        this.hexcode = hexcode;
    }

    public void setTlength(int tlength) {
        this.tlength = tlength;
    }

    public void setHlength(int hlength) {
        this.hlength = hlength;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public void setCiord(int ciord) {
        this.ciord = ciord;
    }

    public void setPcord(int pcord) {
        this.pcord = pcord;
    }
    
}
