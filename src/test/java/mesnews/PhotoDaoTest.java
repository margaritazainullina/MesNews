package mesnews;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mesnews.dao.AuteurDao;
import mesnews.dao.PhotoDao;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.model.Auteur;
import mesnews.model.Photo;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Margarita
 */
public class PhotoDaoTest {

    static List<Photo> photos;
    static Photo testPhoto;
    static Auteur a;
    static NewsDBService ns;

    public PhotoDaoTest() {
    }

    @BeforeClass
    public static void setUp() throws Exception {

        NewsAbstractService.initWithConcreteService(NewsDBService.INSTANCE);
        ns = NewsDBService.INSTANCE;
        //create test entry
        a = AuteurDao.saveAuteur(new Auteur(0, "Test1", "Test1"));
        HashSet<Auteur> auteurs = new HashSet<Auteur>();
        auteurs.add(a);
        testPhoto = new Photo(".jpg", 600, 800, true, "Some photo", LocalDate.now(),auteurs, new URL("http://123.com"), null);
        //read all entrues to collection
        photos = PhotoDao.listPhotos();
    }

    @AfterClass
    public static void shutDown() {
       // ns.deleteAuteur(a);
    }

    @Test
    public void CRUDPhotoTest() throws CloneNotSupportedException {
        ns.add(testPhoto);
        photos.add(testPhoto);

        //list from db and collection and compare
        assertEquals(photos, PhotoDao.listPhotos());

        //update entry
        Photo old = (Photo) testPhoto.clone();
        testPhoto.setHauteur(1500);
        testPhoto.setLargeur(1000);
        //update in collection
        photos.set(photos.size() - 1, testPhoto);
        //update in db
        ns.update(old, testPhoto);

        //list from db and collection and compare
        assertEquals(photos, PhotoDao.listPhotos());

        //delete in collection
        photos.remove(testPhoto);
        //update in db
        ns.delete(testPhoto);

        //list from db and collection and compare
        assertEquals(photos, PhotoDao.listPhotos());
    }
}
