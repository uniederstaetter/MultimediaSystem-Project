package compression;
import java.util.ArrayList;

public class HuffmanEncoder {
	private static double DCprev=0;
	
	public static JPEGCategory assignCategory(double coeff) {
		Double doubleObj = new Double(coeff);
		int myCoeff = Math.abs(doubleObj.intValue());
		
		if (myCoeff <=1) { // -1, 1
			double position = getPosition(coeff, 1);
			return new JPEGCategory(coeff,1, position);
		}
		if (myCoeff <=3) { // -3, -2, 2, 3
			double position = getPosition(coeff, 3);
			return new JPEGCategory(coeff,2, position);
		}
		if (myCoeff <=7) { // -7, -6, -5, -4, 4, 5 ,6 ,7
			double position = getPosition(coeff, 7);
			return new JPEGCategory(coeff,3, position);
		}
		if (myCoeff <=15) {
			double position = getPosition(coeff, 15);
			return new JPEGCategory(coeff,4, position);
		}
		if (myCoeff <=31) {
			double position = getPosition(coeff, 31);
			return new JPEGCategory(coeff,5, position);
		}
		if (myCoeff <=63) {
			double position = getPosition(coeff, 63);
			return new JPEGCategory(coeff,6, position);
		}
		if (myCoeff <=127) {
			double position = getPosition(coeff, 127);
			return new JPEGCategory(coeff,7, position);
		}
		if (myCoeff <=255) {
			double position = getPosition(coeff, 255);
			return new JPEGCategory(coeff,8, position);
		}
		if (myCoeff <=511) {
			double position = getPosition(coeff, 511);
			return new JPEGCategory(coeff,9, position);
		}
		if (myCoeff <=1023) {
			double position = getPosition(coeff, 1023);
			return new JPEGCategory(coeff,10, position);
		}	
		return null;
	}
	
	public String convertToBinary(int cat) {
		String result = Integer.toBinaryString(cat);
		if (result.length() < 4) {
			int zerol = 4 - result.length();
			String addzero = "";
			for (int i = 0; i < zerol; i++) {
				addzero += "0";
			}
			result = addzero + result;
		}
		return result;
	}
	
	public static double getPosition(double coeff, int limit) {
		double sign = Math.signum(coeff);
		double position;
		if (sign > 0) {
			position = Math.abs(coeff);
		} else {
			position = limit - Math.abs(coeff);
		}
		return position;
	}
	
	private static int count = 0; 
	
	public static ArrayList<JPEGCategory> RLE(double[] arr) {
		ArrayList<JPEGCategory> result = new ArrayList<>();
		int c = 0;
		for (int i = 1; i < arr.length; i++) {				// Starting from 1, because we want to exclude the DC element!
			if (arr[i] == 0) {
				if (c < 15) {
					if(i==arr.length-1) {
						JPEGCategory newObj = new JPEGCategory(arr[i],0, 0);
						newObj.setRunlength(c);
						result.add(newObj);
						//TODO: add codes to huffmann table
					}
					c++;	
				} else {
					JPEGCategory newObj = new JPEGCategory(arr[i],0, 0);
					newObj.setRunlength(15);
					result.add(newObj);
					c = 0;
				}	
			} else {
				JPEGCategory newObj = assignCategory(arr[i]);
				newObj.setRunlength(c);
				result.add(newObj);
				c = 0;
			}
		}
		count++;
		JPEGCategory EOB = new JPEGCategory(0,0, 0);
		EOB.setEndOfBlock(true);
		EOB.setRunlength(0);
		result.add(EOB);
		return result;
	}
	
	public static int getCOunter() {
		return count;
	}
	
	public static JPEGCategory RLEDC(double DCElement) {
		
		double DCdiff=DCElement-DCprev;
		JPEGCategory dcCat=assignCategory(DCdiff);
		DCprev=DCElement;

		return dcCat;
	}
	
	
}
