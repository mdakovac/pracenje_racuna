package stanje;

import java.util.List;
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
	
	private List<Stanje> listaStanja;

	private int korisnikId;

	public StanjeBean() {
		updateListaStanja();
	}

	public List<Stanje> getListaStanja() {
		return listaStanja;
	}

	public void setListaStanja(List<Stanje> listaStanja) {
		this.listaStanja = listaStanja;
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
	
	public int getKorisnikId() {
		return korisnikId;
	}

	public void setKorisnikId(int korisnikId) {
		this.korisnikId = korisnikId;
	}

	public void noviUnos() {
		if (naziv.length() <= 0) {
			Message.Display("Naziv ne može biti prazan");
			return;
		}
		
		Session session = HibernateUtil.getSession();
		
		Korisnik k = session.load(Korisnik.class, korisnikId);
		Stanje s = new Stanje(naziv, k, pocetno);

		try {
			session.beginTransaction();
			session.save(s);
			session.getTransaction().commit();
			session.close();
		}catch(ConstraintViolationException e) {
			
			String n = e.getConstraintName();
			
			if(n.equals("STANJE__KORISNIK_ID_NAZIV_UNIQUE")) {
				Message.Display("Stanje s ovim imenom već postoji.");
				return;
			}
				
		}
		
		Message.Display("Stanje dodano.");	
		updateListaStanja();
	}
	
	private void updateListaStanja() {
		FacesContext context = FacesContext.getCurrentInstance();
		korisnikId = (int) context.getExternalContext().getSessionMap().get("id");
		
		listaStanja = Stanje.find(korisnikId);
	}

	public void brisi() {
		System.out.println("brisanje called");
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int id = Integer.parseInt(params.get("stanjeId"));

		Stanje.drop(id);
		
		updateListaStanja();
	}
	
}
