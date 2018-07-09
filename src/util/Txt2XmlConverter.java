package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import util.Txt2XmlConverter.FlightLeg;

public class Txt2XmlConverter {
	
	public static void main(String[] args) {
		
		try {
			new Txt2XmlConverter().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void run() throws Exception {
		//BufferedReader reader = new BufferedReader(new FileReader("FlightLegsKLNovo.txt"));
		//BufferedReader reader = new BufferedReader(new FileReader("FlightLegsRepublic.txt"));
		BufferedReader reader = new BufferedReader(new FileReader("resources/data/FlightLegsRepublicNovo.txt"));
		HashMap<String, Vector<FlightLeg>> map = new HashMap<String, Vector<FlightLeg>>();
		
		String line;
		StringTokenizer tk;
		
		System.out.println("<?xml version=\"1.0\"?>");
		System.out.println("<flights>");
		
		while(true) {
			
			line = reader.readLine();
			
			if (line == null) {
				break;
			}
			
			tk = new StringTokenizer(line, "\t");
			
			String legId = tk.nextToken();
			String flightNbr = tk.nextToken();
			String legOrig = tk.nextToken();
			String legDest = tk.nextToken();
			String legETD = tk.nextToken();
			String legETA = tk.nextToken();
			String acftType = tk.nextToken();
			String routeID = tk.nextToken();
			
			
			if (!map.containsKey(routeID)) {
				map.put(routeID, new Vector<FlightLeg>());
			}
			
			map.get(routeID).addElement(new FlightLeg(legId,flightNbr,legOrig,legDest,legETD,legETA));

			/*
			System.out.println("\t<flight id=\""+flightNbr+"\">");
			System.out.println("\t\t<flightID>"+flightNbr+"</flightID>");
			System.out.println("\t\t<origin>"+legOrig+"</origin>");
			System.out.println("\t\t<destination>"+legDest+"</destination>");
			System.out.println("\t\t<departureTime>"+legETD+"</departureTime>");
			System.out.println("\t\t<arrivalTime>"+legETA+"</arrivalTime>");
			System.out.println("\t</flight>");
			System.out.println("");
			*/
			
		}
		
		for (String key : map.keySet()) {
			
			/*
			
				<flightID>KL1880</flightID>
				<origin>NUE</origin>
				<destination>AMS</destination>
				<departureTime>04:00</departureTime>
				<arrivalTime>05:20</arrivalTime>
			
			*/
			System.out.println("\t<route>");
			for (FlightLeg leg : map.get(key)) {
				Random randomno = new Random();
				Double flightValue = (double) ((randomno.nextInt(6) * 1000) + 5000);
				flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
								
				System.out.println("\t\t<flight id=\""+leg.fltNbr+"\">");
				System.out.println("\t\t\t<flightID>"+leg.fltNbr+"</flightID>");
				System.out.println("\t\t\t<origin>"+leg.legOrig+"</origin>");
				System.out.println("\t\t\t<destination>"+leg.legDest+"</destination>");
				System.out.println("\t\t\t<departureTime>"+leg.legETD+"</departureTime>");
				System.out.println("\t\t\t<arrivalTime>"+leg.legETA+"</arrivalTime>");
				System.out.println("\t\t\t<flightValue>"+flightValue+"</flightValue>");
				System.out.println("\t\t</flight>");
				System.out.println();
			}
			System.out.println("\t</route>\n");
			
			//System.out.println(map.get(key).get(0).fltNbr);
			//System.out.println(key);
		}
		
		System.out.println("</flights>");
		
		reader.close();
	}
	
	class FlightLeg{
		String legId;
		String fltNbr;
		String legOrig;
		String legDest;
		String legETD;
		String legETA;
		
		public FlightLeg(String legId, String fltNbr, String legOrig, String legDest, String legETD, String legETA) {
			this.legId = legId;
			this.fltNbr = fltNbr;
			this.legOrig = legOrig;
			this.legDest = legDest;
			this.legETD = legETD;
			this.legETA = legETA;
		}
	}
}
