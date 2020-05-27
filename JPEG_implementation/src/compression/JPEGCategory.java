package compression;

import utils.Utils;

public class JPEGCategory {
	
	private double coeff;
	private int cat;		// The category of the coefficient.
	private double prec;	// The precision of the coefficient.
	private int runlength;	// The RLE of the coefficient.
	
	/** Constructor. */
	public JPEGCategory(double coeff, int cat, double prec) {
		this.setCoeff(coeff);
		this.setCat(cat);
		this.setPrec(prec);
	}
	
	public double getCoeff() {
		return coeff;
	}

	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}

	/** @return the category of the coefficient. */
	public int getCat() {
		return cat;
	}
	
	/**
	 * Sets the category of the coefficient to the given value.
	 * @param cat The category.
	 */
	public void setCat(int cat) {
		this.cat = cat;
	}
	
	/** @return The precision of the coefficient. */
	public double getPrec() {
		return prec;
	}
	
	/**
	 * Sets the precision of the coefficient to the given value.
	 * @param prec The precision of the coefficient.
	 */
	public void setPrec(double prec) {
		this.prec = prec;
	}

	/** @return The RLE of the coefficient. */
	public int getRunlength() {
		return runlength;
	}

	/**
	 * Sets the RLE of the coefficient to the given value.
	 * @param runlength The RLE of the coefficient.
	 */
	public void setRunlength(int runlength) {
		this.runlength = runlength;
	}
	
	/**
	 * Converts the concatenation of the RLE and the category of the coefficient into its binary 
	 * representation and returns it as a String.
	 * @return The concatenation of the RLE and the category of the coefficient as a binary representation.
	 */
	public String convertToBinary() {
		String category = Utils.convertIntToBinary(cat, 4);		// Category represented in binary.
		String rle = Utils.convertIntToBinary(runlength, 4);	// RLE represented in binary.
		return rle + category;
	}
	
	public String huffmanEncode() {
		int index=this.runlength*10+this.cat;
		
		//if EOB
		if(this.runlength==0&&this.cat==0) {
			return "1010";
		}
		int position=(int)this.prec;
		//System.out.println(this.runlength);
		String huffmanString=HuffmannTable.huffmanJPG[index]+Utils.convertIntToBinary(position, this.cat);
		
		return huffmanString;
	}
	
	public String huffmanEncodeDC() {
		int position=(int)this.prec;
		String huffmanString=HuffmannTable.huffmannDC[this.cat]+Utils.convertIntToBinary(position, this.cat);
		
		return huffmanString;
	}
	
}