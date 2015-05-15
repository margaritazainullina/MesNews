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
import java.util.TreeSet;

/**
 *
 * @author Margarita
 */
//Singletone
public class NewsFileService extends NewsAbstractService implements Serializable {

    public static final NewsFileService INSTANCE = new NewsFileService();

    //private consructor for singletone
    private NewsFileService() {
        if (news == null) {
            news = new TreeSet();
        }
    }

    //sauvegarder au fichier
    public void serialize(String filepath) throws FileNotFoundException, IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(news);
        oos.flush();
        oos.close();
    }

    //ouvrir de fichier avec la base
    public void deserialize(String filepath) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filepath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        
        this.news = (TreeSet<News>)obj;
    }

}
