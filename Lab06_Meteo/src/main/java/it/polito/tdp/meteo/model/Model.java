package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	MeteoDAO dao ;
	List<Citta> tutteLeCitta ; 
	List<Citta> best ;
	
	public Model() {
		dao = new MeteoDAO();
		tutteLeCitta = dao.getAllCitta();
		best = new ArrayList<Citta>();
	}
	
	public Map<Citta,Double> getUmiditaMedia(int mese){
		Map<Citta,Double> mappa = new HashMap<Citta,Double>();
		
		for(Citta c : tutteLeCitta) {
			mappa.put(c, dao.getUmiditaMedia(mese, c));
		}
		return mappa;
	}
	

	public List<Citta> result(int mese){
		this.best=null;
		
		for(Citta c : tutteLeCitta) {
			c.setRilevamenti(dao.rilevamentipermese(mese));
		}
		
		cerca(new ArrayList<Citta>(), 0);
		
		return best;
		
	}

	private void cerca(List<Citta> parziale, int livello) {
			
		if(livello == NUMERO_GIORNI_TOTALI) {
			Double costo = calcolacosto(parziale);
			if(best == null || calcolacosto(best)>costo) {
				best = new ArrayList<Citta>(parziale);
			}
			return;
		}
		
		for(Citta c : tutteLeCitta) {
			if(aggiuntaValida(parziale, c)) {
				parziale.add(c);
				cerca(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private boolean aggiuntaValida(List<Citta> parziale, Citta c) {

		int conta =0;
		if(parziale.size()==0) return true;

		for(Citta c1 : parziale) {
			if(c1.equals(c)) conta++;
		}
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX) return false;
		
		if(parziale.size()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
			if(parziale.get(parziale.size()-1).equals(c)) return true;
		}
		
		if(parziale.size()>=3) {
			if(parziale.get(parziale.size()-3).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-1))) {
				return true;
			}
		}
		
		return false;
	}

	private Double calcolacosto(List<Citta> parziale) {
		Double costo =0.0;
		for(int giorno =1;giorno<NUMERO_GIORNI_TOTALI;giorno++) {
			costo+=parziale.get(giorno-1).getRilevamenti().get(giorno-1).getUmidita();
		}
		
		for(int giorno=2;giorno<NUMERO_GIORNI_TOTALI;giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo+=COST;
			}
		}
		
		return costo;
	}
	
	
	
	
	

}
