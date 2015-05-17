package mesnews.model;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import mesnews.Lire;
import static mesnews.db.NewsAbstractService.idx;
import mesnews.util.LocalDatePersistenceConverter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author Margarita
 */
/*@ Une classe News.java qui comporte tous les attributs
 * n´ecessaires `a la gestion d’une actualit´e, `a savoir : titre, date,
 * auteur, source. Pour le moment, vous pouvez vous contenter de la date sans
 * pr´eciser l’heure exacte, grˆace `a la classe LocalDate. La source est
 * une URL qui permet de situer sur Internet la page Web d’o`u provient
 * l’information. Vous utiliserez la classe URL.
 */
@MappedSuperclass
public abstract class News implements Comparable<News>, Serializable {

    @Column(name = "titre")
    protected String titre;
    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "date")
    protected LocalDate date;
    @Column(name = "source")
    protected URL source;
    @Transient
    Set<String> keyWords= new HashSet<>();

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public URL getSource() {
        return source;
    }

    public void setSource(URL source) {
        this.source = source;
    }

    public Set<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    public News(String titre, LocalDate date, URL source) {
        this.titre = titre;
        this.date = date;
        this.source = source;
    }

    public News() {
    }

    public void inserer() {
        System.out.println("Entrez le titre");
        this.titre = Lire.S();

        //parce date
        do {
            try {
                System.out.println("Entrez le date en format yyyy-mm-dd");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.date = LocalDate.parse(Lire.S(), formatter);
            } catch (Exception e) {
                System.err.println("Erreur dans l'information!");
            }
        } while (this.date == null);

        System.out.println("Entrez l'auteur");
         //TODO
        //this.auteur = Lire.S();

        //parce url
        do {
            try {
                System.out.println("Entrez la source");
                this.source = new URL(Lire.S());
            } catch (Exception e) {
                System.err.println("Erreur dans l'information!");
            }
        } while (this.source == null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Information:\n");
        sb.append("Titre: ");
        sb.append(this.titre);
        sb.append("\n");
        sb.append("Date: ");
        sb.append(this.date);
        sb.append("\n");
        sb.append("Auteur: ");
        sb.append("\n");
        sb.append("Keywords: ");
        sb.append(this.keyWords);
        sb.append("\n");
        sb.append("Source: ");
        sb.append(this.source);
        sb.append("\n");
        return sb.toString();
    }

    //default - sort by date
    @Override
    public int compareTo(News another) {
        return this.titre.compareTo(another.titre);
    }

    public enum NewsComparator implements Comparator<News> {

        TITRE {
                    @Override
                    public int compare(News n1, News n2) {
                        return (n1.titre.compareTo(n2.titre));
                    }
                },
        DATE {
                    @Override
                    public int compare(News n1, News n2) {
                        return (n1.date.compareTo(n2.date));
                    }
                },
        AUTEUR {
                    @Override
                    public int compare(News n1, News n2) {
                        return 0;//(n1.auteurs.toString().compareTo(n2.auteurs.toString()));
                    }
                },
        SOURCE {
                    @Override
                    public int compare(News n1, News n2) {
                        return (n1.source.toString().compareTo(n2.source.toString()));
                    }
                },
        KEYWORDS {
                    @Override
                    public int compare(News n1, News n2) {
                        //TODO: compare!
                        return 0;
                    }
                };
    }

    /**
     * Make a Document object with an un-indexed title field and an indexed
     * content field.
     *
     * @return
     */
    public abstract Document createDocument();
}
