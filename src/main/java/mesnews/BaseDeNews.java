/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews;

import mesnews.model.News;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author Margarita
 */
//Singletione
public class BaseDeNews implements Serializable {

    public static final BaseDeNews INSTANCE = new BaseDeNews();
    private TreeSet<News> news;

    //private consructor for singletone
    private BaseDeNews() {
        if (news == null) {
            news = new TreeSet();
        }
    }

    //getteurs and setteurs pour collection
    public TreeSet<News> getNews() {
        return news;
    }

    public void setNews(TreeSet<News> news) {
        this.news = news;
    }

    //ajoute une nouvelle
    public void ajoute(News n) {
        news.add(n);
    }

    public void afficher() {
        if (news.isEmpty()) {
            System.out.println("La base est vide!");
        } else {
            System.out.println("Un total de " + news.size() + " entr�es");
        }
        for (News n : news) {
            System.out.println(n);
        }
    }

    public TreeSet<News> rechercher(String nom) {
        TreeSet<News> results = new TreeSet<>();
        String[] cles = nom.split("\\W+");
        for (News n : news) {
            for (String cle : cles) {
                if (n.getTitre().contains(cle)) {
                    results.add(n);
                }
            }
        }
        return results;
    }

    //sauvegarder au fichier
    public void serialize(String filepath) throws FileNotFoundException, IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    //ouvrir de fichier avec la base
    public void deserialize(String filepath) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filepath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        BaseDeNews loaded = (BaseDeNews) obj;
        this.news = loaded.news;
    }

}
