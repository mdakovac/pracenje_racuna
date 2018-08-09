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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import util.HibernateUtil;
import util.Message;

@Entity
@Table(name = "stanje", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "korisnik_id", "naziv" }, name = "STANJE__KORISNIK_ID_NAZIV_UNIQUE") })
public class Stanje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stanje_id")
	protected int id;

	@Column(name = "naziv")
	@NotNull
	protected String naziv;

	@Column(name = "pocetno_stanje")
	@NotNull
	protected BigDecimal pocetnoStanje;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "korisnik_id")
	protected Korisnik korisnik;

	@ManyToMany(mappedBy = "stanja")
	protected List<Transakcija> transakcije;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "vrijeme_unosa")
	protected Date vrijemeUnosa;

	@PrePersist
	protected void onCreate() {
		vrijemeUnosa = new Date();
	}
	
	public Stanje() {

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
	public BigDecimal getPocetnoStanje() {
		return pocetnoStanje;
	}
	public void setPocetnoStanje(BigDecimal pocetnoStanje) {
		this.pocetnoStanje = pocetnoStanje;
	}
	public Date getVrijemeUnosa() {
		return vrijemeUnosa;
	}
	public void setVrijemeUnosa(Date vrijemeUnosa) {
		this.vrijemeUnosa = vrijemeUnosa;
	}
	

	public Stanje(@NotNull String naziv, @NotNull Korisnik korisnik, @NotNull BigDecimal pocetnoStanje) {
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
	
	public static Stanje get(int stanjeId) {
		Session session = HibernateUtil.getSession();
		Stanje s = session.get(Stanje.class, stanjeId);
		session.close();
		return s;
	}
	
	

	

	public static void drop(int stanjeId) {
		Session session = HibernateUtil.getSession();

		// pronadi sve transakcije koje su samo na danom stanju
		// ako je transakcija na nekom stanju osim danog, ovaj query ju ne vraća
		Query<Integer> q = session.createNativeQuery(
				"SELECT" + "    st.transakcija_id " + "FROM" + "    stanje_transakcija st " + "JOIN transakcija t ON"
						+ "    st.transakcija_id = t.transakcija_id " + "JOIN stanje_transakcija st2 ON"
						+ "    t.transakcija_id = st2.transakcija_id " + "WHERE" + "    st2.stanje_id = ?1 "
						+ "GROUP BY" + "    st.transakcija_id " + "HAVING" + "    COUNT(st.stanje_id) = 1",
				Integer.class);
		q.setParameter(1, stanjeId);

		List<Integer> transakcijeId = q.getResultList();

		session.beginTransaction();

		// obrisi dano stanje iz stanje_transakcija
		Query<?> qu = session.createNativeQuery("DELETE FROM stanje_transakcija WHERE stanje_id = ?");
		qu.setParameter(1, stanjeId);
		qu.executeUpdate();

		// obrisi transakcije koje su samo na danom stanju
		if (!transakcijeId.isEmpty()) {
			qu = session.createQuery("delete from Transakcija where transakcija_id in (?1)");
			qu.setParameter(1, transakcijeId);
			qu.executeUpdate();
		}

		// obrisi dano stanje
		session.delete(session.load(Stanje.class, stanjeId));

		session.getTransaction().commit();

		session.close();
	}

	public static int save(String naziv, BigDecimal pocetnoStanje, int korisnikId) {
		Session session = HibernateUtil.getSession();

		Korisnik k = session.load(Korisnik.class, korisnikId);
		Stanje s = new Stanje(naziv, k, pocetnoStanje);

		try {
			session.beginTransaction();

			session.save(s);

			Query<?> q = session.createNativeQuery("INSERT INTO stanje_transakcija (stanje_id, transakcija_id) VALUES (?1, 1)");
			q.setParameter(1, s.getId());
			q.executeUpdate();

			session.getTransaction().commit();

		} catch (ConstraintViolationException e) {

			String n = e.getConstraintName();
			session.close();

			if (n.equals("STANJE__KORISNIK_ID_NAZIV_UNIQUE")) {
				Message.Display("Stanje s ovim imenom već postoji.");
				return -1;
			}
			return -2;

		}
		session.close();
		return 0;
	}
}
