package compression;

import decompression.HuffmanDecoder;
import utils.Utils;

public class JPEGCategory {
	
	private double coeff;
	private int cat;		// The category of the coefficient.
	private double prec;	// The precision of the coefficient.
	private int runlength;	// The RLE of the coefficient.
	private static int prev=0;
	
	/** Constructor. */
	public JPEGCategory(double coeff, int cat, double prec) {
		this.setCoeff(coeff);
		this.setCat(cat);
		this.setPrec(prec);
	}
	public JPEGCategory() {
		
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
	
	public void huffmanDecodeDC(String huffmanString) {
		
		for(int i=HuffmannTable.huffmannDC.length-1; i>=0; i--) {
			int digits=HuffmannTable.huffmannDC[i].length();
			if(digits<huffmanString.length()) {
				String sub=huffmanString.substring(0, digits);
				if(sub.equals(HuffmannTable.huffmannDC[i])) {
					this.cat=i;
					String postSub=huffmanString.substring(digits);
					int toDec=Integer.parseInt(postSub, 2);
					this.prec=toDec;
				}
				
			}
			
		}
		this.coeff=HuffmanDecoder.assignCoefficant(this.cat, (int)this.prec)+prev;
		System.out.println(prev);
		prev=(int)this.coeff;
	}
	
	public void huffmanDecodeAC(String huffmanString) {
		
		String special=HuffmannTable.huffmanJPG[150];
		String special2=HuffmannTable.huffmanJPG[140];
		//System.out.println(special2);
				
		if(huffmanString.startsWith(special)) {
			this.runlength=15;
			this.prec=0;
			this.cat=0;
			this.coeff=0;
			return;
		}
		if(huffmanString.startsWith(special2)) {
			this.runlength=14;
			this.prec=0;
			this.cat=0;
			this.coeff=0;
			return;
		}
		
		
		for(int i=HuffmannTable.huffmanJPG.length-1; i>=0; i--) {
			int digits=HuffmannTable.huffmanJPG[i].length();
			if(digits<huffmanString.length()) {
				String sub=huffmanString.substring(0, digits);
				if(sub.equals(HuffmannTable.huffmanJPG[i])) {
					int index=i;
					String postSub=huffmanString.substring(digits);
					this.cat=postSub.length();
					this.prec=Integer.parseInt(postSub,2);
					this.runlength=(index/10)-this.cat;
					System.out.println("index is: "+index);
				}
			}
		}
		
		this.coeff=HuffmanDecoder.assignCoefficant(this.cat, (int)this.prec);
	}
	
	
	
}