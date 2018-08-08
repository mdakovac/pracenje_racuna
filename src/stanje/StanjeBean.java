package stanje;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import entities.Stanje;
import sessionScope.SessionVars;
import util.Message;

@ManagedBean(name = "stanjeBean")
public class StanjeBean {
	private String naziv;
	private float pocetno = 0;
	
	@ManagedProperty(value = "#{sessionVarsBean}")
	SessionVars sessionVars;

	public StanjeBean() {
		sessionVars.updateListaStanja();
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
	public SessionVars getSessionVars() {
		return sessionVars;
	}
	public void setSessionVars(SessionVars sessionVars) {
		this.sessionVars = sessionVars;
	}

	public void noviUnos() {
		// provjeri naziv
		if (naziv.length() <= 0) {
			Message.Display("Naziv ne može biti prazan");
			return;
		}
		
		// spremi novo stanje u bazu
		int r = Stanje.save(naziv, pocetno, sessionVars.getKorisnikId());
		
		// ako postoje greške ne ispisuj "stanje dodano" i ne osvjezavaj listu stanja
		if(r != 0) {
			return;
		}
		
		// uspjesno dodano
		Message.Display("Stanje dodano.");	
		sessionVars.updateListaStanja();
	}
	
	public void brisi() {
		// dohvati id stanja za brisanje iz POST requesta
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int id = Integer.parseInt(params.get("stanjeId"));
		
		// izbrisi iz baze
		Stanje.drop(id);
		
		// update i feedback
		sessionVars.updateListaStanja();
		Message.Display("Stanje obrisano.");	
	}
}
