package mesnews;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mesnews.dao.AuteurDao;
import mesnews.dao.ArticleDao;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.model.Auteur;
import mesnews.model.Article;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Margarita
 */
public class ArticleDaoTest {

    static List<Article> articles;
    static Article testArticle;
    static Auteur a;
    static NewsDBService ns;

    public ArticleDaoTest() {
    }

    @BeforeClass
    public static void setUp() throws Exception {

        NewsAbstractService.initWithConcreteService(NewsDBService.INSTANCE);
        ns = NewsDBService.INSTANCE;
        //create test entry
        a = AuteurDao.saveAuteur(new Auteur(0, "Test", "Test"));
        HashSet<Auteur> auteurs = new HashSet<Auteur>();
        auteurs.add(a);
        testArticle = new Article("Article 1", LocalDate.now(), auteurs, new URL("http://123.com/1"), "content", false);
        //read all entrues to collection
        articles = ArticleDao.listArticles();
    }

    @AfterClass
    public static void shutDown() {
        ns.deleteAuteur(a);
    }

    @Test
    public void CRUDArticleTest() throws CloneNotSupportedException {
        ns.add(testArticle);
        articles.add(testArticle);

        //list from db and collection and compare
        assertEquals(articles, ArticleDao.listArticles());

        //update entry
        Article old = (Article) testArticle.clone();
        testArticle.setContenu("another content");
        //update in collection
        articles.set(articles.size() - 1, testArticle);
        //update in db
        ns.update(old, testArticle);

        //list from db and collection and compare
        assertEquals(articles, ArticleDao.listArticles());

        //delete in collection
        articles.remove(testArticle);
        //update in db
        ns.delete(testArticle);

        //list from db and collection and compare
        assertEquals(articles, ArticleDao.listArticles());     

    }
}
