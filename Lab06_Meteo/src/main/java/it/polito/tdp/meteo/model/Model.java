package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO dao;
	List<Citta> best =  new ArrayList<Citta>();
	List<Citta> citta = new ArrayList<Citta>();

	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		dao = new MeteoDAO();
		citta = dao.tuttelecitta();
	}

	// of course you can change the String output with what you think works best
	public Map<Citta,Double> getUmiditaMedia(int mese) {
		List<Double> umiditacittamese = new ArrayList<Double>();
		Map<Citta,Double> umiditamedia = new HashMap<Citta,Double>();
		List<Citta> citta = dao.tuttelecitta();
		for(Citta c : citta) {
			 umiditamedia.put(c, dao.umiditamedia(c, mese));
			
		}
		
		return umiditamedia;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		 this.best = null;
		List<Citta> parziale = new ArrayList<Citta>();
		
		for(Citta c : citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale, 0);
		
		return best;
	}
	
	
	private void cerca(List<Citta> parziale, int i) {
		//caso terminale
		if(i == NUMERO_GIORNI_TOTALI) {
			if(best == null || costototale(best)>costototale(parziale)) {
				best = new ArrayList<>(parziale);
			}
			return ;
		}
		
		//caso normale
		for(Citta citta : citta) {
			if(cittaValida(parziale,citta)) {
				parziale.add(citta);
				cerca(parziale,i+1);
				//backtracking
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	
	

	private boolean cittaValida(List<Citta> parziale, Citta citta) {
		
		int conta =0;
		
		for(Citta precedente : parziale) {
			if(precedente.equals(citta)) conta++;
		}
		
		if(conta>=NUMERO_GIORNI_CITTA_MAX) return false;
		
		if(parziale.size()==0) return true;
		
		if(parziale.size()==1 || parziale.size()==2) {
			return parziale.get(parziale.size()-1).equals(citta);
		}
		
		if(parziale.get(parziale.size()-1).equals(citta)) return true;
		
		if(!parziale.get(parziale.size()-1).equals(citta)) {
				if(parziale.get(0).equals(parziale.get(1)) && parziale.get(2).equals(parziale.get(1))) return true;
			}
		
		
		for(int i =0; i < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN-1; i++) {
			if(!parziale.get(parziale.size()-(i+1)).equals(parziale.get(parziale.size()-(i+2)))) return false;
		}
		return false;
	}

	private double costototale(List<Citta> best) {
		double costo = 0.0;
		
		for(int giorno=1;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			Citta c = best.get(giorno-1);
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			costo+=umid;
		}
		
		for(int giorno=2;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			if(!best.get(giorno-1).equals(best.get(giorno-2))) {
				costo+=COST;
			}
		}
		return costo;
	}

	public List<Citta> citta(){
		return dao.tuttelecitta();
	}
	

}
