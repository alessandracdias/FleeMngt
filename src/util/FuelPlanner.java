package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FuelPlanner {

	protected String aircraft;
	protected String originICAO;
	protected String destinationICAO;
	
	private double fuelUsageKG;
	private double fuelReserveKG;
	
	private double fuelUsageTime;
	private double fuelReserveTime;

	public FuelPlanner(String aircraft, String originICAO, String destinationICAO) {
		this.aircraft = aircraft;
		this.originICAO = originICAO;
		this.destinationICAO = destinationICAO;
		
		this.fetch();
	}

	public boolean fetch() {
		
		if (this.loadFromCache()) {
			return true;
		}
		
		System.out.print("Fetching fuel usage of " + this.aircraft + " from " + this.originICAO + " to " + this.destinationICAO + "... ");
		String url = "http://fuelplanner.com/index.php";

		try {
			Document doc = Jsoup.connect(url)
					.data("okstart","1")
					.data("EQPT", this.aircraft)
					.data("ORIG", this.originICAO)
					.data("DEST",this.destinationICAO)
					.data("submit","PLANNER")
					.data("RULES","FARDOM")
					.data("TANKER","")
					.data("UNITS","METRIC")
					.data("OEW","")
					.data("TTL","")
					.post();
			
			Element table = doc.selectFirst("table");
			Elements rows = table.select("tr");
			
			for (int i = 1; i < rows.size(); i++) {
			    Element row = rows.get(i);
			    Elements cols = row.select("td");
			    
			    if (i==1) {
			    	this.fuelUsageKG = Double.parseDouble(cols.get(1).text());
			    	this.fuelUsageTime = convertTime(cols.get(2).text());
			    }
			    
			    if (i==2) {
			    	this.fuelReserveKG = Double.parseDouble(cols.get(1).text());
			    	this.fuelReserveTime = convertTime(cols.get(2).text());
			    }
			    
			    if (i==3) {
			    	break;
			    }
			}
			
			System.out.println(this.getTotalFuelUsage() + " KG");
			saveToCache();
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public double getFuelUsage() {
		return this.fuelUsageKG;
	}
	
	public double getReserveFuel() {
		return this.fuelReserveKG;
	}
	
	public double getFuelUsageTime() {
		return this.fuelUsageTime;
	}
	
	public double getReserveTime() {
		return this.fuelReserveTime;
	}
	
	public double getTotalFuelUsage() {
		return this.fuelUsageKG + this.fuelReserveKG;
	}
	
	public double getTotalFuelTime() {
		return this.fuelUsageTime + this.fuelReserveTime;
	}
	
	private boolean loadFromCache() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:fuelplannercache.db");
			stmt = c.createStatement();
			
			this.createTableIfNotExists(c, stmt);

			ResultSet rs = stmt.executeQuery("SELECT * FROM FUEL WHERE AIRCRAFT='" +  this.aircraft + "' AND ORIGIN='"+this.originICAO+"' AND DESTINATION='"+this.destinationICAO+"'");

			while (rs.next()) {
				
				this.fuelUsageKG = rs.getDouble("FUELUSAGE");
				this.fuelReserveKG = rs.getDouble("FUELRESERVE");
				this.fuelUsageTime = rs.getDouble("FUELUSAGETIME");
				this.fuelReserveTime = rs.getDouble("FUELRSERVETIME");

				stmt.close();
				c.close();

				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	
	private void saveToCache() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:fuelplannercache.db");
			stmt = c.createStatement();

			this.createTableIfNotExists(c, stmt);

			stmt.executeUpdate("INSERT INTO FUEL VALUES ('"+this.aircraft+"', '"+this.originICAO+"', '"+this.destinationICAO+"', "+this.fuelUsageKG+", "+this.fuelReserveKG+","+this.fuelUsageTime+","+this.fuelReserveTime+")");

			stmt.close();
			c.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(this.toString());
		}
	}
	
	private void createTableIfNotExists(Connection c, Statement stmt) throws SQLException {
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FUEL (AIRCRAFT TEXT NOT NULL, ORIGIN TEXT NOT NULL, DESTINATION TEXT, FUELUSAGE REAL, FUELRESERVE REAL, FUELUSAGETIME REAL, FUELRSERVETIME REAL)");
	}
	
	private static double convertTime(String time) {
		StringTokenizer tk = new StringTokenizer(time, ":");
		
		double hours = Double.parseDouble(tk.nextToken());
		double minutes = Double.parseDouble(tk.nextToken());
		
		return hours + minutes/60.0;
	}

	public static void main(String[] args) {
		FuelPlanner planner = new FuelPlanner("A300", "EHAM", "ENVA");
		planner.fetch();
		System.out.println(planner.getTotalFuelUsage() + " Kg");
		System.out.println(planner.getTotalFuelTime() + " h");
	}

}
