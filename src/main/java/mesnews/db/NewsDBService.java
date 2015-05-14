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
import mesnews.dao.ArticleDao;
import mesnews.dao.PhotoDao;
import mesnews.model.Article;
import mesnews.model.Photo;

/**
 *
 * @author Margarita
 */
//Singletone
public final class NewsDBService extends NewsAbstractService implements Serializable {

    public static final NewsDBService INSTANCE = new NewsDBService();

    //private consructor for singletone
    private NewsDBService() {
        if (news == null) {
            news = new TreeSet();
            init();
        }
    }

    public void init() {}
    
    public void load() {
        //load photos
        //load articles
        //merge collections and sort by date
        news.addAll(PhotoDao.listPhotos());
        news.addAll(ArticleDao.listArticles());

    }

}
