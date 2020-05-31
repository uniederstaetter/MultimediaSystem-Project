package compression;

import java.util.ArrayList;

import data.JPEGCategory;

/**
 * Implementation of Huffman encoding algorithm.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 */
public class HuffmanEncoder {
	
	private static double prediction = 0; // The prediction of the DC element.

	/** @return The prediction of the DC element. */
	public static double getPrediction() {
		return prediction;
	}

	/**
	 * Sets the prediction to the given value.
	 * @param value The new Prediction.
	 */
	public static void setPrediction(double value) {
		prediction = value;
	}

	/**
	 * Returns a JPEGCategory for the given coefficient.
	 * 
	 * @param coeff The coefficient.
	 * @return JPEGCategory depending on the coefficient
	 */
	public static JPEGCategory assignCategory(double coeff) {
		int myCoeff = Math.abs((int) coeff);

		if (myCoeff == 0) {
			return new JPEGCategory(coeff, 0, 0);
		}
		if (myCoeff <= 1) { // -1, 1
			double position = getPrecision(coeff, 1);
			return new JPEGCategory(coeff, 1, position);
		}
		if (myCoeff <= 3) { // -3, -2, 2, 3
			double position = getPrecision(coeff, 3);
			return new JPEGCategory(coeff, 2, position);
		}
		if (myCoeff <= 7) { // -7, -6, -5, -4, 4, 5 ,6 ,7
			double position = getPrecision(coeff, 7);
			return new JPEGCategory(coeff, 3, position);
		}
		if (myCoeff <= 15) {
			double position = getPrecision(coeff, 15);
			return new JPEGCategory(coeff, 4, position);
		}
		if (myCoeff <= 31) {
			double position = getPrecision(coeff, 31);
			return new JPEGCategory(coeff, 5, position);
		}
		if (myCoeff <= 63) {
			double position = getPrecision(coeff, 63);
			return new JPEGCategory(coeff, 6, position);
		}
		if (myCoeff <= 127) {
			double position = getPrecision(coeff, 127);
			return new JPEGCategory(coeff, 7, position);
		}
		if (myCoeff <= 255) {
			double position = getPrecision(coeff, 255);
			return new JPEGCategory(coeff, 8, position);
		}
		if (myCoeff <= 511) {
			double position = getPrecision(coeff, 511);
			return new JPEGCategory(coeff, 9, position);
		}
		if (myCoeff <= 1023) {
			double position = getPrecision(coeff, 1023);
			return new JPEGCategory(coeff, 10, position);
		}
		return null;
	}

	/**
	 * Evaluates the precision of the coefficient in a category.
	 * 
	 * @param coeff The coefficient.
	 * @param limit The limit of the category.
	 * @return The precision of the coefficient.
	 */
	public static double getPrecision(double coeff, int limit) {
		double sign = Math.signum(coeff);
		double position;
		if (sign > 0) {
			position = Math.abs(coeff);
		} else {
			position = limit - Math.abs(coeff);
		}
		return position;
	}

	/**
	 * Calculates the runlength of each coefficient and creates a 
	 * JPEGCategory object with the calculated runlength and the coefficient.
	 *  
	 * @param coeffs The list of coefficients.
	 * @return The list of JPEGCategories including coefficient and calculated runlength.
	 */
	public static ArrayList<JPEGCategory> RLE(double[] coeffs) {
		ArrayList<JPEGCategory> result = new ArrayList<>();
		int c = 0;	// Number of 0s encountered.
		for (int i = 1; i < coeffs.length; i++) { // Starting from 1, because we want to exclude the DC element!
			if (coeffs[i] == 0) {
				if (c < 15) {
					if (i == coeffs.length - 1) { // Special case for reaching end of the list.
						JPEGCategory newObj = new JPEGCategory(coeffs[i], 0, 0);
						newObj.setRunlength(c);
						result.add(newObj);
					}
					c++;
				} else { // Adding new JPEGCategory object if max value of countable 0s encountered. (15)
					JPEGCategory newObj = new JPEGCategory(coeffs[i], 0, 0);
					newObj.setRunlength(15);
					result.add(newObj);
					c = 0;
				}
			} else { // Adding new JPEGCategory object if coefficient different from 0 encountered.
				JPEGCategory newObj = assignCategory(coeffs[i]);
				newObj.setRunlength(c);
				result.add(newObj);
				c = 0;
			}
		}
		// Adding additional element to specify end of block.
		JPEGCategory EOB = new JPEGCategory(0, 0, 0);
		EOB.setEndOfBlock(true);
		EOB.setRunlength(0);
		result.add(EOB);
		return result;
	}

	/**
	 * Created a JPEGCategory object for the DC element of a block.
	 * @param coeff The coefficient.
	 * @return The JPEGCategory object of the DC element.
	 */
	public static JPEGCategory RLEDC(double coeff) {
		// Calculate error
		double error = coeff - getPrediction();
		// Assign the category of the error.
		JPEGCategory dcCat = assignCategory(error);
		// Update prediction.
		setPrediction(coeff);
		return dcCat;
	}
}
