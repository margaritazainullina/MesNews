/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Article.class)
    @JoinTable(name = "ARTICLE_AUTEUR",
            joinColumns = {
                @JoinColumn(name = "AUTEUR_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "ARTICLE_ID")})
    private Set<Article> articles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Photo.class)
    @JoinTable(name = "PHOTO_AUTEUR",
            joinColumns = {
                @JoinColumn(name = "AUTEUR_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "PHOTO_ID")})
    private Set<Photo> photos = new HashSet<>();

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

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

    public Auteur() {
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(nom).
                append(prenom).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Auteur)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Auteur rhs = (Auteur) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                append(nom, rhs.nom).
                append(prenom, rhs.prenom).
                isEquals();
    }
}
