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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

@Entity
@Table(name = "stanje", uniqueConstraints = { 
	@UniqueConstraint(columnNames = { "korisnik_id", "naziv" }, name="STANJE__KORISNIK_ID_NAZIV_UNIQUE") 
})
public class Stanje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stanje_id")
	private int id;

	@Column(name = "naziv")
	@NotNull
	private String naziv;

	@Column(name = "pocetno_stanje")
	@NotNull
	private float pocetnoStanje = 0;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "korisnik_id")
	private Korisnik korisnik;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "stanje_transakcija", joinColumns = { @JoinColumn(name = "stanje_id") }, inverseJoinColumns = {
			@JoinColumn(name = "transakcija_id") })
	List<Transakcija> transakcije;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public List<Transakcija> getTransakcije() {
		return transakcije;
	}

	public void setTransakcije(List<Transakcija> transakcije) {
		this.transakcije = transakcije;
	}

	public float getPocetnoStanje() {
		return pocetnoStanje;
	}

	public void setPocetnoStanje(float pocetnoStanje) {
		this.pocetnoStanje = pocetnoStanje;
	}

	public Stanje() {

	}

	public Stanje(@NotNull String naziv, @NotNull Korisnik korisnik, @NotNull float pocetnoStanje) {
		super();
		this.naziv = naziv;
		this.pocetnoStanje = pocetnoStanje;
		this.korisnik = korisnik;
	}

	public Stanje(@NotNull String naziv, @NotNull Korisnik korisnik) {
		super();
		this.naziv = naziv;
		this.korisnik = korisnik;
	}

	public static List<Stanje> find(int korisnikId) {
		Session session = HibernateUtil.getSession();

		Query<Stanje> query = session.createQuery("from Stanje where korisnik_id=?1", Stanje.class);
		query.setParameter(1, korisnikId);
		List<Stanje> stanjeList = query.list();

		session.close();

		return stanjeList;
	}

}
