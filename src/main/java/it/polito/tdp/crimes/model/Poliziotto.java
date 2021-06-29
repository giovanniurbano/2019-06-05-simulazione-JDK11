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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nPol == null) ? 0 : nPol.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poliziotto other = (Poliziotto) obj;
		if (nPol == null) {
			if (other.nPol != null)
				return false;
		} else if (!nPol.equals(other.nPol))
			return false;
		return true;
	}
	
	
	
}
