package transakcija;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import entities.Stanje;
import entities.Transakcija;
import util.Message;

@ManagedBean(name="transakcijaBean")
@RequestScoped
public class TransakcijaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3306368693592000870L;
	
	private float iznos;
	private String platitelj = "";
	private String primatelj= "";
	private String model = "";
	private String primateljRacun = "";
	private String brojOdobrenja = "";
	private String opis = "";
	private String tip = "Prihod";
	
	private List<Stanje> listaStanja;
	private List<String> odabranaStanja;
	
	private int korisnikId;

	@PostConstruct
    public void init() {
        //System.out.println("Odabrana stanja al iz Post construct: " + odabranaStanja);
    }
	
	public TransakcijaBean() {
		//System.out.println("Odabrana stanja al iz konstruktora: " + odabranaStanja);
		FacesContext context = FacesContext.getCurrentInstance();
		korisnikId = (int) context.getExternalContext().getSessionMap().get("id");
		listaStanja = Stanje.find(korisnikId);
	}
	
	public float getIznos() {
		return iznos;
	}

	public void setIznos(float iznos) {
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
	
	public List<Stanje> getListaStanja() {
		return listaStanja;
	}

	public void setListaStanja(List<Stanje> listaStanja) {
		this.listaStanja = listaStanja;
	}

	public List<String> getOdabranaStanja() {
		return odabranaStanja;
	}

	public void setOdabranaStanja(List<String> odabranaStanja) {
		this.odabranaStanja = odabranaStanja;
	}

	public void unos() {
		if(this.iznos <= 0) {
			Message.Display("Nevažeći iznos");
			return;
		}
		if(odabranaStanja.isEmpty()) {
			Message.Display("Odaberite stanje za transakciju");
			return;
		}
		
		Transakcija t = new Transakcija(iznos, platitelj, primatelj, model, primateljRacun, brojOdobrenja, opis);
		
		t.dodajNaStanja(odabranaStanja, korisnikId);
		
	}
}
