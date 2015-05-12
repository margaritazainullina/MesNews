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
public class ArticleDePresse extends News {

    private String contenu;
    private boolean siElectronique;

    public ArticleDePresse( String titre, LocalDate date, Auteur auteur, URL source, String contenu, boolean siElectronique) {
        super(auteur, titre, date, source);
        this.contenu = contenu;
        this.siElectronique = siElectronique;
    }

    public ArticleDePresse() {
        super();
        System.out.println("Entrez le contenu");
        this.contenu = (Lire.S());

        System.out.println("Entrez 'y' si la nouvelle est electronique, 'n' si non");
        switch (Lire.S()) {
            case "y": {
                this.siElectronique = true;
                break;
            }
            case "n": {
                this.siElectronique = false;
                break;
            }
            default:
                System.err.println("Erreur");

        }
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isSiElectronique() {
        return siElectronique;
    }

    public void setSiElectronique(boolean siElectronique) {
        this.siElectronique = siElectronique;
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
        sb.append("Contenu:");
        sb.append(contenu);
        sb.append("\n");
        sb.append("Info:");
        if (siElectronique) {
            sb.append("electronique\n");
        } else {
            sb.append("noir blanc\n");
        }
        return sb.toString();
    }

}
