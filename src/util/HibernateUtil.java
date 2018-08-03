package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.Korisnik;
import entities.Transakcija;
import entities.Stanje;

public abstract class HibernateUtil {
	private static SessionFactory sessionFactory = null;

	public static Session getSession() {
		createSessionFactory();
		
		Session session = sessionFactory.openSession();
		return session;
	}
	
	public static void createSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration()
					.configure("hibernate.cfg.xml")
					.addAnnotatedClass(Korisnik.class)
					.addAnnotatedClass(Stanje.class)
					.addAnnotatedClass(Transakcija.class)
					.buildSessionFactory();
		}
	}
}
