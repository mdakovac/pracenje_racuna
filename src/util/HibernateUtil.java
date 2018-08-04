package util;

import java.util.TimeZone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.Korisnik;
import entities.Stanje;
import entities.Transakcija;

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
			

			sessionFactory.withOptions().jdbcTimeZone(TimeZone.getTimeZone("GMT+1"));
		}
	}
}
