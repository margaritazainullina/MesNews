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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import mesnews.Lire;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
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
    private int article_id;
    @Column(name = "contenu")
    @Type(type = "text")
    private String contenu;
    @Column(name = "siElectronique")
    private boolean siElectronique;
    @ManyToMany(mappedBy = "articles")
    private Set<Auteur> article_auteurs = new HashSet<Auteur>();

    public Article(String titre, LocalDate date, Set<Auteur> auteurs, URL source, String contenu, boolean siElectronique) {
        super(titre, date, source);
        this.contenu = contenu;
        this.article_auteurs = auteurs;
        this.siElectronique = siElectronique;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public Set<Auteur> getArticle_auteurs() {
        return article_auteurs;
    }

    public void setArticle_auteurs(Set<Auteur> article_auteurs) {
        this.article_auteurs = article_auteurs;
    }

    public Article() {
        /*super();
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

         }*/
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

   /* public void getKeyWords() {
        List<Term> terms = new ArrayList<Term>();    //will be filled with non-matched terms
        List<Term> hitTerms = new ArrayList<Term>(); //will be filled with matched terms
        Query q = new MultiTermQuery("cat");
        GetHitTerms(query, searcher, docId, hitTerms, terms);
    }

    void GetHitTerms(Query query, IndexSearcher searcher, int docId, List<Term> hitTerms, List<Term> rest) throws IOException {
        if (query instanceof TermQuery) {
            if ((searcher.explain(query, docId)).isMatch()) {
                rest.add(((TermQuery) query).getTerm());
            } else {
                rest.add(((TermQuery) query).getTerm());
            }
            return;
        }

        if (query instanceof BooleanQuery) {
            BooleanClause[] clauses = ((BooleanQuery) query).getClauses();
            if (clauses == null) {
                return;
            }

            for (BooleanClause bc : clauses) {
                GetHitTerms(bc.getQuery(), searcher, docId, hitTerms, rest);
            }
            return;
        }

        if (query instanceof MultiTermQuery) {
            if (!(query instanceof FuzzyQuery)) //FuzzQuery doesn't support SetRewriteMethod
            {
                ((MultiTermQuery) query).setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
            }

            GetHitTerms(query.rewrite(searcher.getIndexReader()), searcher, docId, hitTerms, rest);
        }
    }*/
}
