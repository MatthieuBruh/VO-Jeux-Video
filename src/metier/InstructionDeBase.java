package metier;

import dao.TraitementNeo4J.BddNeo4J;

public class InstructionDeBase {

    private static BddNeo4J bdd = new BddNeo4J();

    /**
     * Cette fonction permet de changer le nombre de vente d'un jeu.
     * @param jeu le jeu à qui on doit changer le nombre de vente.
     */
    public static void updateGame(String jeu, double nombreDeVentes) {
        bdd.connect();
        bdd.run("match (j:Jeu{nom:'" + jeu + "'}) set j.ventes = "+ nombreDeVentes + " return j");
        bdd.close();
    }

    /**
     * Cette fonction permet de supprimer un jeu.
     * @param jeu le jeu que l'on souhaite supprimer.
     */
    public static void deleteGame(String jeu) {
        bdd.connect();
        bdd.run("match (j:Jeu{nom:'" + jeu + "'}) detach delete j");
        bdd.close();
    }

    /**
     * Cette fonction permet de supprimer une relation entre un joueur et un jeu.
     * @param jeu Le jeu qui est relié avec la personne.
     * @param nomPers Le nom de la personne.
     * @param prenomPers Le prenom de la personne
     */
    public static void deleteRelationPlayerGame(String jeu, String nomPers, String prenomPers) {
        bdd.connect();
        bdd.run("match (j:Jeu{nom: '" + jeu + "'})<-[r]-(p:Personne{nom: '" + nomPers + "', prenom: '" + prenomPers + "'}) delete r");
        bdd.close();
    }

    /**
     * Trouver un jeu
     * @param nom Le nom du jeu
     */
    public static void findGame(String nom) {
        bdd.connect();
        bdd.run("MATCH (j:Jeu {nom:'" + nom + "'}) return j");
        bdd.close();
    }
}
