/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews;

import mesnews.model.News;
import mesnews.model.ArticleDePresse;
import mesnews.model.Photo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import mesnews.service.AuteurService;

/**
 *
 * @author Margarita
 */
public class MesNews {

    static BaseDeNews base;

    public static void main(String[] args) {
       // afficherMenu();
         // Read
     System.out.println("******* READ *******");
     List employees = AuteurService.listAuteurs();
     System.out.println("Total Employees: " + employees.size());
    }

    public static void afficherMenu() {
        int n = 0;
        do {
            System.out.println("Entrez un nombre de la commande:\n 1-Creer\n 2-Ouvrir\n 3-Sauvegarder\n 4-Afficher \n 5-Inserer\n 6-Supprimer\n 7-Rechercher\n 8-Quitter");
            n = Lire.i();
            switch (n) {
                case 1:
                    creer();
                    break;
                case 2:
                    ouvrir();
                    break;
                case 3:
                    sauvegarder();
                    break;
                case 4:
                    afficher();
                    break;
                case 5:
                    inserer();
                    break;
                case 6:
                    supprimer();
                    break;
                case 7:
                    rechercher();
                    break;
                case 8:
                    quitter();
                    break;
                default:
                    System.out.println("Une mauvaise commande!");
            }
        } while (n != 8);
    }

    /*cree une nouvelle base dâ€™actualitee, câ€™est-`a-dire un ensemble de news dans la collection ;
     attention, cette action est `a rÂ´ealiser une seule fois au dÂ´ebut, quand la base nâ€™existe pas encore.*/
    public static void creer() {
        base = BaseDeNews.INSTANCE;
        System.out.println("La base de donnee est cree");
    }

    /*charge une base dâ€™actualitÂ´e existante qui a Â´etÂ´e enregistrÂ´ee prÂ´ealablement sur le disque
     dur de lâ€™ordinateur.*/
    public static void ouvrir() {
        System.out.println("Entrez le chemin vers le fichier:");
        String filepath = Lire.S();
        try {
            base.deserialize(filepath);
            System.out.println("Success!");
        } catch (FileNotFoundException ex) {
            System.err.println("Le fichier est introuvable");
        } catch (IOException ex) {
            System.err.println("L'exception d'ouvrir");
        } catch (ClassNotFoundException ex) {
            System.out.println("Le class est introuvable");
        }

    }

    /*Sauvegarder : sauvegarde la base courante sur le disque dur de lâ€™ordinateur.*/
    public static void sauvegarder() {
        System.out.println("Entrez le chemin vers le fichier:");
        String filepath = Lire.S();
        try {
            base.serialize(filepath);
            System.out.println("Success!");
        } catch (FileNotFoundException ex) {
            System.err.println("Le fichier est introuvable");
        } catch (IOException ex) {
            System.err.println("L'exception d'ouvrir");
        }

    }

    /* affche le contenu total de la base.*/
    public static void afficher() {
        /*Toujours dans la classe principale, testez votre programme en faisant a!cher le contenu de lâ€™actualitÂ´e
         n `a lâ€™aide de la mÂ´ethode afficher().*/
        try {
            base.afficher();
        } catch (Exception ex) {
            System.err.println("Base n'est pas cree!");
        }
    }

    /*ins`ere une nouvelle actualitÂ´e dans la base (statut dâ€™un ami, article de presse, photo. . . ).*/
    public static void inserer() {
        System.out.println("Quelle type de nouvelle vous voulez cree: 1-article, 2-photo");
        News n = null;
        switch (Lire.i()) {
            case 1: {
                n = new ArticleDePresse();
                break;
            }
            case 2: {
                n = new Photo();
                break;
            }
            default:
                throw new IllegalArgumentException();

        }

        base.ajoute(n);
        System.out.println("La nouvelle est ajoutee");
    }

    /*supprime une actualitÂ´e de la base.*/
    public static void supprimer() {
        int i = 0;
        //for storing key-numbers of article, values - instance of News in order to delete
        HashMap<Integer, News> hm = new HashMap();

        for (News n : base.getNews()) {
           // if(n instanceof Photo){}
            i++;
            hm.put(i, n);
            System.out.println(i + ":" + n);
        }
        if (base.getNews().isEmpty()) {
            System.out.println("La base est vide!");
            return;
        }
        System.out.println("Entrez une numero de nouvelle pour supprimer");
        int index = Lire.i();
        try {
            //remove is news with this number is present
            base.getNews().remove(hm.get(index));
            System.out.println("La nouvelle est suprimee");
        } catch (Exception e) {
            System.err.println("Il n'y a pas de nouvelle avec ce numero!");
        }
    }

    /* permet de recherche si une actualitÂ´e existe en fonction du titre, ou mË†eme de
     mots-clefs, entrÂ´es par lâ€™utilisateur.*/
    public static void rechercher() {
        String requete = Lire.S();
        base.rechercher(requete);
    }

    public static void quitter() {
        System.out.println("Au revoir!");
    }
}
