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
 	Per l'arco devo prendere due cibi  diversi e verificare che abbiano almeno un ingradiente in comune
 	e questo lo faccio prendendo la tabella food condiment in join con se stessa. Il peso dell'arco 
 	e' il valore medio del campo calorie degli ingredienti in comune tra i due cibi e quindi mettiamo
 	ancora in join la tabella degli ingredienti per estrarre le calorie e facciamo la media con AVG 
 	direttamente dal database dato che non ci sono altri ragionamenti dietro. Raggruppo per la coppia di
 	cibi e cosi' mi crea il gruppo sulla coppia di cibi con in ogni riga la coppia e il singolo ingradiente
 	in comune di cui estraggo le calorie di cui calcolo direttamente la media.
 	
 	Scelgo un cibo dalla tendina popolata prima, scelgo i 5 vertici vicini che hanno le calorie (medie) 
 	massime, cioe' quelle relative al peso dell'arco in pratica.
 	Ci creiamo un metodo che restituisce i cibi connessi al cibo scelto ordinate per calorie massime. Ci
 	creiamo una classe stupida che contiene il Food e le calorie per fare il compareTo in maniera rapida.
 	I grafi vengono parecchio isolati con 5,6 o 7 porzioni quindi bisogna fare un po' di prove per estrarre
 	la lista dei vicini.
 	
 	
 	Dobbiamo gestire una simulazione ad eventi in simulazione per preparare dei cibi sul grafo di prima
 	simulando una cucina. Le calorie congiunte equivalgono a dei minuti.
 	In sostanza partiamo dal cibo selezionato al punto precedenza sempre dal menu a tendina e da questo
 	andiamo in tutti i vicini ad esso e l'arco che congiunge questo cibo ai congiunti (che rappresentano le
 	calorie medie che sono condivise) rappresentano i minuti che ci vogliono per preparare il cibo in cui
 	vogliamo arrivare con l'arco. Predisponiamo un numero 'k' di postazioni di lavoro in cucina per la 
 	preparazione di un determinato cibo che al massimo vogliamo occupare. Quando uno dei cibi con cui siamo
 	partiti in preparazione termina di essere preparato, la stazione di cucina si libera e ci muoviamo in 
 	uno dei cibi adiacenti (mi sembra quello con le calorie massime) a patto che non sia gia' stato 
 	preparato. La simulazione termina quando tutte le stazioni di preparazione non hanno piu' degli adiacenti
 	tra cui andare a cercare e il tempo totale di preparazione in cucina riguarda dall'inizio alla fine della
 	preparazione in quanto molte preparazioni avvengono in parallelo e quindi il tempo totale non e' la somma 
 	dei singoli tempi di preparazione dei cibi.
 	
 	Per prima cosa nel modello dobbiamo creare il simulatore e l'evento.
 	
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
		//estraggo i 5 cibi piu' calorici in media vicini a quello selezionato
		txtResult.clear();
		
		Food f = boxFood.getValue() ;
		
		if(f==null) {
			txtResult.appendText("ERRORE: devi selezionare un cibo\n");
			return ;
		}
		
		List<FoodCalories> lista = model.elencoCibiConnessi(f);
		//stampo solo i primi 5 (o meno se ho meno vicini)
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
