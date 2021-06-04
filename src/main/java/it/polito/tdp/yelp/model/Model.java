package it.polito.tdp.yelp.model;

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
	
	public void creaGrafo(String citta, int anno) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(citta, anno, bMap));
		
		for(Adiacenza a: dao.getArchi(citta, anno, bMap)) {
			if(grafo.getEdge(a.getB1(), a.getB2()) == null) {
				if(a.getPeso() < 0) 
					Graphs.addEdge(grafo, a.getB1(), a.getB2(), a.getPeso()*-1);
				if(a.getPeso() > 0)
					Graphs.addEdge(grafo, a.getB2(), a.getB1(), a.getPeso());
			}
		}
	}
	
	public int vertexNumber() {
		return grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return grafo.edgeSet().size();
	}
}
