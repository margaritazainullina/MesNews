package mesnews.dao;

import mesnews.util.HibernateUtil;
import java.sql.Date;
import java.util.List;
import mesnews.model.Auteur;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AuteurDao {
    
    public static List<Auteur> listAuteurs() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        List auteurs = session.createQuery("from Auteur").list();
        session.close();
        return auteurs;
    }

    public static Auteur readAuteur(Long id) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Auteur auteur = (Auteur) session.get(Auteur.class, id);
        session.close();
        return auteur;
    }

    public static Auteur saveAuteur(Auteur auteur) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(auteur);
        auteur.setId(id);
        session.getTransaction().commit();
        session.close();
        return auteur;
    }

    public static Auteur updateAuteur(Auteur auteur) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        session.merge(auteur);
        session.getTransaction().commit();
        session.close();
        return auteur;

    }

    public static void deleteAuteur(Auteur auteur) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        session.delete(auteur);
        session.getTransaction().commit();
        session.close();
    }

}
