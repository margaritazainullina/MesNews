/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.db;

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
import java.util.Set;
import java.util.TreeSet;
import mesnews.dao.ArticleDao;
import mesnews.dao.AuteurDao;
import mesnews.dao.PhotoDao;
import mesnews.model.Article;
import mesnews.model.Auteur;
import mesnews.model.Photo;

/**
 *
 * @author Margarita
 */
//Singletone
public class NewsFileService extends NewsAbstractService implements Serializable {

    public static final NewsFileService INSTANCE = new NewsFileService();

    private static List<Auteur> allAuthors=new ArrayList<>();

    //private consructor for singletone
    private NewsFileService() {
        if (news == null) {
            news = new TreeSet();
        }
    }

    //sauvegarder au fichier
    public static void saveToFile(String filepath) throws FileNotFoundException, IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(filepath);
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(news);
            oos.flush();
        }
    }

    //ouvrir de fichier avec la base
    public static void loadFromFile(String filepath) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filepath);
        Object obj;
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            obj = ois.readObject();
        }
        NewsFileService.news = (TreeSet<News>) obj;
    }

    @Override
    public void add(News n) {
        news.add(n);
    }

    @Override
    public void update(News oldEntry, News newEntry) {
        news.remove(oldEntry);
        news.add(newEntry);
    }

    @Override
    public void delete(News n) {
        news.remove(n);
    }

    @Override
    public void storeAuthor(Auteur a) {
        allAuthors.add(a);
    }

    @Override
    public List<Auteur> getAllAuthors() {
        allAuthors.clear();
        for (News n : news) {
            allAuthors.addAll(n.getAuteurs());
        }
        return allAuthors;
    }
}
