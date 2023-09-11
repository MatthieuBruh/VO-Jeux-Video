package domaine.wrapper;

import domaine.base.Editeur;
import domaine.base.Genre;

public class BestGenreEditeur {
    private Editeur editeur;
    private Genre genre;
    private int nbJoueursGenre;
    private int joueusPossible;

    public BestGenreEditeur(Editeur editeur, Genre genre, int nbJoueursGenre, int joueusPossible) {
        this.editeur = editeur;
        this.genre = genre;
        this.nbJoueursGenre = nbJoueursGenre;
        this.joueusPossible = joueusPossible;
    }

    @Override
    public String toString() {
        return "Le meilleure genre de l'éditeur " + editeur + " est : " + genre + ".\n" +
                "En ce concentrant sur ce genre, il pourrait y avoir " + joueusPossible + " joueurs possibles sur " + nbJoueursGenre + " joueurs totaux de ce genre.";
    }
}
