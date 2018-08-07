package additionalTypes;

import java.util.Date;

public class ObradenoStanje {
	int id;
	String naziv;
	float pocetnoStanje;
	double trenutnoStanje;
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
	public float getPocetnoStanje() {
		return pocetnoStanje;
	}
	public void setPocetnoStanje(float pocetnoStanje) {
		this.pocetnoStanje = pocetnoStanje;
	}
	public double getTrenutnoStanje() {
		return trenutnoStanje;
	}
	public void setTrenutnoStanje(double trenutnoStanje) {
		this.trenutnoStanje = trenutnoStanje;
	}
	public Date getVrijemeUnosa() {
		return vrijemeUnosa;
	}
	public void setVrijemeUnosa(Date vrijemeUnosa) {
		this.vrijemeUnosa = vrijemeUnosa;
	}
}
