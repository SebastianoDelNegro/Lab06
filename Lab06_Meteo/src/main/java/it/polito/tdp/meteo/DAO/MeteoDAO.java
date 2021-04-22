package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta localita) {
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		String sql = "SELECT Localita, Data, Umidita "
				+ "FROM situazione "
				+ "WHERE localita= ? AND MONTH(DATA)= ?";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita.getNome());
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			conn.close();
			return rilevamenti;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	
	}
	
	
	public Double umiditamedia(Citta citta, int mese) {
		String sql = "SELECT  localita, AVG(Umidita) "
				+ "FROM situazione "
				+ "WHERE localita= ? AND MONTH(data)= ? ";
		double umidita = 0.0;
		//Map<String,Double> umiditapercitta = new HashMap<String,Double>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta.getNome());
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				 umidita = rs.getDouble("AVG(Umidita)");
			}
			
			conn.close();
			return umidita;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> tuttelecitta(){
		
		String sql = "SELECT DISTINCT localita FROM situazione";
		List<Citta> citta = new ArrayList<Citta>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Citta c = new Citta(rs.getString("localita"));
				citta.add(c);
			}
			
			conn.close();
			return citta;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
