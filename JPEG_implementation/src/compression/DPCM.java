package compression;
import java.util.List;

import org.opencv.core.Mat;

public class DPCM {
	private static double prediction=0;
	private static double levels = 256;
	private static double normRange = 2;
	private static double normDelta = normRange/levels;
	private static double offSet;
	public static double getNormDelta() {
		return normDelta;
	}
	//getter and setter
	public static double getPredict() {
		return prediction;
	}
	public static void setPredict(double predictNew) {
		prediction=predictNew;
	}
	
	// to be called only once, to determine the range of errors we might encounter
	public static double getRange(List<double []> arr) {
		double minError=minMat(arr);
		double maxError=maxMat(arr);
		offSet=minError;
		return (maxError - minError);
	}
	
	public static double getOffSet() {
		return offSet;
	}

	public static double getLevels() {
		return levels;
	}
	// computes the errors, normalizes them in the [0, 2] range, then quantizes them and returns the correspondent level (using a nonlinear approach)
	public static double quantiseError(double coeff, double range) {
		//get the actual value of my error
		double error=Math.abs(coeff-getPredict());
//		System.out.println("Error = " + error);
//		System.out.println("Normalized Range = " + normRange);
//		System.out.println("My range = " + range);
		//predictor is updated
		setPredict(coeff);
		System.out.println("usual pred is: "+getPredict());
		//error is normalized, but on the [0,2] range, for convenience
		double normalizedError = (normRange*error)/range;
		//System.out.println("Normalized Error = " + normalizedError);
		//Get our actual level: first we compute how many deltas are in it, then round to the minimal distance/level
		double myLevel = Math.round(normalizedError/normDelta);
		//System.out.println("Actual level = " + myLevel);
		//Adjust for non linearity
		double result = -1;
		if (myLevel == 0)
			result = 1;
		else if (myLevel == 256)
			result = 255;
		else if (myLevel % 2 == 0)
			result = myLevel +1;
		else
			result = myLevel;
		return result;
		
	}
	
	public static double getNormRange() {
		return normRange;
	}
	// Servant methods to compute the min/max DC value among all blocks
	public static double minMat(List<double []> arr) {
		double min = Double.MAX_VALUE;
 		for (double [] a : arr) {
 			min = a[0] < min ? a[0] :min;
  		}
 		return min;
		
	}
	public static double maxMat(List<double []> arr) {
		double max = Double.MIN_VALUE;
 		for (double [] a : arr) {
 			max = a[0] > max ? a[0] :max;
  		}
 		return max;
		
	}
	
	//how to encode the DC coefficient
	public static String encode(double level) {
		//Turn it into an object, so we can do fancy things with it
		Double myObjLevel = new Double(level);
		//Turn it into an int in an elegant way, then into its binary representation
		String result = Integer.toBinaryString(myObjLevel.intValue());
		//Add zeroes, in order to always have 8bit words
		if (result.length() < 8) {
			int zerol = 8 - result.length();
			String addzero = "";
			for (int i = 0; i < zerol; i++) {
				addzero += "0";
			}
			result = addzero + result;
		}
		return result;
	}

}
