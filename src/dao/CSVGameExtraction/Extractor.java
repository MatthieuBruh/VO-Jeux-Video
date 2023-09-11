package dao.CSVGameExtraction;

import dao.CSVTools.CSVWriter;
import dao.CSVTools.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Extractor {

    public Extractor(String fileToExtract, String fileToWrite) {
        List<String[]> result;

        try {
            result = chargerData(fileToExtract);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire les jeux! Impossible de continuer.");
        }

        result = extractData(result);

        //afficherData(result);

        try {
            CSVWriter.writeFile(result, fileToWrite);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'écrire les jeux dans un fichier! Impossible de continuer.");
        }
    }

    /**
     * Méthode permettant d'appler une autre méthode afin de lire les données d'un CSV
     * @param filename Nom du fichier CSV
     * @return Retourne le contenu du fichier sous forme d'une liste de tableau de String
     * @throws IOException Exception qui est renvoyée en cas d'erreur
     */
    public static List<String[]> chargerData(String filename) throws IOException {
        return CSVReader.readFile(filename);
    }

    /**
     * Méthode permettant de prendre les données souhaitée.
     * Cette méthode est propre au fichier lu (doit être réadapté en cas de besoin)
     * @param allData Reçoi une liste de tableau de string
     * @return Retourne un double tableau de string
     */
    public static List<String[]> extractData(List<String[]> allData) {
        List<String[]> newAllGames = new ArrayList<>(allData.size());
        String[] temp;

        for (int i = 0; i < allData.size(); i++) {
            String nom = comparaison(allData.get(i)[0]);
            String plateforme = comparaison(allData.get(i)[1]);
            String annee = comparaison(allData.get(i)[2]);
            String genre = comparaison(allData.get(i)[3]);
            String edit = comparaison(allData.get(i)[4]);
            String sales = comparaison(allData.get(i)[9]);
            String score = comparaison(allData.get(i)[12]);
            String prix;
            if (i > 0) {
                prix = String.valueOf(findPrice(annee));
            } else {
                prix = "Price";
            }
            temp = new String[]{nom, plateforme, annee, genre, edit, sales, score, prix};
            newAllGames.add(temp);
        }
        return newAllGames;
    }

    /**
     * Méthode permettant de remplacer les espaces vide par des 0
     * @param data Prend la valeur à tester
     * @return Retourne soit 0 soit la data reçue
     */
    public static String comparaison(String data) {
        if (data.equalsIgnoreCase(" ")) {
            return "0";
        } else {
            return data;
        }
    }

    /**
     * Fonction permettant d'afficher de la data qui est sous forme de liste contenant des tableaux de string
     * @param allData Reçois la liste qu'il va falloir afficher
     */
    public static void afficherData(List<String[]> allData) {
        for (String[] data : allData) {
            for (String str : data) {
                System.out.print(str + ";");
            }
            System.out.println();
        }
    }

    /**
     * Méthode permettant de trouver un prix à un jeu vidéo en fonction de son année de sortie
     * @param year Reçois l'année de sortie du jeu
     * @return Retourne le prix sous forme d'int
     */
    public static int findPrice(String year) {
        try {
            int annee = Integer.parseInt(year);
            Random random = new Random();
            if (annee < 2000) {
                return random.nextInt(15 - 2) + 2;
            } else if (annee >= 2000 || annee <= 2014) {
                return random.nextInt(45 - 10) + 10;
            } else  {
                return random.nextInt(80 - 50) + 50;
            }
        } catch (NumberFormatException p) {
            return 0;
        }
    }
}
