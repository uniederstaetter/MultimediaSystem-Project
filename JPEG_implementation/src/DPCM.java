import java.util.List;

import org.opencv.core.Mat;

public class DPCM {
	private static double prediction=0;
	private static double levels = 256;
	
	//getter and setter
	public static double getPredict() {
		return prediction;
	}
	public static void setPredict(double predictNew) {
		prediction=predictNew;
	}
	
	// to be called only once, to determine the range of errors we might encounter
	public static double prediction(List<double []> arr) {
		double minError=minMat(arr);
		double maxError=maxMat(arr);
		return (maxError - minError)/levels;
	}
	
	// quantizes the errors in a uniform way given the delta computed with prediction() and updates the predictor
	public static double quantiseError(double coeff, double delta) {
		double error=coeff-getPredict();
		double myLevel = Math.ceil(error/delta);
		setPredict(coeff);
		return myLevel;
		
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
		Double myObjLevel = new Double(Math.abs(level));
		//turn it into int in an elegant way, then its binary representation
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
