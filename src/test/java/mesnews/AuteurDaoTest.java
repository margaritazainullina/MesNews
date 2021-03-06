package mesnews;

import java.util.ArrayList;
import java.util.List;
import mesnews.dao.AuteurDao;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.model.Auteur;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Margarita
 */
public class AuteurDaoTest {

    static List<Auteur> auteurs;
    static NewsDBService ns;
    static Auteur testAuteur;

    public AuteurDaoTest() {
    }

    @BeforeClass
    public static void setUp() throws Exception {
        NewsAbstractService.initWithConcreteService(NewsDBService.INSTANCE);
        ns = NewsDBService.INSTANCE;
        //create test entry
        testAuteur = new Auteur(0, "Test", "Test");
        //read all entrues to collection
        auteurs = AuteurDao.listAuteurs();
    }

    @Test
    public void CRUDAuteurTest() {
        //save to db
        AuteurDao.saveAuteur(testAuteur);
        //update autogenerated id
        int id = AuteurDao.listAuteurs().get(AuteurDao.listAuteurs().size() - 1).getId();
        testAuteur.setId(id);
        //save to collection
        auteurs.add(testAuteur);

        //read from db and collection and compare
        assertEquals(testAuteur, AuteurDao.readAuteur(testAuteur.getId()));
        //list from db and collection and compare
        assertEquals(auteurs, AuteurDao.listAuteurs());

        //update entry
        testAuteur.setNom("Test update");
        testAuteur.setPrenom("Test update");
        //update in collection
        auteurs.set(auteurs.size() - 1, testAuteur);
        //update in db
        AuteurDao.updateAuteur(testAuteur);

        //read from db and collection and compare
        assertEquals(testAuteur, AuteurDao.readAuteur(testAuteur.getId()));
        //list from db and collection and compare
        assertEquals(auteurs, AuteurDao.listAuteurs());

        //delete in collection
        auteurs.remove(testAuteur);
        //update in db
        AuteurDao.deleteAuteur(testAuteur);

        //list from db and collection and compare
        assertEquals(auteurs, AuteurDao.listAuteurs());
    }
}
