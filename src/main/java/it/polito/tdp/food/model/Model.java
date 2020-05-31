package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi ;
	private Graph<Food, DefaultWeightedEdge> graph ; 
	
	public Model() {
	}
	
	//restituisce i cibi per il primo punto dell'esame che restituisce i cibi che si servono
	//nel numero di porzioni che sono passate come parametro
	public List<Food> getFoods(int portions) {
		//l'avrei creata nel costruttore, ma non cambia molto
		FoodDao dao = new FoodDao() ;
		this.cibi = dao.getFoodsByPortions(portions) ;

		// Crea un grafo nuovo e vuoto
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;

		// Aggiungi i vertici
		Graphs.addAllVertices(this.graph, this.cibi) ;
		
		// Aggiungi gli archi
		for(Food f1: this.cibi) {
			for(Food f2: this.cibi) {
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
					Double peso = dao.calorieCongiunte(f1, f2) ;
					if(peso!=null) {
						Graphs.addEdge(this.graph, f1, f2, peso) ;
					}
				}
			}
		}
		System.out.println(this.graph) ;
		
		return this.cibi ;
	}
	
	public String simula(Food cibo, int K) {
		Simulator sim = new Simulator(this.graph, this) ;
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", 
				sim.getCibiPreparati(), sim.getTempoPreparazione());
		return messaggio ;
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		
		List<FoodCalories> result = new ArrayList<>() ;
		
		List<Food> vicini = Graphs.neighborListOf(this.graph, f) ;
		
		for(Food v: vicini) {
			Double calorie = this.graph.getEdgeWeight(this.graph.getEdge(f, v)) ;
			result.add(new FoodCalories(v, calorie)) ;
		}
		
		Collections.sort(result);
		
		return result ;
	}

}
