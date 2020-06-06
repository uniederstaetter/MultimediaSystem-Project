package compression;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

/**
 * Implementation of the zig zag scan.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class ZigZag {

	/**
	 * Performs the zigzag scan on each block.
	 * 
	 * @param mat The list of blocks.
	 * @return The list of blocks after zigzag scan as an array of doubles.
	 */
	public static List<double[]> zigZag(List<Mat> mat) {
		List<double[]> zigZagResult = new ArrayList<>();

		for (Mat m : mat)
			zigZagResult.add(zigZagMatrix(m));

		return zigZagResult;
	}

	/**
	 * Performs the zigzag scan on a single block.
	 * @param mat The single block.
	 * @return The block after the zigzag scan.
	 */
	public static double[] zigZagMatrix(Mat mat) {

		double[] result = { 
			mat.get(0, 0)[0], mat.get(0, 1)[0], mat.get(1, 0)[0], mat.get(2, 0)[0], mat.get(1, 1)[0], mat.get(0, 2)[0], mat.get(0, 3)[0], mat.get(1, 2)[0], 
			mat.get(2, 1)[0], mat.get(3, 0)[0], mat.get(4, 0)[0], mat.get(3, 1)[0], mat.get(2, 2)[0], mat.get(1, 3)[0], mat.get(0, 4)[0], mat.get(0, 5)[0], 
			mat.get(1, 4)[0], mat.get(2, 3)[0], mat.get(3, 2)[0], mat.get(4, 1)[0], mat.get(5, 0)[0], mat.get(6, 0)[0], mat.get(5, 1)[0], mat.get(4, 2)[0], 
			mat.get(3, 3)[0], mat.get(2, 4)[0], mat.get(1, 5)[0], mat.get(0, 6)[0], mat.get(0, 7)[0], mat.get(1, 6)[0], mat.get(2, 5)[0], mat.get(3, 4)[0], 
			mat.get(4, 3)[0], mat.get(5, 2)[0], mat.get(6, 1)[0], mat.get(7, 0)[0], mat.get(7, 1)[0], mat.get(6, 2)[0], mat.get(5, 3)[0], mat.get(4, 4)[0],
			mat.get(3, 5)[0], mat.get(2, 6)[0], mat.get(1, 7)[0], mat.get(2, 7)[0], mat.get(3, 6)[0], mat.get(4, 5)[0], mat.get(5, 4)[0], mat.get(6, 3)[0], 
			mat.get(7, 2)[0], mat.get(7, 3)[0], mat.get(6, 4)[0], mat.get(5, 5)[0], mat.get(4, 6)[0], mat.get(3, 7)[0], mat.get(4, 7)[0], mat.get(5, 6)[0], 
			mat.get(6, 5)[0], mat.get(7, 4)[0], mat.get(7, 5)[0], mat.get(6, 6)[0], mat.get(5, 7)[0], mat.get(6, 7)[0], mat.get(7, 6)[0], mat.get(7, 7)[0] 
		};
		return result;
	}
}
