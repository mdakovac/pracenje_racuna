package entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.Session;

import util.HibernateUtil;

@Entity
@Table(
	name="korisnik",
	uniqueConstraints = {
            @UniqueConstraint(columnNames = "korisnicko_ime", name="KORISNIK__KORISNICKO_IME_UNIQUE"),
            @UniqueConstraint(columnNames = "email", name="KORISNIK__EMAIL_UNIQUE")
    }
)
public class Korisnik {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="korisnik_id")
	private int id;
	
	@Column(name="korisnicko_ime")
	@Size(min=5, max=20, message="Korisnicko ime mora biti duljine od 5 do 20 znakova.")
	private String korisnickoIme;
	
	@Column(name="lozinka")
	@Size(min=5, max=60, message="Lozinka mora biti minimalno 5 znakova duljine.")
	private String lozinka;
	
	@Column(name="email")
	@Size(min=5, max=100, message="E-mail mora biti minimalno 5 znakova duljine.")
	private String email;
	
	@OneToMany(mappedBy="korisnik", cascade = CascadeType.ALL)
    private List<Stanje> stanja;
	
	public Korisnik() {
		
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String email) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.email = email;
	}
	
	public static Korisnik find(int id) {
		Session session = HibernateUtil.getSession();
		
		Korisnik k = session.get(Korisnik.class, id);
		session.close();
		
		return k;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
		//System.out.println(username.length());
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Korisnik [id=" + id + ", korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", email=" + email
				+ "]";
	}
	
	
}
