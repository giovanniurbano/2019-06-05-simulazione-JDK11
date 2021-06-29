package it.polito.tdp.crimes.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento>{
	public enum TipoEvento {
		CRIMINE_COMMESSO,
		INTERVENTO,
		POL_LIBERO,
		MAL_GESTITO
	}
	
	private LocalTime t;
	private TipoEvento tipo;
	private Event crimine;
	private Poliziotto pol;
	
	public Evento(LocalTime t, TipoEvento tipo, Event crimine, Poliziotto p) {
		this.t = t;
		this.tipo = tipo;
		this.crimine = crimine;
		this.pol = p;
	}

	public LocalTime getT() {
		return t;
	}

	public void setT(LocalTime t) {
		this.t = t;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public void setTipo(TipoEvento tipo) {
		this.tipo = tipo;
	}

	public Event getCrimine() {
		return crimine;
	}

	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}

	public Poliziotto getPoliziotto() {
		return pol;
	}

	public void setPoliziotto(Poliziotto poliziotto) {
		this.pol = poliziotto;
	}

	@Override
	public String toString() {
		return "Evento [t=" + t + ", tipo=" + tipo + ", crimine=" + crimine + ", poliziotto=" + pol + "]";
	}

	@Override
	public int compareTo(Evento o) {
		return this.t.compareTo(o.t);
	}
}
