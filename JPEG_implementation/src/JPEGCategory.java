
public class JPEGCategory {
	
	private int cat;
	private double prec;
	private int runlength;
	
	public JPEGCategory(int cat, double prec) {
		this.setCat(cat);
		this.setPrec(prec);
	}
	
	public int getCat() {
		return cat;
	}
	public void setCat(int cat) {
		this.cat = cat;
	}
	public double getPrec() {
		return prec;
	}
	public void setPrec(double prec) {
		this.prec = prec;
	}

	public int getRunlength() {
		return runlength;
	}

	public void setRunlength(int runlength) {
		this.runlength = runlength;
	}
	
	public String convertToBinary() {
		String ctg = Integer.toBinaryString(cat);
		if (ctg.length() < 4) {
			int zerol = 4 - ctg.length();
			String addzero = "";
			for (int i = 0; i < zerol; i++) {
				addzero += "0";
			}
			ctg = addzero + ctg;
		}
		String rle = Integer.toBinaryString(runlength);
		if (rle.length() < 4) {
			int zerol = 4 - rle.length();
			String addzero = "";
			for (int i = 0; i < zerol; i++) {
				addzero += "0";
			}
			rle = addzero + rle;
		}
		return rle + ctg;
		
	}

}
