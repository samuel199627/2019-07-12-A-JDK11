package it.polito.tdp.food.model;

public class FoodCalories implements Comparable<FoodCalories> {
	
	private Food food ;
	//mettiamo la classe primaria per poter avere il compareTo naturale dei double
	private Double calories ;
	/**
	 * @param food
	 * @param calories
	 */
	public FoodCalories(Food food, Double calories) {
		super();
		this.food = food;
		this.calories = calories;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public Double getCalories() {
		return calories;
	}
	public void setCalories(Double calories) {
		this.calories = calories;
	}
	@Override
	public int compareTo(FoodCalories other) {
		// ordina in verso DECRESCENTE rispetto all'ordinamento naturale dei numeri
		//con dei double qui bisogna fare attenzione a non fare la differenza perche' se in
		//modulo viene piu' piccola di 1, dato che il metodo restituisce un intero e' un 
		//problema siccome viene tutto troncato.
		return -(this.calories.compareTo(other.calories));
		
	}
	
	

}
