package traitement;

public class evenement {
	private int type;
	private double date;
	
	public evenement(double date,int type) {
		this.type = type;
		this.date = date;
	}
	
	public int getType() {
		return this.type;
	}
	
	public double getDate() {
		return this.date;
	}

}
