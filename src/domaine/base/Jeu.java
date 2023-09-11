package domaine.base;

import java.util.Objects;

public class Jeu {
    private String nom;
    private int annee;
    private double prix;
    private double nbVentes;
    private double ranking;

    private Jeu(Builder builder) {
        nom = builder.nom;
        annee = builder.annee;
        prix = builder.prix;
        nbVentes = builder.nbVentes;
        ranking = builder.ranking;
    }

    @Override
    public String toString() {
        return nom + " " + annee + " au prix de " + prix + ". (nbVentes:" + nbVentes + ", ranking:" + ranking+ ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jeu jeu = (Jeu) o;
        return Objects.equals(nom, jeu.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    public static final class Builder {
        private String nom;
        private int annee;
        private double prix;
        private double nbVentes;
        private double ranking;

        public Builder() {
        }


        public Builder nom(String val) {
            nom = val;
            return this;
        }

        public Builder annee(int val) {
            annee = val;
            return this;
        }

        public Builder prix(double val) {
            prix = val;
            return this;
        }

        public Builder nbVentes(double val) {
            nbVentes = val;
            return this;
        }

        public Builder ranking(double val) {
            ranking = val;
            return this;
        }

        public Jeu build() {
            if (this.nom != null) {
                return new Jeu(this);
            }
            throw new RuntimeException("Il manque soit l'ID soit le nom du jeu");
        }
    }

    public double getRanking() {
        return ranking;
    }
}
