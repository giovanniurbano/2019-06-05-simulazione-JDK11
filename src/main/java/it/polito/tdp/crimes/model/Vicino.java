package it.polito.tdp.crimes.model;

public class Vicino implements Comparable<Vicino>{
	private Integer district_id;
	private Double distanza;
	
	public Vicino(Integer district_id, Double distanza) {
		super();
		this.district_id = district_id;
		this.distanza = distanza;
	}
	public Integer getDistrict_id() {
		return district_id;
	}
	public void setDistrict_id(Integer district_id) {
		this.district_id = district_id;
	}
	public Double getDistanza() {
		return distanza;
	}
	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(Vicino o) {
		return this.distanza.compareTo(o.distanza);
	}
	@Override
	public String toString() {
		return this.district_id + " - " + this.distanza + " km";
	}
	
}
