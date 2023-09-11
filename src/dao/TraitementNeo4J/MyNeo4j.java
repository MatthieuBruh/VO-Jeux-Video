package dao.TraitementNeo4J;

import dao.CSVTools.CSVReader;
import org.neo4j.driver.Result;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MyNeo4j {
    private static List<String[]> allGames;
    private static List<String[]> allGamers;
    private static int numberGames;
    private static int numberPlayers;

    private static BddNeo4J bdd = new BddNeo4J();

    public MyNeo4j(int numberGames,String gamesFile, int numberPlayers, String playersFile) {
        try {
            allGames = CSVReader.readFile(gamesFile);
        } catch (IOException e) {
            throw new RuntimeException("Le fichier de jeux n'a pas été trouvé");
        }
        try {
            allGamers = CSVReader.readFile(playersFile);
        } catch (IOException e) {
            throw new RuntimeException("Le fichier de personnes n'a pas été trouvé");
        }
        if (numberGames == -1 || numberGames > allGames.size()) {
            MyNeo4j.numberGames = allGames.size();
        } else {
            MyNeo4j.numberGames = numberGames;
        }
        if (numberPlayers == -1 || numberPlayers > allGamers.size()) {
            MyNeo4j.numberPlayers = allGamers.size();
        } else {
            MyNeo4j.numberPlayers = numberPlayers;
        }
        creerPlatformeGenreEditeur();
        chargerPersonnes();

    }

    /**
     * Fonction permettant de parcourir la liste de jeux afin de créer
     */
    public static void creerPlatformeGenreEditeur() {
        bdd.connect();
        for (int i = 0; i < numberGames; i++) {
            if (i >= 1) {
                creerJeu(allGames.get(i));
                refactorBddConnection(i);
            }
        }
        bdd.close();
    }

    /**
     * Fonction permettant de créer un jeu, son éditeur, sa plateforme et son genre.
     * La création de l'éditeur, la plateforme et du genre se fait automatique par appel de méthode.
     * @param data Le jeu à inserer
     */
    public static void creerJeu(String[] data) {
        System.out.println("Traitement du jeu : " + data[0]);
        creerPlateforme(data[1]);
        creerGenre(data[3]);
        creerEditeur(data[4]);

        String nom = data[0];

        int annee;
        try {
            annee = Integer.parseInt(data[2]);
        } catch (NumberFormatException e) {
            annee = 0;
        }

        double ventes;
        try {
            ventes = Double.parseDouble(data[5]);
        } catch (NumberFormatException e) {
            ventes = 0.0;
        }

        double score;
        try {
            score = Double.parseDouble(data[6]);
        } catch (NumberFormatException e) {
            score = 0.0;
        }

        int prix;
        try {
            prix = Integer.parseInt(data[7]);
        } catch (NumberFormatException e) {
            prix = 0;
        }

        Result res = bdd.run("MATCH (p:Jeu{nom:'" + nom + "'}) RETURN p");
        if (!res.hasNext()) {
            bdd.run("CREATE (p:Jeu{nom: '" + nom + "', annee:" + annee + ", ventes:" + ventes + ", score:" + score + ", prix:" + prix + "})");
        }

        relationJeuEditeur(data[0], data[4]);
        relationJeuGenre(data[0], data[3]);
        relationJeuPlateforme(data[0], data[1]);
    }

    /**
     * Fonction permettant de créer une plateforme. Elle vérifie aussi que la plateforme n'existe pas.
     * @param plateforme nom de la plateforme
     */
    public static void creerPlateforme(String plateforme) {
        Result res = bdd.run("MATCH (p:Plateforme{nom:'" + plateforme + "'}) RETURN p");
        if (!res.hasNext()) {
            bdd.run("CREATE (p:Plateforme{nom: '" + plateforme + "'})");
        }
    }

    /**
     * Fonction permettant de créer un genre. Elle vérifie aussi que le genre n'existe pas.
     * @param genre nom du genre
     */
    public static void creerGenre(String genre) {
        Result res = bdd.run("MATCH (p:Genre{nom:'" + genre + "'}) RETURN p");
        if (!res.hasNext()) {
            bdd.run("CREATE (p:Genre{nom: '" + genre + "'})");
        }
    }

    /**
     * Fonction permettant de créer un éditeur. Elle vérifie aussi que l'éditeur n'existe pas.
     * @param editeur nom de l'éditeur
     */
    public static void creerEditeur(String editeur) {
        Result res = bdd.run("MATCH (p:Editeur{nom:'" + editeur + "'}) RETURN p");
        if (!res.hasNext()) {
            bdd.run("CREATE (p:Editeur{nom: '" + editeur + "'})");
        }
    }

    /**
     * Fonction permettant de faire une relation entre un jeu et un éditeur.
     * Elle vérifie aussi que la relation n'existe pas.
     * @param jeu Le nom du jeu concerné
     * @param editeur Le nom de l'éditeur concerné
     */
    public static void relationJeuEditeur(String jeu, String editeur) {
        Result res = bdd.run("MATCH (p:Jeu{nom:'" + jeu + "'})<-[*]-(:Editeur{nom:'" + editeur + "'}) return p");
        if (!res.hasNext()) {
            bdd.run("MATCH (a:Jeu{nom:'" + jeu + "'}), (b:Editeur{nom:'" + editeur + "'}) CREATE (a)<-[:a_edite]- (b)");
        }

    }

    /**
     * Fonction permettant de faire une relation entre un jeu et un genre
     * Elle vérifie aussi que la relation n'existe pas.
     * @param jeu Le nom du jeu concerné
     * @param genre Le nom du genre concerné
     */
    public static void relationJeuGenre(String jeu, String genre) {
        Result res = bdd.run("MATCH (p:Jeu{nom:'" + jeu + "'})-[*]->(:Genre{nom:'" + genre + "'}) return p");
        if (!res.hasNext()) {
            bdd.run("MATCH (a:Jeu{nom:'" + jeu + "'}), (b:Genre{nom:'" + genre + "'}) CREATE (a)-[:du_genre]-> (b)");
        }
    }

    /**
     * Fonction permettant de faire une relation entre un jeu et une plateforme
     * Elle vérifie aussi que la relation n'existe pas.
     * @param jeu Le nom du jeu concerné
     * @param plateforme Le nom de la plateforme concerné
     */
    public static void relationJeuPlateforme(String jeu, String plateforme) {
        Result res = bdd.run("MATCH (p:Jeu{nom:'" + jeu + "'})<-[*]-(:Plateforme{nom:'" + plateforme + "'}) return p");
        if (!res.hasNext()) {
            bdd.run("MATCH (a:Jeu{nom:'" + jeu + "'}), (b:Plateforme{nom:'" + plateforme + "'}) CREATE (a)<-[:propose]- (b)");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Fonction permettant de créer une personne et les jeux auxquels elle joue.
     * La création de la relation se fait automatiquement par appel de méthode.
     */
    public static void chargerPersonnes() {
        bdd.connect();
        for (int i = 0; i < numberPlayers; i++) {
            System.out.println("Traitement du joueur : " + allGamers.get(i)[0] + " " + allGamers.get(i)[1]);
            ajouterPersonne(allGamers.get(i));
            refactorBddConnection(i);
        }
        bdd.close();
    }

    /**
     * Fonction permettant d'ajouter une personne dans la bdd
     * @param personne le nom et prénom de la personne
     */
    public static void ajouterPersonne(String... personne) {
        String nom = personne[0];
        String prenom = personne[1];
        Result res = bdd.run("MATCH (p:Personne{nom:'" + nom + "', prenom:'" + prenom + "'}) RETURN p");
        if (!res.hasNext()) {
            bdd.run("CREATE (p:Personne{nom:'" + nom + "', prenom:'" + prenom + "'}) RETURN p");
        }
        ajouterPersonneJoue(personne);
    }

    /**
     * Fonction permettant de créer environ 10 relations entre des jeux et une personne.
     * Vérification que leu jeu n'est pas déjà lié à une personne.
     * Le nom est pris de manière aléatoire dans la liste des jeux. (appel de la méthode prendreJeuRandom)
     * @param personne Le nom et prénom de la personne
     */
    public static void ajouterPersonneJoue(String... personne) {
        String nom = personne[0];
        String prenom = personne[1];
        int nbJeux = nbJeuxRandom();
        for (int i = 0; i < nbJeux; i++) {
            int numJeu = prendreJeuRandom();
            String nomJeu = allGames.get(numJeu)[0];
            Result res = bdd.run("MATCH (p:Jeu{nom:'" + nomJeu + "'})<-[*]-(:Personne{nom:'" + nom + "', prenom:'" + prenom + "'}) RETURN p");
            if (!res.hasNext()) {
                bdd.run("MATCH (a:Jeu{nom:'" + nomJeu + "'}), (b:Personne{nom:'" + nom + "', prenom:'" + prenom + "'}) CREATE (a)<-[:JoueA]- (b)");
            }
        }
    }

    /**
     * Méthode peremettant de choisir un nombre random entre 1 et 20.
     * Utilisée pour le nombre de jeux d'un joueur
     * @return le int aléatoire
     */
    public static int nbJeuxRandom() {
        int min = 1;
        int max = 20;
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     * Méthode permettant de retourner un int aléatoirement.
     * @return le int aléatoire
     */
    public static int prendreJeuRandom() {
        int min = 1;
        int max = numberGames;
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Méthode permettant de supprimer tous les jeux, genres, éditeurs, personnes et plateformes de la BDD
     */
    public static void deleteAll() {
        bdd.connect();
        bdd.run("MATCH (p:Plateforme) detach delete p");
        bdd.run("MATCH (p:Genre) detach delete p");
        bdd.run("MATCH (p:Editeur) detach delete p");
        bdd.run("MATCH (p:Jeu) detach delete p");
        bdd.run("MATCH (p:Personne) detach delete p");
        bdd.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void refactorBddConnection(int i) {
        if (i%100 == 0) {
            bdd.close();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bdd.connect();
        }
    }
}
