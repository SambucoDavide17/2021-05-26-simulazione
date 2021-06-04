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
		
		
	}
}
