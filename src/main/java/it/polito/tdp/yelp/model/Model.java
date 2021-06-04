package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao;
	Graph<Business, DefaultWeightedEdge> grafo;
	Map<String, Business> bMap;
	
	public Model(){
		dao = new YelpDao();
		bMap = new HashMap<>();
		dao.getAllBusiness(bMap);
	}
	
	public List<String> getCitta(){
		return dao.getCitta();
	}
	
	public List<String> getLocali(){
		
		List<String> locali = new ArrayList<>();
		for(String s: bMap.keySet()) {
			locali.add(bMap.get(s).getBusinessName());
		}
		return locali;
	}
	
	public void creaGrafo(String citta, int anno) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(citta, anno, bMap));
		
		for(Adiacenza a: dao.getArchi(citta, anno)) {
			if(grafo.getEdge(bMap.get(a.getB1()), bMap.get(a.getB2())) == null) {
				if(a.getPeso() < 0) 
					Graphs.addEdge(grafo, bMap.get(a.getB1()), bMap.get(a.getB2()), a.getPeso()*-1);
				if(a.getPeso() > 0)
					Graphs.addEdge(grafo, bMap.get(a.getB2()), bMap.get(a.getB1()), a.getPeso());
			}
		}
	}
	
	public int vertexNumber() {
		return grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return grafo.edgeSet().size();
	}
	
	public Business localeMigliore() {
		
		Business localeM = null;
		double media = 0.0;
		for(Business b: grafo.vertexSet()) {
			double bTot = 0.0;
			for(DefaultWeightedEdge e: grafo.incomingEdgesOf(b)) {
				bTot += grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(b)) {
				bTot -= grafo.getEdgeWeight(e);
			}
			if(bTot > media) {
				media = bTot;
				localeM = b;
			}
		}
		return localeM;
	}
}
