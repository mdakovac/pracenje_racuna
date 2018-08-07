package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.query.Query;

import additionalTypes.ObradenoStanje;
import util.HibernateUtil;

@Entity
@Table(name = "stanje", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "korisnik_id", "naziv" }, name = "STANJE__KORISNIK_ID_NAZIV_UNIQUE") })
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

	@ManyToMany(mappedBy = "stanja")
	private List<Transakcija> transakcije;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "vrijeme_unosa")
	private Date vrijemeUnosa;

	@PrePersist
	protected void onCreate() {
		vrijemeUnosa = new Date();
	}

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

	public Date getVrijemeUnosa() {
		return vrijemeUnosa;
	}

	public void setVrijemeUnosa(Date vrijemeUnosa) {
		this.vrijemeUnosa = vrijemeUnosa;
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

	public static List<ObradenoStanje> findAll(int korisnikId) {
		Session session = HibernateUtil.getSession();

		Query<Object[]> query = session.createNativeQuery("select t2.stanje_id, t2.naziv, t2.pocetno_stanje, t2.vrijeme_unosa, SUM(t.iznos) " + 
				"from transakcija t " + 
				"INNER JOIN stanje_transakcija t1 on t.transakcija_id = t1.transakcija_id " + 
				"INNER JOIN stanje t2 on t2.stanje_id = t1.stanje_id " + 
				"WHERE korisnik_id=?1 " + 
				"GROUP BY t2.stanje_id, t2.naziv", Object[].class);
		query.setParameter(1, korisnikId);
		
		List<Object[]> l = query.list();
		session.close();
		
		
		List<ObradenoStanje> listaStanja = new ArrayList<ObradenoStanje>();

		for (Object[] element : l) {
			ObradenoStanje os = new ObradenoStanje();
			os.setId((int)element[0]);
			os.setNaziv((String)element[1]);
			os.setPocetnoStanje((float)element[2]);
			os.setTrenutnoStanje((double)element[4]);
		    os.setVrijemeUnosa((Date)element[3]);
		    
		    listaStanja.add(os);
		}

		return listaStanja;
	}
	
	public static void drop(int stanjeId) {
		Session session = HibernateUtil.getSession();
		
		// pronadi sve transakcije koje su samo na danom stanju
		// ako je transakcija na nekom stanju osim danog, ovaj query ju ne vraća
		Query<Integer> q = session.createNativeQuery("SELECT" + 
				"    st.transakcija_id " + 
				"FROM" + 
				"    stanje_transakcija st " + 
				"JOIN transakcija t ON" + 
				"    st.transakcija_id = t.transakcija_id " + 
				"JOIN stanje_transakcija st2 ON" + 
				"    t.transakcija_id = st2.transakcija_id " + 
				"WHERE" + 
				"    st2.stanje_id = ?1 " + 
				"GROUP BY" + 
				"    st.transakcija_id " + 
				"HAVING" + 
				"    COUNT(st.stanje_id) = 1", Integer.class);
		q.setParameter(1, stanjeId);
		
		List<Integer> transakcijeId = q.getResultList();
		
		session.beginTransaction();
		
		// obrisi dano stanje iz stanje_transakcija
		Query<?> qu = session.createNativeQuery("DELETE FROM stanje_transakcija WHERE stanje_id = ?");
		qu.setParameter(1, stanjeId);
		qu.executeUpdate();
		
		// obrisi transakcije koje su samo na danom stanju
		if(!transakcijeId.isEmpty()) {
			qu = session.createQuery("delete from Transakcija where transakcija_id in (?1)");
			qu.setParameter(1, transakcijeId);
			qu.executeUpdate();
		}
		
		// obrisi dano stanje
		session.delete(session.load(Stanje.class, stanjeId));
		
		session.getTransaction().commit();
		
		session.close();
	}
	

}
