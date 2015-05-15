package mesnews.dao;

import mesnews.util.HibernateUtil;
import java.sql.Date;
import java.util.List;
import mesnews.model.Article;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ArticleDao {

    public static List<Article> listArticles() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        List articles = session.createQuery("from Article").list();
        session.close();
        return articles;
    }

    public static Article readArticle(int id) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Article auteur = (Article) session.get(Article.class, id);
        session.close();
        return auteur;
    }

    public static Article saveArticle(Article article) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(article);
        article.setId(id);
        session.getTransaction().commit();
        session.close();
        return article;
    }

    public static Article updateArticle(Article article) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        session.merge(article);
        session.getTransaction().commit();
        session.close();
        return article;

    }

    public static void deleteArticle(Article article) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        session.delete(article);
        session.getTransaction().commit();
        session.close();
    }

}
