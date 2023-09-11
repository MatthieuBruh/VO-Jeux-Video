package dao.CSVTools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    /**
     * Fonction permettant d'écrire de la data dans un fichier
     * @param allData Prend une liste de tableau de string en paramètre
     * @param filename Reçois le fichier dans lequel il va falloir écrire
     * @throws IOException Remonte une IOException en cas d'erreur
     */
    public static void writeFile(List<String[]> allData, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        for (String[] data : allData) {
            for (int i = 0; i < data.length; i++) {
                if (i == data.length -1) {
                    writer.write(data[i] + "\n");
                } else {
                    writer.write(data[i] + ";");
                }
            }
        }
        writer.close();
    }
}
