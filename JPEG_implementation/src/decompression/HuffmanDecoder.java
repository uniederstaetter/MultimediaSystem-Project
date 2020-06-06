package decompression;

/**
 * Implementation of Huffman decoding.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class HuffmanDecoder {

	private static double DCprev = 0;	// The previous DC element.

	/** @return The previous DC element. */
	public static double getDCprev() {
		return DCprev;
	}

	/**
	 * Set the previous DC element to the given value.
	 * @param dCprev The previous DC element.
	 */
	public static void setDCprev(double dCprev) {
		DCprev = dCprev;
	}

	/**
	 * Calculates the coefficient by the given category and the precision.
	 * @param cat The category of the coefficient.
	 * @param prec The precision of the coefficient.
	 * @return The coefficient.
	 */
	public static int assignCoefficient(int cat, double prec) {
		int newPrec = (int) prec;

		int[] values = getValues(cat);	// The values of a specific category.

		return values[newPrec];	// The precision is the index of the coefficient.
	}

	/**
	 * Returns all the possible coefficients of a category.
	 * @param cat The category.
	 * @return All the possible coefficient of a category.
	 */
	public static int[] getValues(int cat) {
		int start = (int) (Math.pow(2, cat) - 1) * (-1);	// Calculates the first coefficient of the given category.
		int skip = cat == 1 ? 0 : (int) (Math.pow(2, cat - 1) - 1) * (-1);	// Calculates the first coefficient of the previous category.
		int end = Math.abs(start);	// The last coefficient of the given category.
		int skipEnd = Math.abs(skip);	// The last coefficient of the previous category.
		int[] numbers = new int[(int) Math.pow(2, cat)];	// Number of coefficients is always 2^category.
		int j = 0;
		
		for (int i = start; i <= end; i++) {
			if (i < skip || i > skipEnd) {	// Skip the elements of the previous category.
				numbers[j] = i;
				j++;
			}
		}
		return numbers;
	}
}
