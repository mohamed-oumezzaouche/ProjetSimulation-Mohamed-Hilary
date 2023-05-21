package traitement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CourbeGraph {
  public static void graph() {
    String fichier = "resultats.txt"; // Spécifiez le chemin vers votre fichier texte

    XYSeries series = new XYSeries("Courbe");

    try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
      String ligne;
      while ((ligne = br.readLine()) != null) {
        String[] valeurs = ligne.split(" ");
        double x = Double.parseDouble(valeurs[0]);
        double y = Double.parseDouble(valeurs[1]);

        series.add(x, y);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    XYSeriesCollection dataset = new XYSeriesCollection(series);

    JFreeChart chart = ChartFactory.createXYLineChart(
        "Courbe", // Titre du graphique
        "Abscisse", // Étiquette de l'axe des abscisses
        "Ordonnée", // Étiquette de l'axe des ordonnées
        dataset, // Données du graphique
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    ChartFrame frame = new ChartFrame("Graphique", chart);
    frame.pack();
    frame.setVisible(true);
  }
}
