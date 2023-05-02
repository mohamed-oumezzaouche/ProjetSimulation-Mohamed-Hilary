package traitement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class echeancier {
	
	public static LinkedList<evenement> eList = new LinkedList<evenement>();
	public static int EV_ARRIVEE  = 100;
	public static int EV_DEB_SERV = 200;
	public static int EV_FIN_SERV = 300;
	public static int NbrePcLibres = 10;
	public static int N = 0;
	public static int mu = 1;
	public static int cpt = 0;
	public static int T = 0;
	public static int last_T = 0;
	public static int l = 1000;
	public static double h = 0.001;
	public static double NewValue;
	public static double DebRectangle;
	public static int S_tpsAttente = 0;
	
	public int addEvent(double date, int type) {
		int i = 0;
		if(eList.isEmpty() || eList.get(0).getDate() > date) {
			eList.addFirst(new evenement(date,type));
			return 0;
		}else {
			for(evenement e : eList) {
				if(eList.get(i).getDate() > date) {
					eList.add(i, new evenement(date,type));
					return 0;
				}
				i++;
			}
		}
		eList.addLast(new evenement(date,type));
		return 0;
	}
	
	public static evenement premier() {
		evenement e = eList.getFirst();
		eList.remove(0);
		return e;
	}
	
	public static void afficher() {
		System.out.println("#");
		for(evenement e : eList) {
			System.out.println(e.getDate() + " " + e.getType());
			System.out.println(System.getProperty("line.separator"));
		}
		System.out.println("#");
		System.out.println(System.getProperty("line.separator"));
	}
	
	public static double exp(double d) {
		double r = Math.random() * ( 1 - 0 );
		return - (Math.log(1 - r)) / d;
	}
	
	public static double mode1(double valeur) {
        int i = 0;
        cpt = 0;
        N = 0;
        NbrePcLibres = 10;
        DebRectangle = 0;
        NewValue = 0;
        echeancier e = new echeancier();
		e.addEvent(0.0, EV_ARRIVEE);
		while(cpt <  1000000 && i < l) {
			evenement premier = premier();
			T = (int) premier.getDate();
			traitement1(premier,e,valeur);
			NewValue = (double) S_tpsAttente / (double) T;
			if(Math.abs(DebRectangle - NewValue) > DebRectangle*(h/2)) {
				DebRectangle = NewValue;
				i = 0;
			}else {
				i++;
			}
		}
		return (double) S_tpsAttente / (double) T;
	}
	
	private static void traitement1(evenement premier, echeancier e, double valeur) {
		if(premier.getType() == EV_ARRIVEE) {
			if(N == 0) {
				e.addEvent(T, EV_DEB_SERV);
			}
			if (T > last_T) {
	            last_T = T;
	        }
			N++;
			e.addEvent(T+exp(valeur), EV_ARRIVEE);
		}else if(premier.getType() == EV_DEB_SERV) {
			double tempsAttente = T - premier.getDate(); // calcul du temps d'attente pour le client qui commence à être servi
			S_tpsAttente += tempsAttente; // ajout du temps d'attente pour le client dans la somme des temps d'attente
			NbrePcLibres --;
			if(T > last_T) {
				S_tpsAttente += T - last_T;
				last_T = T;
			}
			e.addEvent(T+exp(mu), EV_FIN_SERV);
		}else if(premier.getType() == EV_FIN_SERV){
			cpt++;
			NbrePcLibres ++;
			N--;
			if(N > 0 && NbrePcLibres > 0) {
				e.addEvent(T, EV_DEB_SERV);
			}
		}		
	}

	public static void main(String[] args) throws IOException {
		String nomFichier = "lambda.txt";
		File fichier = new File(nomFichier);
		FileReader lecteurFichier = new FileReader(fichier);
		BufferedReader lecteurBuffer = new BufferedReader(lecteurFichier);
		FileWriter ecrivainFichier = new FileWriter("resultats.txt");
		String ligne;
        while ((ligne = lecteurBuffer.readLine()) != null) {
        	double valeur = Double.parseDouble(ligne);
        	double result = mode1(valeur);
        	ecrivainFichier.write(valeur + " ");
        	System.out.println(cpt);
        	if(cpt >= 1000000) {
        		ecrivainFichier.write("-1 ");
        	}else {
        		ecrivainFichier.write(result + "");
        	}
        	ecrivainFichier.write(System.lineSeparator());
        }
        lecteurBuffer.close();
        lecteurFichier.close();
        ecrivainFichier.close();
	}

}
