package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Integer> vertici;
	private Map<Integer, List<Vicino>> vicini;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<Integer> getYears() {
		return this.dao.getYears();
	}
	
	public String creaGrafo(Integer anno) {
		this.grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vicini = new HashMap<Integer, List<Vicino>>();
		
		//vertici
		this.vertici = this.dao.getDistricts();
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		for(Integer d1 : this.vertici) {
			this.vicini.put(d1, new ArrayList<Vicino>());
			for(Integer d2 : this.vertici) 
				if(!d1.equals(d2)) {
					Double lon1 = this.dao.getLonMedia(anno, d1);
					Double lat1 = this.dao.getLatMedia(anno, d1);
					
					Double lon2 = this.dao.getLonMedia(anno, d2);
					Double lat2 = this.dao.getLatMedia(anno, d2);
					
					Double distanza = LatLngTool.distance(new LatLng(lat1, lon1), new LatLng(lat2, lon2), LengthUnit.KILOMETER);
					
					Vicino v = new Vicino(d2, distanza);
					this.vicini.get(d1).add(v);
					
					if(this.grafo.getEdge(d1, d2) == null)
						Graphs.addEdgeWithVertices(this.grafo, d1, d2, distanza);
				}
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());	
	}
	
	public Map<Integer, List<Vicino>> getVicini() {
		for(Integer v : this.vicini.keySet()) {
			Collections.sort(this.vicini.get(v));
		}
		
		return this.vicini;
	}
	
}
