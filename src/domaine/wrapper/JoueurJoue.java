package domaine.wrapper;

import domaine.base.Genre;
import domaine.base.Jeu;
import domaine.base.Joueur;

public class JoueurJoue {
    private Joueur joueur;
    private Jeu jeu;
    private Genre genre;

    public JoueurJoue(Joueur joueur, Jeu jeu, Genre genre) {
        this.joueur = joueur;
        this.jeu = jeu;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return joueur + " pourrait jouer à " + jeu + " du genre " + genre;
    }
}
