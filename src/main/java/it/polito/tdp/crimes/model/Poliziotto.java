package it.polito.tdp.crimes.model;

public class Poliziotto {
	private Integer nPol;
	private Integer distretto;
	private boolean libero;
	public Poliziotto(Integer nPol, Integer distretto, boolean libero) {
		super();
		this.nPol = nPol;
		this.distretto = distretto;
		this.libero = libero;
	}
	public Integer getnPol() {
		return nPol;
	}
	public void setnPol(Integer nPol) {
		this.nPol = nPol;
	}
	public Integer getDistretto() {
		return distretto;
	}
	public void setDistretto(Integer distretto) {
		this.distretto = distretto;
	}
	public boolean isLibero() {
		return libero;
	}
	public void setLibero(boolean libero) {
		this.libero = libero;
	}
	
	
	
}
