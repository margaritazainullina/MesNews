/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.db;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import mesnews.dao.ArticleDao;
import mesnews.dao.AuteurDao;
import mesnews.dao.PhotoDao;
import static mesnews.db.NewsAbstractService.news;
import mesnews.model.Article;
import mesnews.model.Auteur;
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
        news.addAll(ArticleDao.listArticles());
        news.addAll(PhotoDao.listPhotos());
    }
    
    @Override
    public void add(News n) {
        if (n instanceof Article) {
            ArticleDao.saveArticle((Article) n);
        } else if (n instanceof Photo) {
            PhotoDao.savePhoto((Photo) n);
        }
    }
    
    @Override
    public void update(News oldEntry, News newEntry) {
        if (newEntry instanceof Article) {
            ArticleDao.updateArticle((Article) newEntry);
        } else if (newEntry instanceof Photo) {
            PhotoDao.updatePhoto((Photo) newEntry);
        }
    }
    
    @Override
    public void delete(News n) {
        n.setAuteurs(new HashSet<Auteur>());
        if (n instanceof Article) {
            ArticleDao.updateArticle((Article) n);
            ArticleDao.deleteArticle((Article) n);
        } else if (n instanceof Photo) {
            PhotoDao.updatePhoto((Photo) n);
            PhotoDao.deletePhoto((Photo) n);
        }
    }
    
    @Override
    public void storeAuthor(Auteur a) {
        AuteurDao.saveAuteur(a);
    }
    
    @Override
    public List<Auteur> getAllAuthors() {
        return AuteurDao.listAuteurs();
    }
    
    public void deleteAuteur(Auteur a) {
        AuteurDao.deleteAuteur(a);
    }
}
