package edu.polytech.balancetalan;

public class LanIssue {
    private String titre;
    private String categorie;
    private String emplacement;

    public LanIssue(String titre, String categorie, String emplacement) {
        this.titre = titre;
        this.categorie = categorie;
        this.emplacement = emplacement;
    }

    public String getTitre() {
        return titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    @Override
    public String toString() {
        return titre + " - " + categorie + " @ " + emplacement;
    }
}
