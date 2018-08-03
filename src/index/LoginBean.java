package index;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.query.Query;

import authentication.BCrypt;
import entities.Korisnik;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "login")
public class LoginBean {

	private String username;
	private String password;

	public LoginBean() {
		// System.out.println("LoginBean instantiated");
	}

	@Override
	public void finalize() {
		// System.out.println("LoginBean destroyed");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();

		Session session = HibernateUtil.getSession();

		session.beginTransaction();
		Query<Korisnik> query = session.createQuery("from Korisnik where korisnicko_ime=?0", Korisnik.class);
		query.setParameter(0, this.username);

		List<Korisnik> u = query.getResultList();

		if (u.size() == 0) {
			Message.Display("Netocni podaci.");
			return "index";
		}

		if (BCrypt.checkpw(this.password.trim(), u.get(0).getLozinka().trim())) {
			context.getExternalContext().getSessionMap().put("id", u.get(0).getId());
			return "transakcija?faces-redirect=true";
		} else {
			Message.Display("Netocni podaci");
		}
		return "index";
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		
		return "index?faces-redirect=true";
	}
}
