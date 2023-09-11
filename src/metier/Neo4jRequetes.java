package metier;

import dao.TraitementNeo4J.BddNeo4J;
import domaine.base.*;
import domaine.wrapper.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import java.util.ArrayList;
import java.util.List;

public class Neo4jRequetes {

    private static BddNeo4J bdd = new BddNeo4J();
    // On a stocké la variable BDD ici afin de ne pas avoir à répéter la connection et la fermeture de la BDD.
    // Dans les autres cas, on y aurait directement mis dans chaque méthode.

    public Neo4jRequetes() {
        bdd.connect();
        scoreMediocreGenre("Role-Playing");
        meilleurGenreEditeur("Square Enix");
        propositionDeJeuJoueur("Sauser", "Marion");
        concurrentDirect("Monster Hunter Freedom 3"); // Cette requête prend du temps.
        joueursCompatibles("Sauser", "Marion");
        liensEntreDeuxJoueurs("Sauser","Marion","Blanco","Sofia");
        remasterJeux(2005, 2007);
        bdd.close();
    }

    /**
     * 1 : Amélioration d'un jeu médiocre
     * @param genre
     */
    public static void scoreMediocreGenre(String genre) {
        System.out.println("\u001B[31m1 : Amélioration d'un jeu médiocre \033[0m");
        Result res = bdd.run(
                "MATCH (e1:Editeur)-[]->(j1:Jeu) -[]-> (g:Genre{nom:'" + genre + "'}) " +
                "WHERE j1.score < 5 AND j1.score > 2 WITH DISTINCT j1,e1 " +
                "MATCH (e2:Editeur)-[]->(j2:Jeu) -[]-> (g:Genre{nom:'" + genre + "'}) " +
                "WHERE j2.score < 10 AND j2.score > 6 " +
                "RETURN distinct e1, j1, e2, j2  LIMIT 1");
        Record rec = res.next();
        EditeurJeu editeurJeu1 = new EditeurJeu(new Editeur(rec.get("e1").get("nom").asString()), creerJeu(rec.get("j1")));
        EditeurJeu editeurJeu2 = new EditeurJeu(new Editeur(rec.get("e2").get("nom").asString()), creerJeu(rec.get("j2")));
        System.out.println(editeurJeu1 + " pourrait s'améliorer en se fiant à " + editeurJeu2);
        System.out.println();
    }

    /**
     * 2 : Estimation du nombre de vente
     * @param editeur
     */
    public static void meilleurGenreEditeur(String editeur) {
        System.out.println("\u001B[31m2 : Estimation du nombre de vente \033[0m");
        Result res = bdd.run("MATCH (e:Editeur{nom:'" + editeur + "'})-[]->(j:Jeu)-[]->(g:Genre) " +
                        "WITH e, g, AVG(j.score) as meilleureCat " +
                        "ORDER BY meilleureCat DESC " +
                        "LIMIT 3 " +
                        "MATCH (genre:Genre)<-[]-(jeu:Jeu)<-[]-(personne:Personne) " +
                        "WHERE genre = g " +
                        "WITH e, genre, meilleureCat, count(personne) as nombreDeJoueurs " +
                        "RETURN e, genre, nombreDeJoueurs, (meilleureCat * nombreDeJoueurs / 10) as estimationDeJoueur " +
                        "LIMIT 1");

        Record rec = res.next();
        BestGenreEditeur best = new BestGenreEditeur(new Editeur(rec.get("e").get("nom").asString()),
                new Genre(rec.get("genre").get("nom").asString()),rec.get("nombreDeJoueurs").asInt(),
                (int) rec.get("estimationDeJoueur").asDouble());
        System.out.println(best);
        System.out.println();
    }

    /**
     * 3 : propostion d'un nouveau jeu à un joueur
     * @param nom Le nom du joueur
     * @param prenom Le prenom du joueur
     */
    public static void propositionDeJeuJoueur(String nom, String prenom) {
        System.out.println("\u001B[31m3 : propostion d'un nouveau jeu à un joueur \033[0m");
        Result res = bdd.run("MATCH (p:Personne{nom:'" + nom + "',prenom:'" + prenom + "'})-[:JoueA]->(:Jeu)-[r1:du_genre]->(g:Genre) " +
                "WITH g, COUNT(r1) AS num, p " +
                "MATCH (j1:Jeu)-[:du_genre]->(g), (p)-[:JoueA]->(j2:Jeu)-[:du_genre]->(g) WHERE j1<>j2 " +
                "RETURN p, j1, g, num ORDER BY num DESC LIMIT 1");
        Record rec = res.next();
        JoueurJoue joueurJoue = new JoueurJoue(new Joueur(rec.get("p").get("nom").asString(), rec.get("p").get("prenom").asString()),
                creerJeu(rec.get("j1")), new Genre(rec.get("g").get("nom").asString()));
        System.out.println(joueurJoue);
        System.out.println();
    }

    /**
     * 4 : Concurrent direct
     * @param jeu Le jeu dont on cherche le le conccurent direct
     */
    public static void concurrentDirect(String jeu) {
        System.out.println("\u001B[31m4 : Concurrent direct \033[0m");
        Result res = bdd.run("MATCH (plat1:Plateforme)-[]->(j:Jeu{nom:'" + jeu + "'}) <-[]- (e:Editeur), (j) -[]-> (g:Genre) " +
                "WHERE j.annee > 0 WITH j, e, g,plat1 " +
                "MATCH  (plat2:Plateforme)-[]->(j2:Jeu)-[]->(g:Genre), (j2)<-[]- (e2:Editeur) " +
                "WHERE j2.annee > 0 AND e <> e2 and plat1 <> plat2 and j2.annee >= (j.annee - 2) " +
                "and j2.annee <= (j.annee + 2) and j2.score >= j.score -0.3 and j2.score <= j.score +0.3 " +
                "RETURN j, e, plat1, (j.ventes + j2.ventes)/2 as predictionDeVentes, j2, e2, plat2 order by j2.score asc limit 1");
        Record rec = res.next();
        System.out.println(rec);
        ConcurrentDirect jeuJeu = new ConcurrentDirect(new Editeur(rec.get("e").get("nom").asString()), creerJeu(rec.get("j")), new Plateforme(rec.get("plat1").get("nom").asString()),
                new Editeur(rec.get("e2").get("nom").asString()), creerJeu(rec.get("j")), new Plateforme(rec.get("plat2").get("nom").asString()), rec.get("predictionDeVentes").asDouble());
        System.out.println(jeuJeu);
        System.out.println();
    }

    /**
     * 5 : Joueurs compatibles
     * @param nom Le nom du joueur dont on cherche des acolytes
     * @param prenom Le prénom du joueur dont on cherche des acolytes
     */
    public static void joueursCompatibles(String nom, String prenom) {
        System.out.println("\u001B[31m5 : Joueurs compatibles \033[0m");
        Result res = bdd.run("MATCH (p1:Personne {nom:'" + nom + "',prenom:'" + prenom + "'})-[]->(j1:Jeu) -[]->(g:Genre), " +
                "(p2:Personne) -[]-> (j2:Jeu)-[]->(g), path = ShortestPath((p1)-[*..4]-(p2)) where j2.annee >= (j1.annee - 2) and j2.annee <= (j1.annee + 2) and p1.nom <> p2.nom " +
                "with reduce(output = [], e in nodes(path) | output + e) as nodeCollection, " +
                "reduce(output = [], r1 in relationships(path) | output + r1) as relations " +
                "unwind relations as relation " +
                "unwind nodeCollection as NoeudsTraverses " +
                "return NoeudsTraverses, labels(NoeudsTraverses), relation limit 10");
        parcoursPath(res);
    }

    /**
     * 6 : Liens entre deux personnes
     */
    public static void liensEntreDeuxJoueurs(String nomPersonne1, String prenomPersonne1, String nomPersonne2, String prenomPersonne2 ) {
        System.out.println("\u001B[31m6 : Liens entre deux joueurs \033[0m");
        Result res = bdd.run("match (p:Personne{nom:'" + nomPersonne1 + "',prenom:'" + prenomPersonne1 + "'})," +
                "(p2:Personne{nom:'" + nomPersonne2 + "',prenom:'" + prenomPersonne2 + "'}), " +
                "path = shortestpath((p)-[*]-(p2)) where p2<>p " +
                "with reduce(output = [], e in nodes(path) | output + e) as nodeCollection, " +
                "reduce(output = [], r1 in relationships(path) | output + r1) as relations " +
                "unwind nodeCollection as NoeudsTraverses unwind relations as relation " +
                "return distinct NoeudsTraverses, labels(NoeudsTraverses), relation");
        parcoursPath(res);
    }

    /**
     * 7 : Remaster de jeux
     */
    public static void remasterJeux(int anneeMin, int anneeMax) {
        System.out.println("\u001B[31m7 : Remaster de jeux \033[0m");
        Result res = bdd.run("match (j:Jeu)<-[]-(e:Editeur) where j.annee > " + anneeMin + " and j.annee < " + anneeMax +" and j.score > 8.5 with j, e " +
                "match (p:Personne)-[r:JoueA]-> (j) return j, e.nom as Editeur, count(r) as NombreDePersonneQuiJoue, j.score as Score order by count(r) desc,j.score desc limit 10");
        List<RemasterJeu> jeuxToRemaster = new ArrayList<>();
        while (res.hasNext()) {
            Record rec = res.next();
            jeuxToRemaster.add(new RemasterJeu(new EditeurJeu(new Editeur(rec.get("Editeur").asString()), creerJeu(rec.get("j"))),
                    Integer.parseInt(String.valueOf(rec.get("NombreDePersonneQuiJoue")))));
        }
        for (int i = 0; i < jeuxToRemaster.size(); i++) {
            System.out.println("\t" + (i+1) + " : " + jeuxToRemaster.get(i));
        }
        System.out.println();
    }

    private static void parcoursPath(Result res) {
        int i = 0;
        while (res.hasNext()) {
            Record rec = res.next();
            String typeNoeud = rec.get("labels(NoeudsTraverses)").get(0).asString();
            Node node = rec.get("NoeudsTraverses").asNode();
            if (i % 2 == 0) {
                switch (typeNoeud) {
                    case "Personne" :
                        System.out.print(new Joueur(node.get("nom").asString(), node.get("prenom").asString()) + " "); break;
                    case "Jeu" : System.out.print(creerJeuNode(node) + " "); break;
                    case "Plateforme" :
                        System.out.print(new Plateforme(node.get("nom").asString()) + " "); break;
                    case "Editeur" : System.out.print(new Editeur(node.get("nom").asString()) + " "); break;
                    case "Genre" : System.out.print(new Genre(node.get("nom").asString()) + " "); break;
                }
                Relationship rel = rec.get("relation").asRelationship();
                System.out.println(rel.type() + " ");
            }
            i++;
        }
        System.out.println();
    }

    /**
     * Méthode permettant de créer une instance de Jeu en recevant une value
     * @param jeu Le jeu dont on va devoir créer l'instance
     * @return L'instance du jeu
     */
    public static Jeu creerJeu(Value jeu) {
        Jeu.Builder game = new Jeu.Builder();
        game.nom(jeu.get("nom").asString());
        try {
            game.annee(Integer.parseInt(String.valueOf(jeu.get("annee"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.prix(Double.parseDouble(String.valueOf(jeu.get("prix"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.nbVentes(Double.parseDouble(String.valueOf(jeu.get("ventes"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.ranking(Double.parseDouble(String.valueOf(jeu.get("score"))));
        } catch (NumberFormatException ignored) {

        }
        return game.build();
    }
    public static Jeu creerJeuNode(Node jeu) {
        Jeu.Builder game = new Jeu.Builder();
        game.nom(jeu.get("nom").asString());
        try {
            game.annee(Integer.parseInt(String.valueOf(jeu.get("annee"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.prix(Double.parseDouble(String.valueOf(jeu.get("prix"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.nbVentes(Double.parseDouble(String.valueOf(jeu.get("ventes"))));
        } catch (NumberFormatException ignored) {

        }
        try {
            game.ranking(Double.parseDouble(String.valueOf(jeu.get("score"))));
        } catch (NumberFormatException ignored) {

        }
        return game.build();
    }
}
