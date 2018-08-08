package session;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import additionalTypes.ObradenoStanje;
import entities.Stanje;

@ManagedBean(name = "sessionVarsBean")
@SessionScoped
public class SessionVars {
	private int korisnikId;
	private List<ObradenoStanje> listaStanja;
	
	public SessionVars() {
		System.out.println("Session vars instantiated");
		FacesContext context = FacesContext.getCurrentInstance();
		korisnikId = (int) context.getExternalContext().getSessionMap().get("id");
		listaStanja = Stanje.findAll(korisnikId);
	}
	
	public int getKorisnikId() {
		return korisnikId;
	}
	public void setKorisnikId(int korisnikId) {
		this.korisnikId = korisnikId;
	}
	public List<ObradenoStanje> getListaStanja() {
		return listaStanja;
	}
	public void setListaStanja(List<ObradenoStanje> listaStanja) {
		this.listaStanja = listaStanja;
	}	
	
	public void updateListaStanja() {
		listaStanja = Stanje.findAll(korisnikId);
	}
	
}
