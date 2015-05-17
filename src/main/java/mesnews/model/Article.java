/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import mesnews.Lire;
import static mesnews.db.NewsAbstractService.idx;
import static mesnews.db.NewsAbstractService.newsIndex;
import static mesnews.db.NewsAbstractService.searcher;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.BaseCompositeReader;
import org.apache.lucene.index.ExitableDirectoryReader;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.hibernate.annotations.Type;

/**
 *
 * @author Margarita
 */
@Entity
@Table(name = "ARTICLE")
public class Article extends News {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private int id;
    @Column(name = "contenu")
    @Type(type = "text")
    private String contenu;
    @Column(name = "siElectronique")
    private boolean siElectronique;
    @ManyToMany(mappedBy = "articles", fetch = FetchType.EAGER)
    private Set<Auteur> article_auteurs = new HashSet<Auteur>();

    public Article(String titre, LocalDate date, Set<Auteur> auteurs, URL source, String contenu, boolean siElectronique) {
        super(titre, date, source);
        this.contenu = contenu;
        this.article_auteurs = auteurs;
        this.siElectronique = siElectronique;
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

    public void inserer() {
        super.inserer();
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

}
