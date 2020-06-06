package decompression;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import data.JPEGCategory;

/**
 * Implementation of inverse zig zag scan.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class InverseZigZag {

	/**
	 * Performs the inverse zigzag scan on a block.
	 * @param input The block.
	 * @return The block after the zigzag scan as a matrix.
	 */
	public static Mat invert(JPEGCategory[] input) {
		double[] data = new double[64];
		int k = 0;
		
		for (int i = 0; i < input.length; i++) {
			double[] subArr = convertToDouble(input[i]);
			for (int j = 0; j < subArr.length; j++) {
				data[k] = subArr[j];
				k++;
			}
		}
		
		double[] newData = { 
			 data[0],  data[1],  data[5],  data[6], data[14], data[15], data[27], data[28], 
			 data[2],  data[4],  data[7], data[13], data[16], data[26], data[29], data[42], 
			 data[3],  data[8], data[12], data[17], data[25], data[30], data[41], data[43], 
			 data[9], data[11], data[18], data[24], data[31], data[40], data[44], data[53], 
			data[10], data[19], data[23], data[32], data[39], data[45], data[52], data[54], 
			data[20], data[22], data[33], data[38], data[46], data[51], data[55], data[60], 
			data[21], data[34], data[37], data[47], data[50], data[56], data[59], data[61], 
			data[35], data[36], data[48], data[49], data[57], data[58], data[62], data[63] 
		};

		Mat result = new Mat(8, 8, CvType.CV_64FC1);
		result.put(0, 0, newData);
		return result;
	}

	/**
	 * Returns a list of elements.
	 * The list contains the coefficient of the element and the 0s encountered.
	 * The coefficient is placed on the last position of the list.
	 * 
	 * @param input The coefficient with its additional data.
	 * @return List of 0s and coefficient at the last position.
	 */
	public static double[] convertToDouble(JPEGCategory input) {

		double[] values = new double[input.getRunlength() + 1];
		for (int i = 0; i < input.getRunlength(); i++) {
			values[i] = 0;
		}
		if (values.length > 1)
			values[values.length - 1] = input.getCoeff();
		else
			values[0] = input.getCoeff();
		return values;
	}

}
