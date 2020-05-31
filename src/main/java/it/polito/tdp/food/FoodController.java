/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalories;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//Importato

/*
 	
 	Lavoriamo con un dataset relativo al cibo. Rappresenta un modo per rappresentare le calorie
 	nei cibi. Ciascun cibo puo' essere servito in porzioni differenti (anche solo una).
 	
 	Abbiamo una tabella che rappresenta tutti i condimenti che vanno a costruire tutti i tipi di cibo
 	e quindi una tabella che 
 	
 	Il primo passo devo inserire un numero che rappresenta un numero di porzioni e devo restituire i 
 	cibi che si possono presentare esattamente in quel numero di variante di porzioni. Metto in 
 	relazione la tabella food con quella portion e per ogni cibo conto in quante porzioni viene servito.
 	Il numero di cibi restituiti e' ristretto con 6 o 7 porzioni e quindi il grafo creato sara' abbastanza
 	piccolo quanto ci serve per lavorare in tranquillita'.
 	
 	Creo un grafo con i cibi che superano il primo filtraggio e creo un arco tra cibi se hanno almeno
 	un ingradiente in comune. Il grafo deve essere semplice, pesato e non orientato.
 	
 	
 	
 */

public class FoodController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtPorzioni"
	private TextField txtPorzioni; // Value injected by FXMLLoader

	@FXML // fx:id="txtK"
	private TextField txtK; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Quello che legge le porzioni e che crea il grafo

	@FXML // fx:id="btnCalorie"
	private Button btnCalorie; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="boxFood"
	private ComboBox<Food> boxFood; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
//    	txtResult.appendText("Creazione grafo...");
		String porzioniStr = txtPorzioni.getText();
		//cerchiamo di intercettare eventuali errori nell'inserimento di un numero
		try {
			int portions = Integer.parseInt(porzioniStr);
			List<Food> cibi = model.getFoods(portions);
			//pulisco da eventuali run precedenti il menu a tendina
			boxFood.getItems().clear();
			//popolo il menu a tendina
			boxFood.getItems().addAll(cibi) ;
			
		} catch (NumberFormatException ex) {
			txtResult.appendText("ERRORE: Devi inserire un numero\n");
			return;
		}
	}

	@FXML
	void doCalorie(ActionEvent event) {
		txtResult.clear();
		
		Food f = boxFood.getValue() ;
		
		if(f==null) {
			txtResult.appendText("ERRORE: devi selezionare un cibo\n");
			return ;
		}
		
		List<FoodCalories> lista = model.elencoCibiConnessi(f);
		
		for(int i=0; i<5 && i<lista.size(); i++) {
			txtResult.appendText(String.format("%s %f\n",
					lista.get(i).getFood().getDisplay_name(),
					lista.get(i).getCalories()));
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		
		Food f = boxFood.getValue() ;
		
		if(f==null) {
			txtResult.appendText("ERRORE: devi selezionare un cibo\n");
			return ;
		}
		
		int K ;
		try {
			K = Integer.parseInt(txtK.getText()) ;
		} catch(NumberFormatException ex) {
			txtResult.appendText("ERRORE: K deve essere un numero\n");
			return;
		}

		String messaggio = model.simula(f, K);
		txtResult.appendText(messaggio);
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
		assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
