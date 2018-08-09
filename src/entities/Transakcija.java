package entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	
	@ManyToMany
	@JoinTable(	name = "stanje_transakcija", 
				joinColumns = { @JoinColumn(name = "transakcija_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "stanje_id") })
	private List<Stanje> stanja;
		
	@Column(name="iznos")
	@NotNull
	private BigDecimal iznos;
	
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
	
	@Column(name = "vrijeme_unosa")
	@Temporal(TemporalType.TIMESTAMP)
	private Date vrijemeUnosa;
	
	@PrePersist
	protected void onCreate() {
		vrijemeUnosa = new Date();
	}
	
	public Transakcija() {
		
	}

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

	public Date getVrijemeUnosa() {
		return vrijemeUnosa;
	}

	public void setVrijemeUnosa(Date vrijemeUnosa) {
		this.vrijemeUnosa = vrijemeUnosa;
	}

	public Transakcija(@NotNull BigDecimal iznos, String platitelj, String primatelj,
			String model, String primateljRacun, String brojOdobrenja, String opis, List<Stanje> stanja) {
		super();
		this.iznos = iznos;
		this.platitelj = platitelj;
		this.primatelj = primatelj;
		this.model = model;
		this.primateljRacun = primateljRacun;
		this.brojOdobrenja = brojOdobrenja;
		this.opis = opis;
		this.stanja = stanja;
	}
	
	public static void save(List<String> odabranaStanja, int korisnikId, BigDecimal iznos, String platitelj, String primatelj, String model, String primateljRacun, String brojOdobrenja, String opis) {
		Session session = HibernateUtil.getSession();
		
		session.beginTransaction();
		
		Query<Stanje> query = session.createQuery("from Stanje where stanje_id in (?0) and korisnik_id=?1", Stanje.class);
		query.setParameter(0, odabranaStanja);
		query.setParameter(1, korisnikId);
		
		List<Stanje> s = query.getResultList();
		
		Transakcija t = new Transakcija(iznos, platitelj, primatelj, model, primateljRacun, brojOdobrenja, opis, s);

		session.save(t);
		
		session.getTransaction().commit();
		
		session.close();
	}
	
	public static void drop(int transakcijaId) {
		Session session = HibernateUtil.getSession();
		
		session.beginTransaction();
		
		// obrisi dano stanje iz stanje_transakcija
		Query<?> qu = session.createNativeQuery("DELETE FROM stanje_transakcija WHERE transakcija_id = ?1");
		qu.setParameter(1, transakcijaId);
		qu.executeUpdate();

		// obrisi dano stanje
		session.delete(session.load(Transakcija.class, transakcijaId));

		session.getTransaction().commit();

		session.close();
	}
}
