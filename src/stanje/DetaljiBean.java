package stanje;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.primefaces.event.RowEditEvent;

import additionalTypes.ObradenoStanje;
import entities.Transakcija;
import session.SessionVars;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "detaljiBean")
@ViewScoped
public class DetaljiBean {
	private ObradenoStanje stanjeZaPregled;

	@ManagedProperty(value = "#{sessionVarsBean}")
	private SessionVars sessionVars;
	
	private int index;

	public DetaljiBean() {
		System.out.println("DetaljiBean konstruktor");
	}
	
	// ManagedPropertyju se ne može pristupiti u konstruktoru
	@PostConstruct
	public void init() { // Note: method name is your choice.
		System.out.println("DetaljiBean init");
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		index = Integer.parseInt(params.get("index"));
		
		updateStanjeZaPregled();
	}
	
	private void updateStanjeZaPregled() {
		stanjeZaPregled = sessionVars.getListaStanja().get(index);
		stanjeZaPregled.loadTransakcije();
		stanjeZaPregled.removeTransakcija(0);
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
	
	public void brisiTransakciju(int id) {
		// izbrisi iz baze
		Transakcija.drop(id);

		// update i feedback
		sessionVars.updateListaStanja();
		Message.Display("Transakcija obrisana.");
		
		updateStanjeZaPregled();
	}
	
	public void onRowEdit(RowEditEvent event) {
		Transakcija t = (Transakcija) event.getObject();
		
		Session s = HibernateUtil.getSession();
		s.beginTransaction();
		t = (Transakcija) s.merge(t);
		s.getTransaction().commit();
		s.close();
		
		
		sessionVars.updateListaStanja();
		updateStanjeZaPregled();
		
		Message.Display("Ažuriranje uspješno.");
    }

    public void onRowCancel(RowEditEvent event) {
    	
        Message.Display("Ažuriranje otkazano.");
    }
	

}
