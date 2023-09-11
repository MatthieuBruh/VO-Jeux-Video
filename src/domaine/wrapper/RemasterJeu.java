package domaine.wrapper;

public class RemasterJeu {
    private EditeurJeu editeurJeu;
    private int nbJoueurs;

    public RemasterJeu(EditeurJeu editeurJeu, int nbJoueurs) {
        this.editeurJeu = editeurJeu;
        this.nbJoueurs = nbJoueurs;
    }

    @Override
    public String toString() {
        return editeurJeu.toString() + " dont il y a encore " + nbJoueurs + " joueurs.";
    }

    public double getRanking() {
        return editeurJeu.getRanking();
    }
}
