import java.awt.Image;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class JPEG_compr {
	public static final int DCT_ROWS=0;
	
	public static Mat imageToMat(String fileName) {
		return Imgcodecs.imread(fileName);
	}
	
	public static void dct(String fileName) {
		
		Mat mat = imageToMat(fileName);
		Mat m=new Mat();
		mat.convertTo(m, CvType.CV_64FC1);
		System.out.println(m.type());
		//Mat col1 = m.col(1);
		//System.out.println(col1.dump());
		
		Mat dct_mat=new Mat(m.rows(), m.cols(),m.type() );
		//System.out.println(dct_mat.type());
		
		//The problem is that it should get as src=m a floating point array-but it does not.!
		Core.dct(m, dct_mat,0);
		
		System.out.println(dct_mat.type());
		
	}
}
