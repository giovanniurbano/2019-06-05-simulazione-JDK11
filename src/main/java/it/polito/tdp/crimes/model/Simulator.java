package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
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
	private int vel = 60; //km/h
	private double probAOC = 0.5;
	
	//valori di output
	private int nEvMalGestiti;
	
	public Simulator(Model model, Integer anno, Integer mese, Integer giorno, int N) {
		this.model = model;
		
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
			System.out.println(e);
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
			if(pm != null) {
				Double distanza = null;
				if(e.getCrimine().getDistrict_id() != pm.getDistretto()) {
					DefaultWeightedEdge arco = this.model.getGrafo().getEdge(e.getCrimine().getDistrict_id(), pm.getDistretto());
					distanza = this.model.getGrafo().getEdgeWeight(arco);
				}
				else
					distanza = 0.0;
				LocalTime a = e.getT().plusSeconds((long) ((distanza*1000)/(this.vel/3.6)));
				
				if(a.isAfter(e.getT().plusMinutes(15))) {
					this.queue.add(new Evento(a, TipoEvento.MAL_GESTITO, e.getCrimine(), pm));
				}
				
				this.queue.add(new Evento(a, TipoEvento.INTERVENTO, e.getCrimine(), pm));
				pm.setDistretto(e.getCrimine().getDistrict_id());
				pm.setLibero(false);
				this.intervento.replace(pm, e.getCrimine());
			}
			else {
				this.queue.add(new Evento(e.getT(), TipoEvento.MAL_GESTITO, e.getCrimine(), pm));
			}
			break;
			
		case INTERVENTO:
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
			
			this.queue.add(new Evento(e.getT().plus(tempoIntervento), TipoEvento.POL_LIBERO, null, e.getPoliziotto()));
			break;
			
		case POL_LIBERO:
			e.getPoliziotto().setLibero(true);
			break;
			
		case MAL_GESTITO:
			this.nEvMalGestiti++;
			if(e.getPoliziotto() != null)
				this.queue.add(new Evento(e.getT().plusMinutes(1), TipoEvento.POL_LIBERO, null, e.getPoliziotto()));
			break;
		}
	}

	public int getnEvMalGestiti() {
		return nEvMalGestiti;
	}
	
}
