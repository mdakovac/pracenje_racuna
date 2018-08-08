package additionalTypes;

import java.math.BigDecimal;
import java.util.Date;

public class ObradenoStanje {
	int id;
	String naziv;
	BigDecimal pocetnoStanje;
	BigDecimal trenutnoStanje;
	Date vrijemeUnosa;
	
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
	public BigDecimal getPocetnoStanje() {
		return pocetnoStanje;
	}
	public void setPocetnoStanje(BigDecimal pocetnoStanje) {
		this.pocetnoStanje = pocetnoStanje;
	}
	public BigDecimal getTrenutnoStanje() {
		return trenutnoStanje;
	}
	public void setTrenutnoStanje(BigDecimal trenutnoStanje) {
		this.trenutnoStanje = trenutnoStanje;
	}
	public Date getVrijemeUnosa() {
		return vrijemeUnosa;
	}
	public void setVrijemeUnosa(Date vrijemeUnosa) {
		this.vrijemeUnosa = vrijemeUnosa;
	}
}
