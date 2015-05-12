package mesnews.model;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import mesnews.Lire;

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
public abstract class News implements Comparable<News>, Serializable {

    private Auteur auteur;
    private String titre;
    private LocalDate date;
    private URL source;

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

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

    public News(Auteur auteur, String titre, LocalDate date, URL source) {
        this.auteur = auteur;
        this.titre = titre;
        this.date = date;
        this.source = source;
    }

    public News() {
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
        sb.append(this.auteur);
        sb.append("\n");
        sb.append("Source: ");
        sb.append(this.source);
        sb.append("\n");
        return sb.toString();
    }

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
                        return (n1.auteur.toString().compareTo(n2.auteur.toString()));
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
}
