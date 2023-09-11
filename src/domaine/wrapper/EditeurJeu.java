package domaine.wrapper;

import domaine.base.Editeur;
import domaine.base.Jeu;

import java.util.Objects;

public class EditeurJeu {
    private Editeur editeur;
    private Jeu jeu;

    public EditeurJeu(Editeur editeur, Jeu jeu) {
        this.editeur = editeur;
        this.jeu = jeu;
    }

    @Override
    public String toString() {
        return editeur.toString() + " a fait le jeu " + jeu.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditeurJeu that = (EditeurJeu) o;
        return Objects.equals(editeur, that.editeur) && Objects.equals(jeu, that.jeu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(editeur, jeu);
    }


    public double getRanking() {
        return jeu.getRanking();
    }
}
