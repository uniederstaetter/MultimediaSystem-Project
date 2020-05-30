package decompression;

import compression.JPEGCategory;

public class HuffmanDecoder {
	
	private static double DCprev=0;
	
	public static double getDCprev() {
		return DCprev;
	}

	public static void setDCprev(double dCprev) {
		DCprev = dCprev;
	}

	public static int assignCoefficient(int cat, double prec) {
		int newPrec = (int) prec;
		int start = (int) (Math.pow(2, cat)-1)*(-1);
		int skip = cat == 1 ? 0 :(int) (Math.pow(2, cat-1)-1)*(-1);
		
		int [] values=getValues(start, skip, cat);
		
		return values[newPrec];
	}
	
	public static int [] getValues(int start, int skip, int cat) {
		int end=Math.abs(start);
		int skipEnd=Math.abs(skip);
		int [] numbers= new int [(int) Math.pow(2, cat)];
		int j=0;
		for (int i=start; i<=end; i++) {
			if(i<skip||i>skipEnd) {
				numbers[j]=i;
				j++;
			}
		}
		//System.out.println("Array " + numbers.length);
		return numbers;
		
	}
}
