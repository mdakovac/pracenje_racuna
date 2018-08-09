package stanje;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import additionalTypes.ObradenoStanje;
import entities.Transakcija;
import session.SessionVars;
import util.Message;

@ManagedBean(name = "detaljiBean")
@ViewScoped
public class DetaljiBean {
	private ObradenoStanje stanjeZaPregled;

	@ManagedProperty(value = "#{sessionVarsBean}")
	private SessionVars sessionVars;
	
	private int index;

	public DetaljiBean() {
		
	}
	
	// ManagedPropertyju se ne može pristupiti u konstruktoru
	@PostConstruct
	public void init() { // Note: method name is your choice.
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		index = Integer.parseInt(params.get("index"));
		
		updateStanjeZaPregled();
	}
	
	private void updateStanjeZaPregled() {
		stanjeZaPregled = sessionVars.getListaStanja().get(index);
		stanjeZaPregled.loadTransakcije();
	}

	public ObradenoStanje getStanjeZaPregled() {
		return stanjeZaPregled;
	}

	public void setStanjeZaPregled(ObradenoStanje stanjeZaPregled) {
		this.stanjeZaPregled = stanjeZaPregled;
	}

	public SessionVars getSessionVars() {
		return sessionVars;
	}

	public void setSessionVars(SessionVars sessionVars) {
		this.sessionVars = sessionVars;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void brisiTransakciju() {
		// dohvati id stanja za brisanje iz POST requesta
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int id = Integer.parseInt(params.get("transakcijaId"));

		// izbrisi iz baze
		Transakcija.drop(id);

		// update i feedback
		sessionVars.updateListaStanja();
		Message.Display("Transakcija obrisana.");
		
		updateStanjeZaPregled();
	}
	

}
