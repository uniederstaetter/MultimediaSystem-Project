package decompression;

import compression.DPCM;

public class ReverseDPCM {
	static double prediction=0;

	public static double reverseDPCM(String DCElement, double range, double offSet) {
		//System.out.println(prediction);
		int binToDec=Integer.parseInt(DCElement, 2);//gets back the level of the error
		
		double normalisedError=binToDec*DPCM.getNormDelta();
		
		
		double error=Math.round(normalisedError*range/DPCM.getNormRange());
	
		double coeff=error+prediction;
		
		
		coeff+=offSet;
		System.out.println("Inverse "+coeff);
		
		
		prediction=coeff;
		
		return coeff;
	}
}
