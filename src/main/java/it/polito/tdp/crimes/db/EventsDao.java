package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;

public class EventsDao {
	
	public List<Event> listAllEventsByDate(Integer anno, Integer mese, Integer giorno){
		String sql = "SELECT * "
				+ "FROM EVENTS "
				+ "WHERE YEAR(reported_date) = ?  "
				+ "AND MONTH(reported_date) = ? "
				+ "AND DAY(reported_date) = ? "
				+ "ORDER BY reported_date" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getYears() {
		String sql = "SELECT DISTINCT YEAR(reported_date) AS anno "
				+ "FROM events "
				+ "ORDER BY anno" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("anno"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getMonths(Integer anno) {
		String sql = "SELECT DISTINCT MONTH(reported_date) AS m "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ? "
				+ "ORDER BY m" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("m"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getDays(Integer anno) {
		String sql = "SELECT DISTINCT DAY(reported_date) AS d "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ? "
				+ "ORDER BY d" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("d"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Integer> getDistricts() {
		String sql = "SELECT DISTINCT district_id "
				+ "FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("district_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Double getLonMedia(Integer anno, Integer distretto) {
		String sql = "SELECT AVG(geo_lon) AS lon "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ?  "
				+ "AND district_id = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, distretto);
			
			Double lon = null;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					lon = res.getDouble("lon");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return lon ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Double getLatMedia(Integer anno, Integer distretto) {
		String sql = "SELECT AVG(geo_lat) AS lat "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ?  "
				+ "AND district_id = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, distretto);
			
			Double lat = null;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					lat = res.getDouble("lat");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return lat ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public Integer getDistrettoMenoCrimini(Integer anno) {
		String sql = "SELECT district_id "
				+ "FROM events "
				+ "WHERE YEAR(reported_date) = ? "
				+ "GROUP BY district_id "
				+ "ORDER BY COUNT(*) ASC "
				+ "LIMIT 1" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			Integer d = null;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					d = res.getInt("district_id");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return d ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
