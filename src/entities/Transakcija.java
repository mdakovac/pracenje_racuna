package entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

@Entity
@Table(name="transakcija")
public class Transakcija {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="transakcija_id")
	private int id;
	
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(	name = "stanje_transakcija", 
				joinColumns = { @JoinColumn(name = "transakcija_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "stanje_id") })
	private List<Stanje> stanja;
	
	
	
	@Column(name="iznos")
	@NotNull
	private float iznos;
	
	@Column(name="platitelj")
	private String platitelj;
	
	@Column(name="primatelj")
	private String primatelj;
	
	@Column(name="model")
	private String model;
	
	@Column(name="broj_racuna_primatelja")
	private String primateljRacun;
	
	@Column(name="poziv_na_broj_odobrenja")
	private String brojOdobrenja;
	
	@Column(name="opis")
	private String opis;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Stanje> getStanja() {
		return stanja;
	}

	public void setStanja(List<Stanje> stanja) {
		this.stanja = stanja;
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

	public Transakcija(@NotNull float iznos, String platitelj, String primatelj,
			String model, String primateljRacun, String brojOdobrenja, String opis) {
		super();
		this.iznos = iznos;
		this.platitelj = platitelj;
		this.primatelj = primatelj;
		this.model = model;
		this.primateljRacun = primateljRacun;
		this.brojOdobrenja = brojOdobrenja;
		this.opis = opis;
	}
	
	public void dodajNaStanja(List<String> listaStanjaId, int korisnikId) {
		System.out.println(listaStanjaId);
		Session session = HibernateUtil.getSession();
		
		session.beginTransaction();
		
		Query<Stanje> query = session.createQuery("from Stanje where stanje_id in (?0) and korisnik_id=?1", Stanje.class);
		query.setParameter(0, listaStanjaId);
		query.setParameter(1, korisnikId);
		
		List<Stanje> s = query.getResultList();
		
		this.setStanja(s);

		session.save(this);
		
		session.getTransaction().commit();
		
		session.close();
	}
}
