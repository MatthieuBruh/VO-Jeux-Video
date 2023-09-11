package domaine.base;

import java.util.Objects;

public class Editeur {
    private String nom;

    public Editeur(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Editeur editeur = (Editeur) o;
        return Objects.equals(nom, editeur.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
