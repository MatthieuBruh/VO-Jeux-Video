package dao.OracleToCSV;

import dao.CSVTools.CSVWriter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyOracle {

    public MyOracle(String filename) {
        try {
            List<String[]> result = chercherPersonnes();
            write(result, filename);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de retrouver les personnes!! Impossible de continuer");
        } catch (IOException i) {
            throw new RuntimeException("Problème d'écrite des personnes!! ");
        }
    }

    /**
     * Méthode permettant d'aller chercher toutes les personnes (nom + prénom) dans la BDD oracle exa_etudiant.
     * @return Retourne toute la liste de personnes
     * @throws SQLException En cas d'erreur de fichier
     */
    public static List<String[]> chercherPersonnes() throws SQLException {
        BddOracle.getInstance().connect();
        List<String[]> personnes = new ArrayList<>();
        ResultSet res = BddOracle.query("SELECT * FROM exa_etudiant");
        while (res.next()) {
            String[] temp = new String[]{res.getString("etu_nom"), res.getString("etu_prenom")};
            personnes.add(temp);
        }
        BddOracle.disconnect();
        return personnes;
    }

    /**
     * Fonction permettant décrire la liste des personnes dans un fichier CSV
     * @param personnes La liste de personne
     * @param filename Le nom du fichier, où il faut écrire
     * @throws IOException En cas d'erreur de fichier
     */
    public static void write(List<String[]> personnes, String filename) throws IOException{
        CSVWriter.writeFile(personnes, filename);
    }
}
