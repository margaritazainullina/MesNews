/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.net.URL;
import java.time.LocalDate;
import mesnews.Lire;

/**
 *
 * @author Margarita
 */
public class Photo extends News {

    private int id;
    private String format;
    private int largeur;
    private int hauteur;
    private boolean siColoree;

    public Photo(int id, String format, int largeur, int hauteur, boolean siColoree, String titre, LocalDate date, Auteur auteur, URL source) {
        super(auteur, titre, date, source);
        this.id = id;
        this.format = format;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.siColoree = siColoree;
    }

    public Photo() {
        super();
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

        }
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
        sb.append(super.getAuteur());
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
