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
import java.util.TreeSet;
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
public class BaseDeNewsTest {
/*
    BaseDeNews news;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    public BaseDeNewsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        news = BaseDeNews.INSTANCE;
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
        news = BaseDeNews.INSTANCE;
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
        news.ajoute(new Photo("Breaking news!", l, "Margarita", url, ".jpg", 600, 800, true));
        assertEquals(news.getNews().size(), 1);
    }

    @Test(expected = DateTimeException.class)
    public void ajouterExceptionDateTest() throws MalformedURLException {
        LocalDate l = LocalDate.of(0, Month.JANUARY, -1);
        URL url = new URL("http://news.com/article1");
        news.ajoute(new Photo("Breaking news!", l, "Margarita", url, ".jpg", 600, 800, true));
    }

    @Test(expected = MalformedURLException.class)
    public void ajouterExceptionUrlTest() throws MalformedURLException {
        URL url = new URL("\n");
        LocalDate l = LocalDate.of(2014, Month.JANUARY, 1);
        news.ajoute(new Photo("Breaking news!", l, "Margarita", url, ".jpg", 600, 800, true));
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
    }*/

}
