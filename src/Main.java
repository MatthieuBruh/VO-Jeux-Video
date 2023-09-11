import dao.CSVGameExtraction.Extractor;
import dao.OracleToCSV.CreerPersonnesEnPlus;
import dao.OracleToCSV.MyOracle;
import metier.InstructionDeBase;
import dao.TraitementNeo4J.MyNeo4j;
import metier.Neo4jRequetes;

public class Main {

    public static void main(String[] args) {
        // Si utilisation de note BDD, ne pas charger les data
        // (Ajouter des // devant les lignes qui ne doivent pas être utilisées)
        chargerData();
        principaleRequetes();
        requetes();
    }

    /**
     * Méthode utilisée pour le chargement des données en BDD.
     * Les données initiales proviennent des fichiers:
     *    - VO-VideoGames_Source.csv (extrait de: https://www.kaggle.com/kendallgillies/video-game-sales-and-ratings)
     *    - VO-Personnes_Source.csv ( Examen 62-21 datant du 16 juin 2021 à la Haute Ecole de Gestion de Genève)
     */
    public static void chargerData() {
        /* VIDER LA BDD Neo4J */
        // MyNeo4j.deleteAll();

        /*
         * BDD Oracle (si vous possédez la BDD de l'examen (sinon le fichier prendra le relais)
         * ATTENTION : Si vous utilisez la BDD de l'examen, il faut décommenter les lignes suivantes afin de fournir les informations de connection
        */
        // BddOracle.getInstance().setUrl("");
        // BddOracle.getInstance().setUser("");
        // BddOracle.getInstance().setPsswd("");

        /*
         * BDD Neo4J.
         * Il est obligatioire de fournir les informations de connection à la BDD et de décommenter les lignes suivantes
        */
        // BddNeo4J bdd = new BddNeo4J();
        // bdd.setUri("");
        // bdd.setUser("");
        //bdd.setPsswd("");


        // Extraction des données des jeux vidéo.
        // new Extractor(source, destination);
        new Extractor("VO-VideoGames_Source.csv", "VO-Jeux.csv");


        /*
         * Gestion des personnes (joueurs)
         * Faire l'étape new MyOracle() UNIQUEMENT si vous possédez la table exa_etudiant (examen de juin 2021)
         * La class MyOracle réécrit le fichier VO-Personnes_Source.csv
         * Le paramètre est le fichier destination qui va contenir les personnes de la BDD
         *
        */
        // new MyOracle("VO-Personnes_Source.csv");

        /*
         * Créations de personnes en plus (si nécessaire)
         * Permet de faire des mixtes de personnes (noms et prénoms) afin d'avoir plus de personnes
         */
        new CreerPersonnesEnPlus("VO-Personnes.csv");


        /*
         * Insertion des données dans la BDD Neo4J.
         * Les paramètres permettent de choisir combien de jeu insérer, de joueurs, ainsi que les fichiers à utiliser
         *
         * 1er paramètre ==> nombre de jeux à entrer :
         *    => Tous les jeux: -1
         *    => Un nombre de jeux limité: xxx (mettre le nombre souhaité)
         * 2ème paramètre ==> le nom fichier contenant les jeux à entrer
         *
         * 3ème paramètre ==> nombre de jeux à entrer :
         *    => Tous les joueurs: -1
         *    => Un nombre de personnes limité: xxx (mettre le nombre souhaité)
        */
        new MyNeo4j(300,"VO-Jeux.csv",  800, "VO-Personnes.csv");
    }

    /**
     * Fonction permettant de faire les requêtes de base
     */
    public static void principaleRequetes() {
        // Les instructions INSERT sont dans la classe MyNeo4j
        InstructionDeBase.updateGame("Super Mario Bros.", 82.55);
        InstructionDeBase.deleteGame("Tetris");
        InstructionDeBase.deleteRelationPlayerGame("Grand Theft Auto V", "Lacroix", "Lucien");
    }

    public static void requetes() {
        new Neo4jRequetes();
    }

    /*
    SOURCES BDD :
        - BDD de jeux vidéo :   https://www.kaggle.com/kendallgillies/video-game-sales-and-ratings
        - BDD de personnes  :   Examen 62-21 datant du 16 juin 2021
    */

}
