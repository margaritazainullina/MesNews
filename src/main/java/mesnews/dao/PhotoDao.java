package mesnews.dao;

import mesnews.util.HibernateUtil;
import java.util.List;
import mesnews.model.Photo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PhotoDao {

    public static List<Photo> listPhotos() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        List photos = session.createQuery("from Photo").list();
        session.close();
        return photos;
    }

    public static Photo readPhoto(Long id) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        Photo auteur = (Photo) session.get(Photo.class, id);
        session.close();
        return auteur;
    }

    public static Photo savePhoto(Photo photo) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        session.beginTransaction();

        Integer id = (Integer) session.save(photo);
        photo.setPhoto_id(id);
        session.getTransaction().commit();
        session.close();
        return photo;
    }

    public static Photo updatePhoto(Photo photo) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        session.beginTransaction();

        session.merge(photo);

        session.getTransaction().commit();

        session.close();
        return photo;

    }

    public static void deletePhoto(Photo photo) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        session.beginTransaction();

        session.delete(photo);

        session.getTransaction().commit();

        session.close();
    }

}
