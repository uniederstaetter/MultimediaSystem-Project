package compression;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import decompression.HuffmanDecoder;

public class Test {
	
	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//		double [] testArray= {
//				1,2,3,4,5,6,7,8,
//				9,10,11,12,13,14,15,16,
//				17,18,19,20,21,22,23,24,
//				25,26,27,28,29,30,31,32,
//				33,34,35,36,37,38,39,40,
//				41,42,43,44,45,46,47,48,49,50,
//				51,52,53,54,55,56,57,58,59,60,
//				61,62,63,64
//		};
//		Mat mat=new Mat(8,8, CvType.CV_64FC1);
//		mat.put(0, 0,testArray);
//		
//		System.out.println(mat.dump());
//		
//		double [] testDouble=ZigZag.zigZagMatrix(mat);
//		
//		for (int i=0; i<testDouble.length; i++) {
//			System.out.print(testDouble[i]+" ");
//		}
		
		
//		int [] testarray=HuffmanDecoder.getValues(-3, -1, 2);
//		for(int t: testarray) {
//			System.out.println(t);
//		}
		
//		int coeff=HuffmanDecoder.assignCoefficant(3, 3);
//		System.out.println(coeff);
		
		String test = "1;2;3;4;5";
		String[] test2 = test.split(";");
		System.out.println(test2.length);
		
}

}
