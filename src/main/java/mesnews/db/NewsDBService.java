/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.db;

import java.io.Serializable;
import java.util.TreeSet;
import mesnews.dao.ArticleDao;
import mesnews.dao.PhotoDao;
import static mesnews.db.NewsAbstractService.news;
import mesnews.model.Article;
import mesnews.model.News;
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
        }
    }

    public static void load() {
        //load photos
        //load articles
        //merge collections and sort by date
        news.addAll(PhotoDao.listPhotos());
        news.addAll(ArticleDao.listArticles());
    }

    @Override
    public void ajouter(News n) {
        news.add(n);
        if (n instanceof Article) {
            ArticleDao.saveArticle((Article) n);
        } else if (n instanceof Photo) {
            PhotoDao.savePhoto((Photo) n);
        }
    }
}
