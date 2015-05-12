package mesnews.service;

import mesnews.service.HibernateUtil;
import java.sql.Date;
import java.util.List;
import mesnews.model.Auteur;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AuteurService {

    /*public static void main(String[] args) {
		
		
     // Read
     System.out.println("******* READ *******");
     List employees = list();
     System.out.println("Total Employees: " + employees.size());
		
		
     // Write
     System.out.println("******* WRITE *******");
     Employee empl = new Employee("Jack", "Bauer", new Date(System.currentTimeMillis()), "911");
     empl = save(empl);
     empl = read(empl.getId());
     System.out.printf("%d %s %s \n", empl.getId(), empl.getFirstname(), empl.getLastname());
		
		
		
     // Update
     System.out.println("******* UPDATE *******");
     Employee empl2 = read(1l); // read employee with id 1
     System.out.println("Name Before Update:" + empl2.getFirstname());
     empl2.setFirstname("James");
     update(empl2);	// save the updated employee details
		
     empl2 = read(1l); // read again employee with id 1
     System.out.println("Name Aftere Update:" + empl2.getFirstname());
		
		
     // Delete
     System.out.println("******* DELETE *******");
     delete(empl); 
     Employee empl3 = read(empl.getId());
     System.out.println("Object:" + empl3);
     }
	
     */
    public static List listAuteurs() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        List employees = session.createQuery("from Auteur").list();
        session.close();
        return employees;
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

        Long id = (Long) session.save(auteur);
        //TODO: !!!
        auteur.setId(Integer.parseInt(id.toString()));
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
