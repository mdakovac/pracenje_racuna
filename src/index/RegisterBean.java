package index;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import authentication.BCrypt;
import entities.Korisnik;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "register")
public class RegisterBean {
	private String username = "";
	private String email = "";
	private String password1 = "";
	private String password2 = "";

	public RegisterBean() {
		//System.out.println("RegisterBean instantiated");
	}

	@Override
	public void finalize() {
		//System.out.println("RegisterBean destroyed");
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String validate() {

		// Check for validation violations
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Korisnik u = new Korisnik(this.username, this.password1, this.email);

		Set<ConstraintViolation<Korisnik>> constraintViolations = validator.validate(u);

		if (!constraintViolations.isEmpty()) {
			for (ConstraintViolation<Korisnik> violation : constraintViolations) {
				String message = violation.getMessage();
				Message.Display(message);
			}
			return "index";
		}

		// Check if passwords match
		if (!this.password1.equals(this.password2)) {
			Message.Display("Lozinke se ne podudaraju!");
			return "index";
		}

		String hashedPassword = BCrypt.hashpw(password1, BCrypt.gensalt());
		u.setLozinka(hashedPassword);

		Session session = HibernateUtil.getSession();
		
		try {
			
			session.beginTransaction();
			session.save(u);
			session.getTransaction().commit();
			session.close();
			
		}catch(ConstraintViolationException e) {
			
			String n = e.getConstraintName();
			
			if(n.equals("KORISNIK__KORISNICKO_IME_UNIQUE"))
				Message.Display("Korisnik s ovim korisničkim imenom već postoji.");
			else if(n.equals("KORISNIK__EMAIL_UNIQUE"))
				Message.Display("Korisnik s ovim E-mailom već postoji.");
			return "index";
		}
		
		Message.Display("Korisnik dodan. Sada se možete logirati.");

		return "index";

		/*
		 * CriteriaBuilder cb = session.getCriteriaBuilder(); CriteriaQuery<User> query
		 * = cb.createQuery(User.class); Root<User> employee = (Root<User>)
		 * query.from(User.class);
		 * query.select(employee).where(cb.equal(employee.get("email"), u.getEmail()));
		 * TypedQuery<User> typedQuery = session.createQuery(query);
		 * typedQuery.getResultList().forEach(System.out::println); session.close();
		 * return "index";
		 */
		/*
		 * 
		 * session.beginTransaction(); session.save(u);
		 * session.getTransaction().commit();
		 * 
		 * session.close();
		 * 
		 * System.out.println("Got here. Returning."); return "index";
		 */
	}
}
