package compression;

import decompression.HuffmanDecoder;
import utils.Utils;

public class JPEGCategory {
	
	private double coeff;
	private int cat;		// The category of the coefficient.
	private double prec;	// The precision of the coefficient.
	private int runlength;	// The RLE of the coefficient.
	private boolean endOfBlock;
	
	private static double prev = 0;
	
	/** Constructor. */
	public JPEGCategory(double coeff, int cat, double prec) {
		this.setCoeff(coeff);
		this.setCat(cat);
		this.setPrec(prec);
	}
	public JPEGCategory() {
		
	}
	private static double prevNew=0;
	
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
		if(this.isEndOfBlock()) {
			return "1010";
		}
		int position=(int)this.prec;
		//System.out.println(this.runlength);
		if(this.cat > 0) {
			return HuffmannTable.huffmanJPG[index]+Utils.convertIntToBinary(position, this.cat);
		}
		return HuffmannTable.huffmanJPG[index];
	}
	
	public boolean isEndOfBlock() {
		return endOfBlock;
	}
	public void setEndOfBlock(boolean endOfBlock) {
		this.endOfBlock = endOfBlock;
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
		//this.coeff=HuffmanDecoder.assignCoefficant(this.cat, (int)this.prec);//DC Element
		
		this.coeff=HuffmanDecoder.assignCoefficant(this.cat, (int)this.prec)+prev;
		
		//this.coeff = this.coeff + HuffmanDecoder.getDCprev();
		
		//HuffmanDecoder.setDCprev(this.coeff);
		
		
	}
	
	public void huffmanDecodeAC(String huffmanString) {
		
		//System.out.println(huffmanString);
		
		String special=HuffmannTable.huffmanJPG[150];
				
		if(huffmanString.startsWith(special)) {
			this.runlength=15;
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
					String postSub=huffmanString.substring(digits);
					this.cat= postSub.length();
					this.prec= Integer.parseInt(postSub,2);
					this.runlength =(i - this.cat)/10;
					
//					(rle*10)+cat = index //-cat
//					rle*10 = index -cat // 10
//					rle = (index-cat) / 10
					
					if (this.runlength < 0) {
						System.out.println("prec " + this.getPrec());
						System.out.println("cat " + this.getCat());
						System.out.println("runlength " + this.getRunlength());
						System.out.println("i " + i);
					}
//					System.out.println("index is: "+i);
				}
			}
		}
		
		this.coeff=HuffmanDecoder.assignCoefficant(this.cat, (int)this.prec);
		if (this.runlength < 0)
		System.out.println("COEF " + this.getCoeff());
	}
	
	
	
}