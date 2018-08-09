package transakcija;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import entities.Transakcija;
import session.SessionVars;
import util.Message;

@ManagedBean(name = "transakcijaBean")
@RequestScoped
public class TransakcijaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3306368693592000870L;

	private BigDecimal iznos = BigDecimal.ZERO;
	private String platitelj = "";
	private String primatelj = "";
	private String model = "";
	private String primateljRacun = "";
	private String brojOdobrenja = "";
	private String opis = "";
	private String tip = "Prihod";

	private List<String> odabranaStanja;

	@ManagedProperty(value = "#{sessionVarsBean}")
	SessionVars sessionVars;

	@PostConstruct
	public void init() {
		// System.out.println("Odabrana stanja al iz Post construct: " +
		// odabranaStanja);
	}

	public TransakcijaBean() {
	}

	public BigDecimal getIznos() {
		return iznos;
	}

	public void setIznos(BigDecimal iznos) {
		this.iznos = iznos;
	}

	public String getPlatitelj() {
		return platitelj;
	}

	public void setPlatitelj(String platitelj) {
		this.platitelj = platitelj;
	}

	public String getPrimatelj() {
		return primatelj;
	}

	public void setPrimatelj(String primatelj) {
		this.primatelj = primatelj;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPrimateljRacun() {
		return primateljRacun;
	}

	public void setPrimateljRacun(String primateljRacun) {
		this.primateljRacun = primateljRacun;
	}

	public String getBrojOdobrenja() {
		return brojOdobrenja;
	}

	public void setBrojOdobrenja(String brojOdobrenja) {
		this.brojOdobrenja = brojOdobrenja;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public SessionVars getSessionVars() {
		return sessionVars;
	}

	public void setSessionVars(SessionVars sessionVars) {
		this.sessionVars = sessionVars;
	}

	public List<String> getOdabranaStanja() {
		return odabranaStanja;
	}

	public void setOdabranaStanja(List<String> odabranaStanja) {
		this.odabranaStanja = odabranaStanja;
	}

	public void unos() {
		// iznos ne moze biti 0 ili manji
		if (iznos.compareTo(BigDecimal.ZERO) <= 0) {
			Message.Display("Nevažeći iznos");
			return;
		}
		// mora postojati stanje na koje ide transakcija
		if (odabranaStanja.isEmpty()) {
			Message.Display("Odaberite stanje za transakciju");
			return;
		}
		// prebaci u negativne ako je rashod
		if (tip.equals("Rashod")) {
			iznos = iznos.negate();
		}

		// spremi i vrati poruku
		Transakcija.save(odabranaStanja, sessionVars.getKorisnikId(), iznos, platitelj, primatelj, model,
				primateljRacun, brojOdobrenja, opis);
		Message.Display("Transakcija dodana.");
		sessionVars.updateListaStanja();
	}

	public void brisi() {
		// dohvati id stanja za brisanje iz POST requesta
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int id = Integer.parseInt(params.get("transakcijaId"));

		// izbrisi iz baze
		Transakcija.drop(id);

		// update i feedback
		sessionVars.updateListaStanja();
		Message.Display("Transakcija obrisana.");
	}
}
