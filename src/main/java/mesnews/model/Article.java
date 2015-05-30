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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 *
 * @author Margarita
 */
@Entity
@Table(name = "ARTICLE")
public class Article extends News implements Cloneable {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private int id;
    @Column(name = "contenu")
    @Type(type = "text")
    private String contenu;
    @Column(name = "siElectronique")
    private boolean siElectronique;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "article_auteur",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "auteur_id"))
    private Set<Auteur> article_auteurs = new HashSet<>();

    public Article(String titre, LocalDate date, Set<Auteur> auteurs, URL source, String contenu, boolean siElectronique) {
        super(titre, date, source);
        this.contenu = contenu;
        this.article_auteurs = auteurs;
        this.siElectronique = siElectronique;
    }

    @Override
    public News clone() throws CloneNotSupportedException {
        return (Article) super.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int article_id) {
        this.id = article_id;
    }

    public Set<Auteur> getArticle_auteurs() {
        return article_auteurs;
    }

    public void setArticle_auteurs(Set<Auteur> article_auteurs) {
        this.article_auteurs = article_auteurs;
    }

    public Article() {
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
        sb.append(article_auteurs);
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

    public Set<Auteur> getAuteurs() {
        return article_auteurs;
    }

    public void setAuteurs(Set<Auteur> article_auteurs) {
        this.article_auteurs = article_auteurs;
    }

    public String getAutorsString() {
        if (article_auteurs.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Auteur a : article_auteurs) {
            sb.append(a + " ,");
        }
        return sb.toString().substring(0, sb.length() - 2);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(titre).
                append(date).
                append(article_auteurs).
                append(source).
                append(contenu).
                append(siElectronique).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Article)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Article rhs = (Article) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                append(titre, rhs.titre).
                append(date, rhs.date).
                append(article_auteurs, rhs.article_auteurs).
                append(source, rhs.source).
                append(contenu, rhs.contenu).
                append(siElectronique, rhs.siElectronique).
                isEquals();
    }

    @Override
    public Document createDocument() {
        Document doc = new Document();

        // Add source as an unindexed field...
        doc.add(Field.UnIndexed("source", this.source.toString()));
        doc.add(Field.Text("title", this.titre));

        // ...and the content as an indexed field. Note that indexed
        // Text fields are constructed using a Reader. Lucene can read
        // and index very large chunks of text, without storing the
        // entire content verbatim in the index. In this example we
        // can just wrap the content string in a StringReader.
        doc.add(Field.Text("content", this.contenu, true));
        return doc;
    }

    @Override
    public void addAuteur(Auteur auteur) {
        this.article_auteurs.add(auteur);
    }

    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append(source);
        if (siElectronique) {
            sb.append("\nEn version electronique.");
        } else {
            sb.append("\nN'est pas en version electronique.");
        }
        return sb.toString();
    }
}
