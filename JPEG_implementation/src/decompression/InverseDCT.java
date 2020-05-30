package decompression;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class InverseDCT {
	
	public static Mat inverseDCT(Mat mat) {

		// Convert the values of the input matrix into floating points and store them into a new matrix.
		Mat m = new Mat();
		mat.convertTo(m, CvType.CV_64FC1); 
		
		// Create an output matrix with same size and same type of the converted input matrix.
		Mat dct_mat = new Mat(m.rows(), m.cols(), m.type());

		// Convert the values of dct_mat into floating points and store them into the matrix dct_convert.
		Mat dct_convert = new Mat();
		dct_mat.convertTo(dct_convert, CvType.CV_64FC1);// convert also to floating points

		// Perform the forward dct on matrix m an store it in the matrix dct_converted.
		Core.dct(m, dct_convert, Core.DCT_INVERSE);

		return dct_convert;
	}

}
