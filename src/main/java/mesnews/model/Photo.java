/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import mesnews.Lire;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author Margarita
 */
@Entity
@Table(name = "PHOTO")
public class Photo extends News {

    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private int id;
    @Column(name = "format")
    private String format;
    @Column(name = "largeur")
    private int largeur;
    @Column(name = "hauteur")
    private int hauteur;
    @Column(name = "siColoree")
    private boolean siColoree;
    @ManyToMany(mappedBy = "photos", fetch = FetchType.EAGER)
    private Set<Auteur> photo_auteurs = new HashSet<Auteur>();
    @Column(name = "image")
    public byte[] image;

    public Photo(int id, String format, int largeur, int hauteur, boolean siColoree, String titre, LocalDate date, Set<Auteur> auteurs, URL source, byte[] image) {
        super(titre, date, source);
        this.id = id;
        this.format = format;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.siColoree = siColoree;
        this.photo_auteurs = auteurs;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int photo_id) {
        this.id = photo_id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public boolean isSiColoree() {
        return siColoree;
    }

    public void setSiColoree(boolean siColoree) {
        this.siColoree = siColoree;
    }

    public Set<Auteur> getAuteurs() {
        return photo_auteurs;
    }

    public void setAuteurs(Set<Auteur> photo_auteurs) {
        this.photo_auteurs = photo_auteurs;
    }

    public String getAutorsString() {
        StringBuffer sb = new StringBuffer();
        if (photo_auteurs.size() == 0) {
            return "";
        }
        for (Auteur a : photo_auteurs) {
            sb.append(a + " ,");
        }
        return sb.toString().substring(0, sb.length() - 2);
    }

    public File getImage() throws IOException {
        File f = new File("image"+".jpg");
        FileUtils.writeByteArrayToFile(f, Base64.getDecoder().decode(image));
        return f;
    }

    public byte[] getFileImage() {
        return this.image;
    }

    public void setImage(File img) throws IOException {
        this.image = Base64.getEncoder().encode(FileUtils.readFileToByteArray(img));
    }

    public Photo() {
    }

    public File loadPhotoFromURL(URL url) throws IOException {
        File f = new File("image");
        InputStream inputStream = url.openStream();
        OutputStream outputStream = new FileOutputStream(f);
        byte[] buffer = new byte[2048];

        int length = 0;

        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
        return f;
    }

    public void inserer() {
        super.inserer();
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

    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append(source);
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
        sb.append(photo_auteurs);
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(titre).
                append(date).
                append(photo_auteurs).
                append(source).
                append(format).
                append(largeur).
                append(hauteur).
                append(siColoree).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Photo)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Photo rhs = (Photo) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                append(titre, rhs.titre).
                append(date, rhs.date).
                append(photo_auteurs, rhs.photo_auteurs).
                append(source, rhs.source).
                append(format, rhs.format).
                append(largeur, rhs.largeur).
                append(hauteur, rhs.hauteur).
                append(siColoree, rhs.siColoree).
                isEquals();
    }

    @Override
    public Document createDocument() {
        Document doc = new Document();

        // Add source as an unindexed field...
        doc.add(Field.UnIndexed("id", this.id + ""));
        doc.add(Field.UnIndexed("source", this.source.toString()));

        // ...and the content as an indexed field. Note that indexed
        // Text fields are constructed using a Reader. Lucene can read
        // and index very large chunks of text, without storing the
        // entire content verbatim in the index. In this example we
        // can just wrap the content string in a StringReader.
        doc.add(Field.Text("titre", this.titre, true));

        return doc;
    }

    public void addAuteur(Auteur photo_auteur) {
        this.photo_auteurs.add(photo_auteur);
    }
}
