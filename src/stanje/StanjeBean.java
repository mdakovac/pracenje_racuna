package stanje;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import entities.Korisnik;
import entities.Stanje;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "stanjeBean")
public class StanjeBean {
	private String naziv;
	private float pocetno = 0;
	
	private int userId;

	public StanjeBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		userId = (int) context.getExternalContext().getSessionMap().get("id");
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv.trim();
	}

	public float getPocetno() {
		return pocetno;
	}

	public void setPocetno(float pocetno) {
		this.pocetno = pocetno;
	}

	public void noviUnos() {

		if (naziv.length() <= 0) {
			Message.Display("Naziv ne može biti prazan");
			return;
		}
		
		Session session = HibernateUtil.getSession();
		
		Korisnik k = session.get(Korisnik.class, userId);
		Stanje s = new Stanje(naziv, k, pocetno);

		try {
			session.beginTransaction();
			session.save(s);
			session.getTransaction().commit();
			session.close();
		}catch(ConstraintViolationException e) {
			
			String n = e.getConstraintName();
			
			if(n.equals("STANJE__KORISNIK_ID_NAZIV_UNIQUE"))
				Message.Display("Stanje s ovim imenom već postoji.");
		}
		
	}

	public void brisi() {
		System.out.println("Brisem.");
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int id = Integer.parseInt(params.get("stanjeId"));

		Session session = HibernateUtil.getSession();

		session.beginTransaction();
		Stanje s = session.load(Stanje.class, id);
		session.delete(s);
		session.getTransaction().commit();
		session.close();
		
	}
	
}
