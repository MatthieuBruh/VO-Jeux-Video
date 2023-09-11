package domaine.wrapper;

import domaine.base.Editeur;
import domaine.base.Jeu;
import domaine.base.Plateforme;

public class ConcurrentDirect {
    private Editeur editeur1;
    private Jeu jeu1;
    private Plateforme platJeu1;
    private Editeur editeur2;
    private Jeu jeu2;
    private Plateforme platJeu2;
    private double estimationVentes;

    public ConcurrentDirect(Editeur editeur1, Jeu jeu1, Plateforme platJeu1, Editeur editeur2, Jeu jeu2, Plateforme platJeu2, double estimationVentes) {
        this.editeur1 = editeur1;
        this.jeu1 = jeu1;
        this.platJeu1 = platJeu1;
        this.editeur2 = editeur2;
        this.jeu2 = jeu2;
        this.platJeu2 = platJeu2;
        this.estimationVentes = (double) Math.round(estimationVentes * 100) / 100;
    }

    @Override
    public String toString() {
        return jeu1.toString() + " fait par " + editeur1 + " a comme concurrent direct " + jeu2.toString() + " fait par " + editeur2 + ".\n" +
                "On estime que le jeu : " + jeu1 + " aurait obtenu " + estimationVentes + " de ventes s'il était sorti sur " + platJeu2 +
                " à la place de la plateforme " + platJeu1 ;
    }

}
