package additionalTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import entities.Stanje;
import util.HibernateUtil;

public class ObradenoStanje extends Stanje{
	BigDecimal trenutnoStanje;
	
	public BigDecimal getTrenutnoStanje() {
		return trenutnoStanje;
	}
	
	public void setTrenutnoStanje(BigDecimal trenutnoStanje) {
		this.trenutnoStanje = trenutnoStanje;	
	}
	
	public static List<ObradenoStanje> findAll(int korisnikId) {
		Session session = HibernateUtil.getSession();

		Query<Object[]> query = session
				.createNativeQuery("select t2.stanje_id, t2.naziv, t2.pocetno_stanje, t2.vrijeme_unosa, SUM(t.iznos) "
						+ "from transakcija t "
						+ "INNER JOIN stanje_transakcija t1 on t.transakcija_id = t1.transakcija_id "
						+ "INNER JOIN stanje t2 on t2.stanje_id = t1.stanje_id " + "WHERE korisnik_id=?1 "
						+ "GROUP BY t2.stanje_id, t2.naziv", Object[].class);
		query.setParameter(1, korisnikId);

		List<Object[]> l = query.list();
		session.close();

		List<ObradenoStanje> listaStanja = new ArrayList<ObradenoStanje>();

		for (Object[] element : l) {
			ObradenoStanje os = new ObradenoStanje();
			os.setId((int) element[0]);
			os.setNaziv((String) element[1]);
			os.setPocetnoStanje(new BigDecimal(element[2].toString()));
			os.setTrenutnoStanje(new BigDecimal(element[4].toString()));
			os.setVrijemeUnosa((Date) element[3]);

			listaStanja.add(os);
		}

		return listaStanja;
	}
	
	public void loadTransakcije() {
		Session session = HibernateUtil.getSession();
		System.out.println(session);
		Stanje s = session.get(Stanje.class, this.getId());
		System.out.println(s);
		System.out.println(s.getTransakcije());
		this.setTransakcije(s.getTransakcije());
		session.close();
	}
}
