package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Evento.TipoEvento;

public class Simulator {
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	//modello del mondo
	private Map<Poliziotto, Event> intervento; //<poliziotto, crimine su cui deve intervenire>
	
	//parametri di input
	private Model model;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	private int N;
	private int vel = 60; //km/h
	private double probAOC = 0.5;
	
	//valori di output
	private int nEvMalGestiti;
	
	public Simulator(Model model, Integer anno, Integer mese, Integer giorno, int N) {
		this.model = model;
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		this.N = N;
		
		Integer distrettoIniziale = this.model.getDistrettoMenoCrimini(anno);
		this.intervento = new HashMap<Poliziotto, Event>();
		for(int i=0; i<N; i++)
			this.intervento.put(new Poliziotto(i, distrettoIniziale, true), null);
		
		this.nEvMalGestiti = 0;
		
		this.queue = new PriorityQueue<Evento>();
		List<Event> criminiDelGiorno = this.model.listAllEventsByDate(anno, mese, giorno);
		for(Event crimine : criminiDelGiorno) {
			LocalTime t = LocalTime.of(crimine.getReported_date().getHour(), crimine.getReported_date().getMinute());
			Evento e = new Evento(t, TipoEvento.CRIMINE_COMMESSO, crimine, null);
			this.queue.add(e);
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			this.processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		switch(e.getTipo()) {
		case CRIMINE_COMMESSO: 
			Poliziotto pm = null;
			for(Poliziotto p : this.intervento.keySet()) {
				if(p.isLibero()) {
					pm = p;
					break;
				}
			}
			DefaultWeightedEdge arco = this.model.getGrafo().getEdge(e.getCrimine().getDistrict_id(), pm.getDistretto());
			Double distanza = this.model.getGrafo().getEdgeWeight(arco);
			TemporalAmount tempoPerArrivare = Duration.ofHours((long) (distanza/this.vel));
			LocalTime a = e.getT().plus(tempoPerArrivare);
			
			this.queue.add(new Evento(a, TipoEvento.INTERVENTO, e.getCrimine(), pm));
			pm.setLibero(false);
			
			TemporalAmount tempoIntervento = null;
			if(e.getCrimine().getOffense_category_id().equals("all-other-crimes")) {
				double p = Math.random();
				if(p < this.probAOC)
					tempoIntervento = Duration.ofHours(1);
				else
					tempoIntervento = Duration.ofHours(2);
			}
			else
				tempoIntervento = Duration.ofHours(2);
			
			this.queue.add(new Evento(a.plus(tempoIntervento), TipoEvento.POL_LIBERO, null, pm));
			break;
		case INTERVENTO:
			break;
		case POL_LIBERO:
			e.getPoliziotto().setLibero(true);
			break;
		case MAL_GESTITO:
			this.nEvMalGestiti++;
			break;
		}
	}
	
	
}
