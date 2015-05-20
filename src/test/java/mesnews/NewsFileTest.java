/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import mesnews.db.NewsFileService;
import mesnews.model.Article;
import mesnews.model.Auteur;
import mesnews.model.News;
import mesnews.model.Photo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Margarita
 */
public class NewsFileTest {

    NewsFileService news;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    public NewsFileTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        news = NewsFileService.INSTANCE;
    }

    @After
    public void tearDown() {
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void creerTest() {
        news = null;
        news = NewsFileService.INSTANCE;
        assertNotNull(news);
    }

    @Test(expected = IOException.class)
    public void sauvegarderExceptionTest() throws IOException {
        String path = "\n\n\n";
        news.serialize(path);
    }

    @Test
    public void ajouterTest() throws MalformedURLException {
        LocalDate l = LocalDate.of(2014, Month.JANUARY, 1);
        URL url = new URL("http://news.com/article1");
        //String titre, LocalDate date, Set<Auteur> auteurs, URL source, String contenu, boolean siElectronique) {
        Set<Auteur> auteurs = new HashSet<>();
        auteurs.add(new Auteur(0, "Spasskaya", "Rita"));

        news.ajouter(new Article("Breaking news!", l, auteurs, url, "contenu", true));
        news.ajouter(new Photo(0, ".jpg", 800, 600, true, "Some Photo", l, auteurs, url, null));

        assertEquals(news.getNews().size(), 2);
    }

    @Test(expected = DateTimeException.class)
    public void ajouterExceptionDateTest() throws MalformedURLException {
        LocalDate l = LocalDate.of(0, Month.JANUARY, -1);
        URL url = new URL("http://news.com/article1");
        news.ajouter(new Photo(0, ".jpg", 800, 600, true, "Some Photo", l, null, url, null));
    }

    @Test(expected = MalformedURLException.class)
    public void ajouterExceptionUrlTest() throws MalformedURLException {
        URL url = new URL("\n");
        LocalDate l = LocalDate.of(2014, Month.JANUARY, 1);
        news.ajouter(new Photo(0, ".jpg", 800, 600, true, "Some Photo", l, null, url, null));
    }

    @Test
    public void sauvegarderOuvrirTest() throws IOException, FileNotFoundException, ClassNotFoundException {
        int hash1 = news.hashCode();
        String path = "base.txt";
        news.serialize(path);
        news.deserialize(path);
        int hash2 = news.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void afficherTest() {
        news.setNews(new TreeSet<News>());
        news.afficher();
        assertEquals("La base est vide!", outContent.toString().trim());
    }

}
