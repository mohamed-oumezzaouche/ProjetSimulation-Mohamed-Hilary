package traitement;

public class evenement2 {
  private int type;
  private double date;
  private int numeroPC;

  public evenement2(double date, int type, int numeroPC) {
    this.type = type;
    this.date = date;
    this.numeroPC = numeroPC;
  }

  public int getType() {
    return this.type;
  }

  public double getDate() {
    return this.date;
  }

  public int getNumeroPC() {
    return this.numeroPC;
  }
}
