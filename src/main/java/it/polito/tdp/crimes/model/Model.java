package it.polito.tdp.crimes.model;

import java.util.List;

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
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<Integer> getYears() {
		return this.dao.getYears();
	}
	
	public String creaGrafo(Integer anno) {
		this.grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.vertici = this.dao.getDistricts();
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		for(Integer d1 : this.vertici)
			for(Integer d2 : this.vertici) 
				if(d1 < d2) {
					Double lon1 = this.dao.getLonMedia(anno, d1);
					Double lat1 = this.dao.getLatMedia(anno, d1);
					
					Double lon2 = this.dao.getLonMedia(anno, d2);
					Double lat2 = this.dao.getLatMedia(anno, d2);
					
					Double distanza = LatLngTool.distance(new LatLng(lat1, lon1), new LatLng(lat2, lon2), LengthUnit.KILOMETER);
					
					Graphs.addEdgeWithVertices(this.grafo, d1, d2, distanza);
				}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());	
	}
}
