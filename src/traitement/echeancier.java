package traitement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class echeancier {

  public static LinkedList<evenement> eList = new LinkedList<evenement>();
  public static LinkedList<evenement2> eList2 = new LinkedList<evenement2>();
  public static int EV_ARRIVEE  = 100;
  public static int EV_DEB_SERV = 200;
  public static int EV_FIN_SERV = 300;
  public static int NbrePcLibres = 10;
  public static int nbreServeurs = 10;
  public static int N = 0;
  public static int mu = 1;
  public static int cpt = 0;
  public static double T = 0;
  public static double last_T = 0;
  public static int l = 1000;
  public static double h = 0.001;
  public static double NewValue;
  public static double DebRectangle;
  public static double S_tpsAttente = 0;
  public static double S_clients = 0;
  public static int[] tableau = new int[10];
  private static int[] tableau2 = new int[10];

  public static void writeLambda(FileWriter fw) throws IOException {
    BigDecimal increment = new BigDecimal("0.01");
    BigDecimal limit = new BigDecimal("10.0");

    BigDecimal i = BigDecimal.ZERO;
    while (i.compareTo(limit) <= 0) {
      fw.write(i.toString());
      fw.write(System.lineSeparator());
      fw.flush();
      i = i.add(increment);
    }
  }

  public echeancier() {
    for (int i = 0; i < tableau.length; i++) {
      tableau[i] = 0;
    }
    for (int i = 0; i < tableau2.length; i++) {
      tableau2[i] = 0;
    }
  }

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

  public int addEvent2(double date, int type, int numeroPC) {
    int i = 0;
    if(eList2.isEmpty() || eList2.get(0).getDate() > date) {
      eList2.addFirst(new evenement2(date,type,numeroPC));
      return 0;
    }else {
      for(evenement2 e : eList2) {
        if(eList2.get(i).getDate() > date) {
          eList2.add(i, new evenement2(date,type,numeroPC));
          return 0;
        }
        i++;
      }
    }
    eList2.addLast(new evenement2(date,type,numeroPC));
    return 0;
  }

  public static evenement2 premier2() {
    evenement2 e = eList2.getFirst();
    eList2.remove(0);
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
    return - (Math.log(r)) / d;
  }

  public static int RandomPC() {
    Random random = new Random();
    int nombreAleatoire = random.nextInt(10);
    return nombreAleatoire;
  }

  public static int min() {
    int minimum = tableau2[0]; // Supposons que le premier ?l?ment est le minimum initial

    for (int i = 1; i < tableau2.length; i++) {
      if (tableau2[i] < minimum) {
        minimum = tableau2[i]; // Met ? jour le minimum si on trouve un ?l?ment plus petit
      }
    }
    return minimum;
  }

  public static double mode1(double valeur) {
    int i = 0;
    cpt = 0;
    N = 0;
    NbrePcLibres = 10;
    DebRectangle = 0;
    NewValue = 0;
    S_clients = 0;
    last_T = 0;
    echeancier e = new echeancier();
    e.addEvent(0.0, EV_ARRIVEE);
    while(cpt <  1000000 && i < l) {
      evenement premier = premier();
      T = premier.getDate();
      traitement1(premier,e,valeur);
      NewValue = S_clients / T;
      if(Math.abs(DebRectangle - NewValue) > DebRectangle*(h/2)) {
        DebRectangle = NewValue;
        i = 0;
      }else {
        i++;
      }
    }
    return (Math.abs(NewValue) / valeur) - 1;
  }

  public static double mode2(double valeur) {
    int i = 0;
    cpt = 0;
    N = 0;
    DebRectangle = 0;
    NewValue = 0;
    S_clients = 0;
    last_T = 0;
    NbrePcLibres = 10;
    echeancier e = new echeancier();
    e.addEvent2(0.0, EV_ARRIVEE,RandomPC());
    while(cpt <  1000000 && i < l) {
      evenement2 premier = premier2();
      T = premier.getDate();
      traitement2(premier,e,valeur);
      NewValue = S_clients / T;
      if(Math.abs(DebRectangle - NewValue) > DebRectangle*(h/2)) {
        DebRectangle = NewValue;
        i = 0;
      }else {
        i++;
      }
    }
    return (Math.abs(NewValue) / valeur) - 1;

  }

  public static double mode3(double valeur) {
    int i = 0;
    cpt = 0;
    N = 0;
    DebRectangle = 0;
    NewValue = 0;
    S_clients = 0;
    last_T = 0;
    echeancier e = new echeancier();
    e.addEvent2(0.0, EV_ARRIVEE,RandomPC());
    tableau2[RandomPC()]++;
    while(cpt <  1000000 && i < l) {
      evenement2 premier = premier2();
      T = premier.getDate();
      traitement3(premier,e,valeur);
      NewValue = S_clients / T;
      if(Math.abs(DebRectangle - NewValue) > DebRectangle*(h/2)) {
        DebRectangle = NewValue;
        i = 0;
      }else {
        i++;
      }
    }
    return (Math.abs(NewValue) / valeur) - 1;

  }

  private static void traitement1(evenement premier, echeancier e, double valeur) {
    if(premier.getType() == EV_ARRIVEE) {
      if(NbrePcLibres > 0) {
        e.addEvent(T, EV_DEB_SERV);
      }
      /**if (T > last_T) {
       if(N - nbreServeurs > 0) {
       S_tpsAttente += (T - last_T)*(N - nbreServeurs);
       }
       last_T = T;
       }**/
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      e.addEvent(T+exp(valeur), EV_ARRIVEE);
      N++;
    }else if(premier.getType() == EV_DEB_SERV) {
      /**S_tpsAttente += T - last_T;**/
      NbrePcLibres --;
      e.addEvent(T+exp(mu), EV_FIN_SERV);
    }else if(premier.getType() == EV_FIN_SERV){
      cpt++;
      NbrePcLibres ++;
      if(N - (10 - NbrePcLibres) > 0) {
        e.addEvent(T, EV_DEB_SERV);
      }
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      N--;
    }
  }

  private static void traitement2(evenement2 premier, echeancier e, double valeur) {
    if(premier.getType() == EV_ARRIVEE) {
      if(tableau[premier.getNumeroPC()] == 0) {
        e.addEvent2(T, EV_DEB_SERV,premier.getNumeroPC());
      }
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      e.addEvent2(T+exp(valeur), EV_ARRIVEE, RandomPC());
      N++;
    }else if(premier.getType() == EV_DEB_SERV) {
      NbrePcLibres--;
      tableau[premier.getNumeroPC()] = 1;
      e.addEvent2(T+exp(mu), EV_FIN_SERV, premier.getNumeroPC());
    }else if(premier.getType() == EV_FIN_SERV){
      NbrePcLibres++;
      tableau[premier.getNumeroPC()] = 0;
      cpt++;
      if(N - (10 - NbrePcLibres)> 0 && eList2.getFirst().getNumeroPC() == premier.getNumeroPC()) {
        e.addEvent2(T, EV_DEB_SERV, premier.getNumeroPC());
      }
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      N--;
    }
  }

  private static void traitement3(evenement2 premier, echeancier e, double valeur) {
    if(premier.getType() == EV_ARRIVEE) {
      if(tableau[premier.getNumeroPC()] == 0) {
        e.addEvent2(T, EV_DEB_SERV,premier.getNumeroPC());
      }else {
        tableau2[premier.getNumeroPC()] ++;
      }
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      int min = min();
      ArrayList<Integer> list = new ArrayList<Integer>();
      for (int i = 0; i < tableau2.length; i++) {
        if(min == tableau2[i]) {
          list.add(i);
        }
      }
      if(list.size() != 1) {
        Random random = new Random();
        int indiceAleatoire = random.nextInt(list.size());
        Integer elementAleatoire = list.get(indiceAleatoire);
        e.addEvent2(T+exp(valeur), EV_ARRIVEE, elementAleatoire);
        tableau2[elementAleatoire]++;
      }else {
        e.addEvent2(T+exp(valeur), EV_ARRIVEE, list.get(0));
        tableau2[list.get(0)]++;
      }
      N++;

    }else if(premier.getType() == EV_DEB_SERV) {
      tableau2[premier.getNumeroPC()]--;
      tableau[premier.getNumeroPC()] = 1;
      e.addEvent2(T+exp(mu), EV_FIN_SERV, premier.getNumeroPC());
    }else if(premier.getType() == EV_FIN_SERV){
      tableau[premier.getNumeroPC()] = 0;
      cpt++;
      Boolean bool = false;
      for (int i = 0; i < tableau2.length; i++) {
        if(tableau2[i] > 0) {
          bool = true;
        }
      }
      if(bool && eList2.getFirst().getNumeroPC() == premier.getNumeroPC()) {
        e.addEvent2(T, EV_DEB_SERV, premier.getNumeroPC());
      }
      if(T > last_T) {
        S_clients += N;
        last_T = T;
      }
      N--;
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
      ecrivainFichier.write(valeur + " ");
      double result1 = mode1(valeur);
      if(cpt == 1000000) {
        ecrivainFichier.write("-1 ");
      }else {
        ecrivainFichier.write(result1 + " ");
      }
      double result2 = mode2(valeur);
      if(cpt == 1000000) {
        ecrivainFichier.write("-1 ");
      }else {
        ecrivainFichier.write(result2 + " ");
      }
      double result3 = mode3(valeur);
      if(cpt == 1000000) {
        ecrivainFichier.write("-1 ");
      }else {
        ecrivainFichier.write(result3 + "");
      }
      ecrivainFichier.write(System.lineSeparator());
    }
    lecteurBuffer.close();
    lecteurFichier.close();
    ecrivainFichier.close();
    CourbeGraph.graph();
  }

}