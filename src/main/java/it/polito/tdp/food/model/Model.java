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

		// Crea un grafo nuovo e vuoto: semplice, pesato e non orientato.
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;

		// Aggiungi i vertici che sono i Food che hanno superato il filtraggio precedente dal database
		Graphs.addAllVertices(this.graph, this.cibi) ;
		
		// Aggiungi gli archi
		//l'arco lo aggiungo  se due cibi hanno almeno un ingradiente in comune (guardare dal controller la
		//descrizione).
		for(Food f1: this.cibi) {
			for(Food f2: this.cibi) {
				//controlliamo di non avere il cibo con se stesso e risparmiamo una query
				//controllo anche di non scorrere nuovamente delle coppie ordinate al contrario
				//che sarebbero inutili in quanto l'arco serve non orientato.
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
					Double peso = dao.calorieCongiunte(f1, f2) ;
					//la query ha restituito qualcosa
					if(peso!=null) {
						//aggiungiamo l'arco con il relativo peso
						Graphs.addEdge(this.graph, f1, f2, peso) ;
					}
				}
			}
		}
		//anche se il grafo e' pesato, nella stampa non stampa il peso degli archi
		//System.out.println(this.graph) ;
		
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
		
		//restituiamo tutti i vicini con il mio vertice in parametro
		List<Food> vicini = Graphs.neighborListOf(this.graph, f) ;
		
		for(Food v: vicini) {
			//this.graph.getEdge(f, v) arco da cui devo estrarre il peso
			Double calorie = this.graph.getEdgeWeight(this.graph.getEdge(f, v)) ;
			result.add(new FoodCalories(v, calorie)) ;
		}
		//ordine del Comparator definito nella classe che ci siamo creati apposa
		//FoodCalories
		Collections.sort(result);
		
		return result ;
	}

}
