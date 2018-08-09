package stanje;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import additionalTypes.ObradenoStanje;
import entities.Stanje;
import session.SessionVars;

@ManagedBean(name = "detaljiBean")
@ViewScoped
public class DetaljiBean {
	private ObradenoStanje stanjeZaPregled;
	private Stanje stanje;

	@ManagedProperty(value = "#{sessionVarsBean}")
	private SessionVars sessionVars;

	public DetaljiBean() {

	}
	
	// ManagedPropertyju se ne može pristupiti u konstruktoru
	@PostConstruct
	public void init() { // Note: method name is your choice.
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(params.get("index"));

		stanjeZaPregled = sessionVars.getListaStanja().get(index);
		
		updateStanje();
	}
	
	public void updateStanje() {
		stanje = Stanje.get(stanjeZaPregled.getId());
		stanje = Stanje.loadTransakcije(stanje);
	}

	public ObradenoStanje getStanjeZaPregled() {
		return stanjeZaPregled;
	}

	public void setStanjeZaPregled(ObradenoStanje stanjeZaPregled) {
		this.stanjeZaPregled = stanjeZaPregled;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public SessionVars getSessionVars() {
		return sessionVars;
	}

	public void setSessionVars(SessionVars sessionVars) {
		this.sessionVars = sessionVars;
	}

	

}
