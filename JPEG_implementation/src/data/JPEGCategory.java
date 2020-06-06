package data;

import decompression.HuffmanDecoder;
import utils.Utils;

/**
 * Object to simplify the code.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class JPEGCategory {

	private double coeff; // The coefficient.
	private int cat; // The category of the coefficient.
	private double prec; // The precision of the coefficient.
	private int runlength; // The RLE of the coefficient.
	private boolean endOfBlock; // Is EOB?

	/** Constructor. */
	public JPEGCategory(double coeff, int cat, double prec) {
		this.setCoeff(coeff);
		this.setCat(cat);
		this.setPrec(prec);
	}

	/** Constructor. */
	public JPEGCategory() {
	}

	/** @return the coefficient. */
	public double getCoeff() {
		return coeff;
	}

	/**
	 * Sets the coefficient to the given value.
	 * 
	 * @param coeff The coefficient.
	 */
	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}

	/** @return the category of the coefficient. */
	public int getCat() {
		return cat;
	}

	/**
	 * Sets the category of the coefficient to the given value.
	 * 
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
	 * 
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
	 * 
	 * @param runlength The RLE of the coefficient.
	 */
	public void setRunlength(int runlength) {
		this.runlength = runlength;
	}

	/**
	 * Converts the concatenation of the RLE and the category of the coefficient
	 * into its binary representation and returns it as a String.
	 * 
	 * @return The concatenation of the RLE and the category of the coefficient as a
	 *         binary representation.
	 */
	public String convertToBinary() {
		String category = Utils.convertIntToBinary(cat, 4); // Category represented in binary.
		String rle = Utils.convertIntToBinary(runlength, 4); // RLE represented in binary.
		return rle + category;
	}

	/**
	 * Performs the Huffman encoding of a AC element. Adds the precision of the
	 * coefficient at the end as a binary string.
	 * 
	 * @return The Huffman code of the coefficient concatenated with the precision
	 *         as binary string of the coefficient.
	 */
	public String huffmanEncode() {
		int index = this.runlength * 10 + this.cat;

		if (this.isEndOfBlock()) { // EOB
			return "1010";
		}

		int position = (int) this.prec;
		if (this.cat > 0) {
			return HuffmannTable.huffmanAC[index] + Utils.convertIntToBinary(position, this.cat);
		}

		return HuffmannTable.huffmanAC[index];
	}

	/** @return true if EOB. */
	public boolean isEndOfBlock() {
		return endOfBlock;
	}

	/**
	 * Sets the flag to the given value.
	 * 
	 * @param endOfBlock true if EOB.
	 */
	public void setEndOfBlock(boolean endOfBlock) {
		this.endOfBlock = endOfBlock;
	}

	/**
	 * Performs the Huffman encoding of a DC element.
	 * 
	 * @return The Huffman code of the coefficient concatenated with the precision
	 *         as binary string of the coefficient.
	 */
	public String huffmanEncodeDC() {
		int position = (int) this.prec;
		String huffmanString = HuffmannTable.huffmannDC[this.cat] + Utils.convertIntToBinary(position, this.cat);

		return huffmanString;
	}

	/**
	 * Performs the Huffman decoding of a DC element. Iterates over lookup table
	 * from bottom to the top. If the corresponding Huffman code was found we are
	 * able to reconstruct the coefficient, category and precision of the DC
	 * element. We first have to perform a prediction before we can assign the
	 * coefficient.
	 * 
	 * @param huffmanString The Huffman encoded string.
	 */
	public void huffmanDecodeDC(String huffmanString) {

		for (int i = HuffmannTable.huffmannDC.length - 1; i >= 0; i--) {
			int digits = HuffmannTable.huffmannDC[i].length();
			if (digits < huffmanString.length()) {
				String sub = huffmanString.substring(0, digits);
				if (sub.equals(HuffmannTable.huffmannDC[i])) {
					this.cat = i; // The index is the category of the coefficient.
					String postSub = huffmanString.substring(digits); // Remaining string is the precision as binary
																		// string.
					int toDec = Integer.parseInt(postSub, 2); // Convert precision into decimal.
					this.prec = toDec; // Set precision
				}
			}
		}
		// Perform prediction.
		int error = HuffmanDecoder.assignCoefficient(this.cat, this.prec); // Get the error of the current coefficient.
		this.coeff = error + Utils.getDecodedPred(); // Set coefficient.
		Utils.setDecodedPred(this.coeff); // Update prediction.
	}

	/**
	 * Performs the Huffman decoding of an AC element. Same logic as for @see
	 * huffmanDecodeDC but without prediction and other lookup table.
	 * 
	 * @param huffmanString
	 */
	public void huffmanDecodeAC(String huffmanString) {

		String special = HuffmannTable.huffmanAC[150];

		if (huffmanString.startsWith(special)) { // If runlength = 15
			this.runlength = 15;
			this.prec = 0;
			this.cat = 0;
			this.coeff = 0;
			return;
		}

		for (int i = HuffmannTable.huffmanAC.length - 1; i >= 0; i--) {
			int digits = HuffmannTable.huffmanAC[i].length();
			if (digits < huffmanString.length()) {
				String sub = huffmanString.substring(0, digits);
				if (sub.equals(HuffmannTable.huffmanAC[i])) {
					String postSub = huffmanString.substring(digits); // Remaining string is the precision of the
																		// coefficient as binary string.
					this.cat = postSub.length(); // Length of precision is the category.
					this.prec = Integer.parseInt(postSub, 2); // Convert precision into decimal.
					this.runlength = (i - this.cat) / 10; // Calculate runlength.
				}
			}
		}
		this.coeff = HuffmanDecoder.assignCoefficient(this.cat, this.prec); // Set the coefficient.
	}
}