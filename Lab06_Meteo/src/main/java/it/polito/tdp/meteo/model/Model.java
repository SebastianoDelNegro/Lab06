package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO dao;
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		dao = new MeteoDAO();
	}

	// of course you can change the String output with what you think works best
	public Map<String,Double> getUmiditaMedia(int mese) {
		List<Double> umiditacittamese = new ArrayList<Double>();
		Map<String,Double> umiditamedia = new HashMap<String,Double>();
		List<String> citta = dao.tuttelecitta();
		for(String c : citta) {
			 umiditamedia.put(c, dao.umiditamedia(c, mese));
			
		}
		
		return umiditamedia;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> best = new ArrayList<Citta>();
		List<Citta> parziale = new ArrayList<Citta>();
		
		cerca(parziale, 0, best);
		
		return best;
	}
	
	
	private void cerca(List<Citta> parziale, int i, List<Citta> best) {
		//caso terminale
		if(i == NUMERO_GIORNI_TOTALI) {
			if(best == null || costototale(best)>costototale(parziale)) {
				best = parziale;
			}
			return ;
		}
		
		//caso normale
		
	}
	
	
	

	private int costototale(List<Citta> best) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<String> citta(){
		return dao.tuttelecitta();
	}
	

}
