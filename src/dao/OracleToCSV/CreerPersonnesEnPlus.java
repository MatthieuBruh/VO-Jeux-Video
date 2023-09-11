package dao.OracleToCSV;

import dao.CSVTools.CSVWriter;
import dao.CSVTools.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreerPersonnesEnPlus {
    private static List<String[]> personnes;
    public CreerPersonnesEnPlus(String destination) {
        try {
            this.personnes = CSVReader.readFile("VO-Personnes_Source.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        test(destination);
    }

    public static void test(String destination) {
        List<String[]> personnesNew = new ArrayList<>();
        List<String> nomDeFamille = new ArrayList<>();
        for (int i = 0; i < personnes.size(); i++) {
            if (!nomDeFamille.contains(personnes.get(i)[0])) {
                List<String> prenomUtilise = new ArrayList<>();
                for (int j = 0; j < 25; j++) {
                    int pos = prendrePrenomRandom();
                    if (!prenomUtilise.contains(personnes.get(pos)[1])) {
                        String[] temp = new String[]{personnes.get(i)[0], personnes.get(pos)[1]};
                        personnesNew.add(temp);
                        prenomUtilise.add(personnes.get(pos)[1]);
                    }
                }
                nomDeFamille.add(personnes.get(i)[0]);
            }
        }
        try {
            CSVWriter.writeFile(personnesNew, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int prendrePrenomRandom() {
        int min = 1;
        int max = personnes.size();
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
