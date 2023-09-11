package dao.CSVTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    /**
     * Méthode permettant de lire un fichier CSV
     * @param filename Le fichier qu'il faut lire
     * @return Retourne le cotnenu du fichier sous forme d'une liste de tableau de string (chaque tableau = 1 ligne)
     * @throws IOException Remonte une IOException en cas d'erreur
     */
    public static List<String[]> readFile(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String ligne = reader.readLine();
        data.add(ligne.split(";"));
        while ((ligne=reader.readLine()) != null) {
            data.add(ligne.split(";"));
        }
        reader.close();
        return data;
    }
}
