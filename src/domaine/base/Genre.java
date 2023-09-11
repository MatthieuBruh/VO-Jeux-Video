package domaine.base;

import java.util.Objects;

public class Genre {
    private String nom;

    public Genre(String nom) {
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
        Genre genre = (Genre) o;
        return Objects.equals(nom, genre.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
