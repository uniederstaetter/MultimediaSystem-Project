import java.util.List;

import org.opencv.core.Mat;

public class DPCM {
	double prediction=0;
	
	
	public double getPredict() {
		return this.prediction;
	}
	public void setPredict(double predictNew) {
		this.prediction=predictNew;
	}
	
	public void prediction(List<double []> arr) {
		double minError=minMat(arr);
		double maxError=maxMat(arr);
		
		
	}
	
	public void quantiseError(double coeff) {
		double error=coeff-this.getPredict();
		
		
	}
	
	public double minMat(List<double []> arr) {
		double min = Double.MAX_VALUE;
 		for (double [] a : arr) {
 			min = a[0] < min ? a[0] :min;
  		}
 		return min;
		
	}
	public double maxMat(List<double []> arr) {
		double max = Double.MIN_VALUE;
 		for (double [] a : arr) {
 			max = a[0] > max ? a[0] :max;
  		}
 		return max;
		
	}
	//how to encode the DC coefficant
	public void encode() {
		
	}

}
