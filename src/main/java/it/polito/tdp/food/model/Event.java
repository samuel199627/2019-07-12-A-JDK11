package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INIZIO_PREPARAZIONE, // evento in cui viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE, // evento in cui la stazione ha completato la preparazione di un cibo
	}

	private Double time ; // tempo in minuti
	private EventType type ;
	private Stazione stazione ;
	private Food food ;
	
	public Double getTime() {
		return time;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public Food getFood() {
		return food;
	}
	/**
	 * @param time
	 * @param stazione
	 * @param food
	 */
	public Event(Double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.type = type;
		this.stazione = stazione;
		this.food = food;
	}
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}
	public EventType getType() {
		return type;
	}
	
	
}


/*
0[ F1 ] 1[ F2 ] 2[ F3 ]
		
		FINE_PREPARAZIONE( F2, stazione 1 )   T
		INIZIO_PREPARAZIONE(F2 cibo concluso, stazione 1)  T
			scelgo il prox cibo
			calcolo la durata
			schedulo evento FINE (cibo nuovo appena scelto)
		FINE_PREPARAZIONE(F4, stazione 1)  T+DELTA  DELTA(peso di F2--F4)
			rischedule evento di INIZIO allo stesso istante, ricordando il cibo terminato
*/