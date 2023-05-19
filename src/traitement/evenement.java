package traitement;

public class evenement {
	private int type;
	private double date;
	private int numero;
	
	public evenement(double date,int type) {
		this.type = type;
		this.date = date;
		//this.numero = Math.random() * ( 1 - 0 );
	}
	
	public int getType() {
		return this.type;
	}
	
	public double getDate() {
		return this.date;
	}

}
