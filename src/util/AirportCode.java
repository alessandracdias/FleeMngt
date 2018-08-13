package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.lang.StringEscapeUtils;

public class AirportCode {

	protected String name;
	protected String location;
	protected String iataCode;
	protected String icaoCode;
	protected double latitude;
	protected double longitude;
	protected String maintenance_base;
	
	private String query;
	
	public AirportCode(String query) {
		this.query = query;
		
		this.fetch();
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getIataCode() {
		return iataCode;
	}

	public String getIcaoCode() {
		return icaoCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getMaintenance_base() {
		return maintenance_base;
	}

	public boolean fetch() {
		
		if (this.loadFromCache()) {
			return true;
		}

		String url = "https://www.world-airport-codes.com/search/?s=SBBR";

		try {
			System.out.print("Fetching airport data of " + this.query + "... ");
			Document doc = Jsoup.connect(url).data("s",this.query).get();

			Element table = doc.selectFirst("table[class=stack]");
			boolean success = false;
			
			if (table!=null) { // several results returned... must select one!

				Elements rows = table.select("tr");

				for (int i = 1; i < rows.size(); i++) {
					Element row = rows.get(i);
					Elements cols = row.select("td");
					Elements link = row.select("th");
					
					String urlNew = link.get(0).select("a").attr("href");
					String icaoCode = cols.get(0).text().trim();
					String iataCode = cols.get(1).text().trim();
					
					if (iataCode.equalsIgnoreCase(this.query) || icaoCode.equalsIgnoreCase(this.query)) {
						doc = Jsoup.connect(urlNew).get();
						success=true;
						break;
					}
				}
			}
			else {
				success = true;
			}
			
			if (success) {

				Element nameElement = doc.selectFirst("h1[class=\"airport-title\"]");
				Element locationElement = doc.selectFirst("p[class=subheader]");
				Element iataElement = doc.selectFirst("span[class=airportAttributeValue][data-key=\"IATA Code\"]");
				Element icaoElement = doc.selectFirst("span[class=airportAttributeValue][data-key=\"ICAO Code\"]");
				Element latitudeElement = doc.selectFirst("span[class=airportAttributeValue][data-key=\"Latitude\"]");
				Element longitudeElement = doc.selectFirst("span[class=airportAttributeValue][data-key=\"Longitude\"]");
				
				
				if (nameElement == null) {
					System.err.println("Airport " + this.query + " not found!");
					return true;
				}

				this.name = nameElement.text();
				this.location = locationElement.text();
				this.iataCode = iataElement.text();
				this.icaoCode = icaoElement.text();
				this.latitude = Double.parseDouble(latitudeElement.text());
				this.longitude = Double.parseDouble(longitudeElement.text());
				this.maintenance_base = "NO";
				
				this.saveToCache();
				
				System.out.println(this.getIcaoCode());
				return true;
			}
			else {
				System.err.println("Airport " + this.query + " not found!");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;

	}

	public String toJSON() {
		String obj = "{\n";
		obj += "  name: \""+this.name+"\" ,\n";
		obj += "  location: \""+this.location+"\",\n";
		obj += "  iatacode: \""+this.iataCode+"\",\n";
		obj += "  icaocode: \""+this.icaoCode+"\",\n";
		obj += "  latitude: "+this.latitude+",\n";
		//obj += "  longitude: "+this.longitude+"\n}\n";
		obj += "  longitude: "+this.longitude+",\n";
		obj += "  maintenence_base: \""+this.maintenance_base+"\",\n}\n"; 
		return obj;
	}

	public String toXML() {
		String obj = "";
		obj += "<aerodrome id=\""+this.iataCode+"\">\n";
		obj += "    <name>"+this.name+"</name>\n";
		obj += "    <location>"+this.location+"</location>\n";
		obj += "    <iata>"+this.iataCode+"</iata>\n";
		obj += "    <icao>"+this.icaoCode+"</icao>\n";
		obj += "    <latitude>"+this.latitude+"</latitude>\n";
		obj += "    <longitude>"+this.longitude+"</longitude>\n";
		obj += "    <maintenence_base>"+this.maintenance_base+"</maintenence_base>\n";
		obj += "</aerodrome>";

		return obj;
	}

	public String toString() {
		//return toJSON();
		return toXML();
	}
	
	private void saveToCache() {
		  Connection c = null;
	      Statement stmt = null;
	      
	      try {
	    	  Class.forName("org.sqlite.JDBC");
	    	  c = DriverManager.getConnection("jdbc:sqlite:airportcodecache.db");
	    	  stmt = c.createStatement();
	    	  
	    	  this.createTableIfNotExists(c, stmt);

	    	  //stmt.executeUpdate("INSERT INTO AIRPORTS VALUES ('"+this.iataCode+"','"+this.icaoCode+"','"+StringEscapeUtils.escapeSql(this.name)+"', '"+this.location+"', "+this.latitude+", "+this.longitude+")");
	    	  stmt.executeUpdate("INSERT INTO AIRPORTS VALUES ('"+this.iataCode+"','"+this.icaoCode+"','"+StringEscapeUtils.escapeSql(this.name)+"', '"+this.location+"', "+this.latitude+", "+this.longitude+",'"+this.maintenance_base+"')");
	    	  stmt.close();
	    	  c.close();
	      }
	      catch (Exception e) {
	    	  e.printStackTrace();
	      }
	}
	
	private void createTableIfNotExists(Connection c, Statement stmt) throws SQLException {
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AIRPORTS (IATA TEXT PRIMARY KEY NOT NULL, ICAO TEXT NOT NULL, NAME TEXT, LOCATION TEXT, LATITUDE REAL, LONGITUDE REAL)");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AIRPORTS (IATA TEXT PRIMARY KEY NOT NULL, ICAO TEXT NOT NULL, NAME TEXT, LOCATION TEXT, LATITUDE REAL, LONGITUDE REAL, MAINTENANCE BASE)");
	}
	
	private boolean loadFromCache() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:airportcodecache.db");
			stmt = c.createStatement();

			this.createTableIfNotExists(c, stmt);
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM AIRPORTS WHERE IATA='" +  this.query + "'");

			while (rs.next()) {
				this.iataCode = rs.getString("IATA");
				this.icaoCode = rs.getString("ICAO");
				this.name = rs.getString("NAME");
				this.location = rs.getString("LOCATION");
				this.latitude = rs.getFloat("LATITUDE");
				this.longitude = rs.getFloat("LONGITUDE");
				this.maintenance_base = "NO";
				
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

	public static void main(String[] args) {
		
		//String[] airports = {"ABZ", "AES", "ALL", "AMS", "BGO", "BHX", "BIO", "BLL", "BLQ", "BOD", "BRE", "BRS", "BRU", "CWL", "DUS", "FLR", "FRA", "GOT", "GVA", "HAM", "KRK", "LPI", "LUX", "LYS", "MAN", "NCE", "NCL", "NUE", "OSL", "PLR", "PRG", "STR", "TLS", "TRD", "TRF", "TRN", "TRO"};
		// String[] airports = {"GRU","GIG","POA","MAO","BSB","CWB","FLN","REC","SSA"};
		String[] airports = {"ABQ", "ATL", "BDL", "BNA", "CHS", "CLE", "CMH", "CZM", "DCA", "DEN", "DTW", "ELP",
				"EWR", "EYW", "FPO", "GGT", "GSO", "HDN", "IAH", "IND", "JAX", "JFK", "LGA", 
				"LIR", "MEM", "MHH", "MIA", "MSP", "MSY", "MTY", "NAS", "ORD", "ORF",
				"PHL", "PIT", "PLS", "PNS", "PVD", "RDU", "RIC", "RSW", "SAT", "SDF", "STL"};
		for (String airport : airports) {
			AirportCode code = new AirportCode(airport);
			while (code.fetch()==false) {
				System.err.println("Trying again for "+airport+"...");
			};

			System.out.println(code);
		}
		/*
		AirportCode code = new AirportCode("FKR");
		code.fetch();
		System.out.println(code);
		*/
	}
}
