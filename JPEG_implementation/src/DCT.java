import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class DCT {
	public static final int DCT_ROWS=0;
	
	public static Mat imageToMat(String fileName) {
		return Imgcodecs.imread(fileName, Imgcodecs.IMREAD_GRAYSCALE);//maybe need imread with some flags so that it is gray-scale
	}
	
	public static List<Mat> divideBlocksDCT(String fileName) {
		Mat mat = imageToMat(fileName);
		
		int rowStart;
		int rowEnd=8;
		int colStart=0;
		int colEnd=8;
		//int size=(int) ((int) Math.ceil(mat.width()/8) *Math.ceil(mat.height()/8));
		//Mat [] concatMatrix=new Mat [size];
		List<Mat>concatMatrix=new ArrayList<Mat>();
		
		for (rowStart=0;rowEnd<=mat.width();rowEnd+=8) {
			for(colStart=0; colEnd<=mat.height(); colEnd+=8) {
				Mat sub=mat.submat(rowStart, rowEnd, colStart, colEnd);
				Mat dct_subMat=dct(sub);
				//System.out.println(dct_subMat.dump());
				concatMatrix.add(dct_subMat);
				colStart+=8;
			}
			colStart=0;
			colEnd=8;
			rowStart+=8;
			
		}
		System.out.println(concatMatrix.toString());
		
		return concatMatrix;
		
	}
	
	public static Mat  dct(Mat mat) {
		
		//Mat mat = imageToMat(fileName);
		Mat m=new Mat();
		mat.convertTo(m, CvType.CV_64FC1);//convert to floating points
		
		//System.out.println(m.type());
		
		Mat dct_mat=new Mat(m.rows(), m.cols(),m.type() );//create output matrix with same size and same type
		
		Mat dct_convert=new Mat();
		dct_mat.convertTo(dct_convert,  CvType.CV_64FC1);//convert also to floating points
		//System.out.println(dct_convert.type());
		
		Core.dct(m, dct_convert);
		//Mat col1 = dct_convert.col(1);
		//System.out.println(col1.dump());
		
		return dct_convert;
	}
}
