/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import mesnews.Lire;

/**
 *
 * @author Margarita
 */
@Entity
@Table(name = "PHOTO")
public class Photo extends News {

    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private int photo_id;
    @Column(name = "format")
    private String format;
    @Column(name = "largeur")
    private int largeur;
    @Column(name = "hauteur")
    private int hauteur;
    @Column(name = "siColoree")
    private boolean siColoree;
    @ManyToMany(mappedBy = "photos")
    private Set<Auteur> photo_auteurs = new HashSet<Auteur>();

    public Photo(int id, String format, int largeur, int hauteur, boolean siColoree, String titre, LocalDate date, Set<Auteur> auteurs, URL source) {
        super(titre, date, source);
        this.photo_id = id;
        this.format = format;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.siColoree = siColoree;
        this.photo_auteurs = auteurs;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public boolean isSiColoree() {
        return siColoree;
    }

    public void setSiColoree(boolean siColoree) {
        this.siColoree = siColoree;
    }

    public Set<Auteur> getAuteurs() {
        return photo_auteurs;
    }

    public void setAuteurs(Set<Auteur> photo_auteurs) {
        this.photo_auteurs = photo_auteurs;
    }

    public Photo() {
       /* super();
        System.out.println("Entrez le format");
        this.format = (Lire.S());

        do {
            System.out.println("Entrez le largeur");
            try {
                this.largeur = (Lire.i());
            } catch (Exception e) {
                System.err.println("Erreur");
            }
        } while (this.largeur == 0);
        do {
            System.out.println("Entrez le hauteur");
            try {
                this.hauteur = (Lire.i());
            } catch (Exception e) {
                System.err.println("Erreur");
            }
        } while (this.hauteur == 0);

        System.out.println("Entrez 'y' si la photo est coloree, 'n' si non");
        switch (Lire.S()) {
            case "y": {
                this.siColoree = true;
                break;
            }
            case "n": {
                this.siColoree = false;
                break;
            }
            default:
                System.err.println("Erreur");

        }*/
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Information:\n");
        sb.append("Titre: ");
        sb.append(super.getTitre());
        sb.append("\n");
        sb.append("Date: ");
        sb.append(super.getDate());
        sb.append("\n");
        sb.append("Auteur: ");
        sb.append(photo_auteurs);
        sb.append("\n");
        sb.append("Source: ");
        sb.append(super.getSource());
        sb.append("\n");
        sb.append("Info:");
        sb.append("\n");
        sb.append(format);
        sb.append(", ");
        sb.append(largeur);
        sb.append("*");
        sb.append(hauteur);
        if (siColoree) {
            sb.append(", coloree\n");
        } else {
            sb.append(", noir blanc\n");
        }
        return sb.toString();
    }

}
