/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Simulator;
import it.polito.tdp.crimes.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxMese.getItems().clear();
    	this.boxGiorno.getItems().clear();
    	this.txtN.clear();
    	
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		this.txtResult.appendText("Scegliere un anno!");
    		return;
    	}
    	String msg = this.model.creaGrafo(anno);
    	this.txtResult.appendText(msg);
    	
    	Map<Integer, List<Vicino>> vicini = this.model.getVicini();
    	for(Integer d : vicini.keySet()) {
    		this.txtResult.appendText("\nDistretto " + d + ":\n");
    		for(Vicino v : vicini.get(d)) {
    			this.txtResult.appendText(v + "\n");
    		}
    	}
    	
    	this.boxMese.getItems().addAll(this.model.getMonths(anno));
    	this.boxGiorno.getItems().addAll(this.model.getDays(anno));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		this.txtResult.appendText("Scegliere un anno!");
    		return;
    	}
    	Integer mese = this.boxMese.getValue();
    	if(mese == null) {
    		this.txtResult.appendText("Scegliere un mese!");
    		return;
    	}
    	Integer giorno = this.boxGiorno.getValue();
    	if(giorno == null) {
    		this.txtResult.appendText("Scegliere un giorno!");
    		return;
    	}
    	String nS = this.txtN.getText();
    	try {
    		int n = Integer.parseInt(nS);
    		if(n < 1 || n > 10) {
    			this.txtResult.appendText("Inserire un N compreso tra 1 e 10!");
        		return;
    		}
    		
    		Simulator sim = new Simulator(this.model, anno, mese, giorno, n);
    		sim.run();
    		this.txtResult.appendText("Crimini mal gestiti: " + sim.getnEvMalGestiti());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire un N intero!");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxAnno.getItems().addAll(this.model.getYears());
    }
}
