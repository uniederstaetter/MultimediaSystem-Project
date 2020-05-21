import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.*;

public class ZigZag {
	
	//need first to rearange the matrix back to its orginal shape, but does not work!!!!
	public static Mat zigZag(List<Mat> mat, int width, int height) {//gets each block 8*8 block of DCT
	

		Mat destMatrix=new Mat();
		
		Core.hconcat(mat, destMatrix);
		
		int rowStart;
		int rowEnd=8;
		int colStart=0;
		int colEnd=8;
		
		/*
		for (rowStart=0;rowEnd<=width;rowEnd+=8) {
			List<Mat> src = Arrays.asList(mat.get(rowStart));
			Core.hconcat(src, destMatrix);
			Mat temp=destMatrix;
			rowStart+=8;
			rowEnd+=8;
		}
			//for(colStart=0; colEnd<=mat.height(); colEnd+=8) {
		
	
		*/
		return destMatrix;
	}

			
			
		//System.out.println(concatanetMatrix.dump());
		//zigZagMatrix(concatanetMatrix, concatanetMatrix.height(), concatanetMatrix.width());
		
	
		
	
	
	
	public static void zigZagMatrix(Mat mat, int n, int m) {
		int row=0, col=0;
		
		//true if row needs to be incremented
		//false if col needs to be incremented.
		boolean row_inc=false;
		
		int mn=Math.min(m,  n);//lower half zig-zag pattern
		
	}
	

}
