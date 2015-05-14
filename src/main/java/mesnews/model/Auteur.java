/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Margarita
 */
@Entity
@Table(name = "AUTEUR")
public class Auteur implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "auteur_id")
    private int auteur_id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "ARTICLE_AUTEUR",
            joinColumns = {
                @JoinColumn(name = "AUTEUR_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "ARTICLE_ID")})
    private Set<Article> articles = new HashSet<Article>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "PHOTO_AUTEUR",
            joinColumns = {
                @JoinColumn(name = "AUTEUR_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "PHOTO_ID")})
    private Set<Photo> photos = new HashSet<Photo>();

    public Auteur(int id, String nom, String prenom) {
        this.auteur_id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() {
        return auteur_id;
    }

    public void setId(int id) {
        this.auteur_id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

    public Auteur() {
    }
}
