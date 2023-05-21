package traitement;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class echeancier {
  public static LinkedList<evenement> eList = new LinkedList();
  public static LinkedList<evenement2> eList2 = new LinkedList();
  public static int EV_ARRIVEE = 100;
  public static int EV_DEB_SERV = 200;
  public static int EV_FIN_SERV = 300;
  public static int NbrePcLibres = 10;
  public static int nbreServeurs = 10;
  public static int N = 0;
  public static int mu = 1;
  public static int cpt = 0;
  public static double T = 0.0;
  public static double last_T = 0.0;
  public static int l = 1000;
  public static double h = 0.001;
  public static double NewValue;
  public static double DebRectangle;
  public static double S_tpsAttente = 0.0;
  public static int S_clients = 0;
  public static double last_T2 = 0.0;
  public static boolean[] computers = new boolean[10];
  public static int[] computerClients = new int[10];

  public echeancier() {
    int i;
    for(i = 0; i < computerClients.length; ++i) {
      computerClients[i] = 0;
    }

    for(i = 0; i < computers.length; ++i) {
      computers[i] = false;
    }

  }

  public int addEvent(double date, int type) {
    int i = 0;
    if (!eList.isEmpty() && !(((evenement)eList.get(0)).getDate() > date)) {
      for(Iterator var6 = eList.iterator(); var6.hasNext(); ++i) {
        evenement e = (evenement)var6.next();
        if (((evenement)eList.get(i)).getDate() > date) {
          eList.add(i, new evenement(date, type));
          return 0;
        }
      }

      eList.addLast(new evenement(date, type));
      return 0;
    } else {
      eList.addFirst(new evenement(date, type));
      return 0;
    }
  }

  public static evenement premier() {
    evenement e = (evenement)eList.getFirst();
    eList.remove(0);
    return e;
  }

  public int addEvent2(double date, int type, int numeroPC) {
    int i = 0;
    if (!eList2.isEmpty() && !(((evenement2)eList2.get(0)).getDate() > date)) {
      for(Iterator var7 = eList2.iterator(); var7.hasNext(); ++i) {
        evenement2 e = (evenement2)var7.next();
        if (((evenement2)eList2.get(i)).getDate() > date) {
          eList2.add(i, new evenement2(date, type, numeroPC));
          return 0;
        }
      }

      eList2.addLast(new evenement2(date, type, numeroPC));
      return 0;
    } else {
      eList2.addFirst(new evenement2(date, type, numeroPC));
      return 0;
    }
  }

  public static evenement2 premier2() {
    evenement2 e = (evenement2)eList2.getFirst();
    eList2.remove(0);
    return e;
  }

  public static void afficher() {
    System.out.println("#");
    Iterator var1 = eList.iterator();

    while(var1.hasNext()) {
      evenement e = (evenement)var1.next();
      System.out.println(e.getDate() + " " + e.getType());
      System.out.println(System.getProperty("line.separator"));
    }

    System.out.println("#");
    System.out.println(System.getProperty("line.separator"));
  }

  public static double exp(double d) {
    double r = Math.random() * 1.0;
    return -Math.log(r) / d;
  }

  public static double mode1(double valeur) {
    int i = 0;
    cpt = 0;
    N = 0;
    NbrePcLibres = 10;
    DebRectangle = 0.0;
    NewValue = 0.0;
    S_tpsAttente = 0.0;
    S_clients = 0;
    last_T = 0.0;
    last_T2 = 0.0;
    echeancier e = new echeancier();
    e.addEvent(0.0, EV_ARRIVEE);

    while(cpt < 1000000 && i < l) {
      evenement premier = premier();
      T = premier.getDate();
      traitement1(premier, e, valeur);
      NewValue = (double)S_clients / T;
      if (Math.abs(DebRectangle - NewValue) > DebRectangle * (h / 2.0)) {
        DebRectangle = NewValue;
        i = 0;
      } else {
        ++i;
      }
    }

    return NewValue / valeur - 1.0;
  }

  public static double mode2(double valeur) {
    return valeur;
  }

  public static double mode3(double valeur) {
    int i = 0;
    cpt = 0;
    N = 0;
    NbrePcLibres = 10;
    DebRectangle = 0.0;
    NewValue = 0.0;
    S_tpsAttente = 0.0;
    S_clients = 0;
    last_T = 0.0;
    last_T2 = 0.0;
    echeancier e = new echeancier();
    int nPc = minRandomPc(10);
    e.addEvent2(0.0, EV_ARRIVEE, nPc);

    while(cpt < 1000000 && i < l) {
      evenement2 premier = premier2();
      T = premier.getDate();
      traitement3(premier, e, valeur);
      NewValue = (double)S_clients / T;
      if (Math.abs(DebRectangle - NewValue) > DebRectangle * (h / 2.0)) {
        DebRectangle = NewValue;
        i = 0;
      } else {
        ++i;
      }
    }

    return NewValue / valeur - 1.0;
  }

  private static void traitement1(evenement premier, echeancier e, double valeur) {
    if (premier.getType() == EV_ARRIVEE) {
      if (NbrePcLibres > 0) {
        e.addEvent(T, EV_DEB_SERV);
      }

      if (T > last_T) {
        S_clients += N;
        last_T = T;
      }

      e.addEvent(T + exp(valeur), EV_ARRIVEE);
      ++N;
    } else if (premier.getType() == EV_DEB_SERV) {
      --NbrePcLibres;
      e.addEvent(T + exp((double)mu), EV_FIN_SERV);
    } else if (premier.getType() == EV_FIN_SERV) {
      ++cpt;
      ++NbrePcLibres;
      if (N - nbreServeurs > 0) {
        e.addEvent(T, EV_DEB_SERV);
      }

      if (T > last_T) {
        S_clients += N;
        last_T = T;
      }

      --N;
    }

  }

  private static int minRandomPc(int n) {
    Random random = new Random();
    return random.nextInt(n) - 1;
  }

  private static int minTab() {
    int min = computerClients[0];

    for(int i = 0; i < computerClients.length; ++i) {
      if (min > computerClients[i]) {
        min = computerClients[i];
      }
    }

    return min;
  }

  private static void traitement3(evenement2 premier, echeancier e, double valeur) {
    int var10002;
    if (premier.getType() == EV_ARRIVEE) {
      int min = minTab();
      ArrayList<Integer> indices = new ArrayList();

      for(int i = 0; i < computerClients.length; ++i) {
        if (computerClients[i] == min) {
          indices.add(i);
        }
      }

      if (!computers[premier.getNumeroPC()]) {
        e.addEvent2(T, EV_DEB_SERV, premier.getNumeroPC());
      } else {
        var10002 = computerClients[premier.getNumeroPC()]++;
      }

      if (T > last_T) {
        S_clients += N;
        last_T = T;
      }

      if (indices.size() != 1) {
        Random random = new Random();
        int indiceAleatoire = random.nextInt(indices.size());
        Integer elementAleatoire = (Integer)indices.get(indiceAleatoire);
        e.addEvent2(T + exp(valeur), EV_ARRIVEE, elementAleatoire);
        var10002 = computerClients[elementAleatoire]++;
      } else {
        e.addEvent2(T + exp(valeur), EV_ARRIVEE, (Integer)indices.get(0));
        var10002 = computerClients[(Integer)indices.get(0)]++;
      }

      ++N;
    } else if (premier.getType() == EV_DEB_SERV) {
      computers[premier.getNumeroPC()] = true;
      var10002 = computerClients[premier.getNumeroPC()]--;
      e.addEvent2(T + exp((double)mu), EV_FIN_SERV, premier.getNumeroPC());
    } else if (premier.getType() == EV_FIN_SERV) {
      ++cpt;
      computers[premier.getNumeroPC()] = false;
      boolean verif = false;

      for(int i = 0; i < computerClients.length; ++i) {
        if (computerClients[i] > 0) {
          verif = true;
        }
      }

      if (verif && ((evenement2)eList2.getFirst()).getNumeroPC() == premier.getNumeroPC()) {
        e.addEvent2(valeur, EV_ARRIVEE, premier.getNumeroPC());
      }

      if (T > last_T) {
        S_clients += N;
        last_T = T;
      }

      --N;
    }

  }

  public static void main(String[] args) throws IOException {
    String nomFichier = "lambda.txt";
    File fichier = new File(nomFichier);
    FileReader lecteurFichier = new FileReader(fichier);
    BufferedReader lecteurBuffer = new BufferedReader(lecteurFichier);

    FileWriter ecrivainFichier;
    String ligne;
    for(ecrivainFichier = new FileWriter("resultats.txt"); (ligne = lecteurBuffer.readLine()) != null; ecrivainFichier.write(System.lineSeparator())) {
      double valeur = Double.parseDouble(ligne);
      double result1 = mode1(valeur);
      ecrivainFichier.write(valeur + " ");
      if (cpt >= 1000000) {
        ecrivainFichier.write("-1 ");
      } else {
        ecrivainFichier.write(result1 + " ");
      }

      double result3 = mode3(valeur);
      if (cpt == 1000000) {
        ecrivainFichier.write("-1 ");
      } else {
        ecrivainFichier.write(String.valueOf(result3));
      }
    }

    lecteurBuffer.close();
    lecteurFichier.close();
    ecrivainFichier.close();
  }
}

