package utils;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Utils {

	

	private static double decodedPred = 0;

	public static double getDecodedPred() {
		return decodedPred;
	}

	public static void setDecodedPred(double decodedPred) {
		Utils.decodedPred = decodedPred;
		System.out.println(decodedPred);
	}

	/**
	 * Takes a integer value as input and converts it to an String representing
	 * the binary number of the value with a given length.
	 * Example:
	 * value = 3 and digits = 4 the result is '0011'.
	 * @param value The value to be converted.
	 * @param digits The number of digits of the result.
	 * @return The converted value to binary as a String.
	 */
	public static String convertIntToBinary(int value, int digits) {
		String ret = Integer.toBinaryString(value);
		
		if (ret.length() < digits) {
			int preLength = digits - ret.length();	// Number of 0's to be added.
			String pre = "";
			
			// Creating the predicate.
			for (int i = 0; i < preLength; i++) 
				pre += "0";
			
			ret = pre + ret; // Concatenation of the predicate and the value converted to binary.
		}
		
		return ret;
	}
	
	/**
	 * Converts an image to a matrix represented in gray scale.
	 * If there is an unsupported operation error null will be returned.
	 * @param path The path of the image.
	 * @return The matrix representing the image as gray scale image or null in case of an error.
	 */
	public static Mat imgToMat(String path) {
		Mat ret;
		try {
			// Reads the image as gray scale image.
			ret = Imgcodecs.imread(path, Imgcodecs.IMREAD_GRAYSCALE);
			
		} catch (UnsupportedOperationException e) {
			// Let the user know that there has been an error.
			System.err.println("Unsupported operation in imgToMat!");
			return null;
		}
		return ret;
	}
	
	public static double[] matToArr(Mat mat) {
		double[] res = new double[mat.cols()*mat.rows()];
		int k = 0;
		for (int i = 0; i < mat.rows(); i++) {
			for (int j =0; j< mat.cols(); j++) {
				res[k] = mat.get(i, j)[0];
				k++;
			}
		}
		return res;
	}
}