/**
 * 
 */
package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import commons.Aerodrome;
import commons.Aircraft;
import commons.Airport;
//import commons.CarregarDadosExcel;
import commons.Flight;
import commons.Route;
import commons.TreeParser;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * @author IMEDEIRO
 * 
 *
 */
public class FleetManagement {

	private final static Logger logger = Logger.getMyLogger("Main.java");
	protected List<Aircraft> listaDeAvioes;
	protected List<Flight> listaFlights;
	protected List<Route> listaRoutes;
	protected Properties prop;

	protected String airportFileName;
	protected String flightsFileName;
	protected String fleetFileName;

	protected HashMap<String, Airport> airports;
	protected HashMap<String, Flight> flights;
	protected HashMap<String, Aircraft> fleet;

	private Double maintenance_penalty;
	private Integer downtime;
	private Integer overnight;

	protected String aircraftType;

	public FleetManagement(String iniFileName) {

		listaDeAvioes = new ArrayList<Aircraft>();
		listaFlights = new ArrayList<Flight>();
		listaRoutes = new ArrayList<Route>();
		airports = new HashMap<String, Airport>();

		prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));

			airportFileName = prop.getProperty("airports.data");
			flightsFileName = prop.getProperty("flights.data");
			fleetFileName = prop.getProperty("fleet.data");

			overnight = Integer.parseInt(prop.getProperty("conf.overnight").toString());
			downtime = Integer.parseInt(prop.getProperty("conf.downtime"));
			maintenance_penalty = Double.parseDouble(prop.getProperty("conf.maintenance_penalty"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		String iniFileName;

		if (args.length == 0) {
			iniFileName = "config.properties";
		} else {
			iniFileName = args[0];
		}

		File iniFile = new File(iniFileName);

		if (!iniFile.exists() || iniFile.isDirectory()) {
			System.err.println("Não foi possível carregar o arquivo " + iniFileName);
			System.exit(0);
		}

		logger.info("Carregando arquivo " + iniFileName);

		FleetManagement fleetMngt = new FleetManagement(iniFileName);

		try {
			fleetMngt.loadAvailableFleet();
			fleetMngt.loadFlightsAndRoutes();
			fleetMngt.loadAirports();

			fleetMngt.loadScenario();
			System.out.println("Done loading");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		fleetMngt.startSimulation();
	}

	public void loadScenario() {

		for (Aircraft acft : listaDeAvioes) {
			System.out.println("Aviao -> " + acft.getId());
			System.out.println("Performance -> " + acft.getFator());
		}
		// for (Flight voo : listaFlights) {
		// System.out.println("--------------------------------");
		// System.out.println("id: " + voo.getM_FlightID());
		// System.out.println("Origem: " + voo.getM_origem());
		// System.out.println("Destino: " + voo.getM_destino());
		// System.out.println("Data inicio: " + voo.getM_dataEtd());
		// System.out.println("--------------------------------");
		// }

		for (Route route : listaRoutes) {
			System.out.println("--------------------------------");
			System.out.println("Route -> " + route.getM_id());
			System.out.println("Fuel Total: " + route.getM_SumFuelKG());
			// System.out.println("Valor Total: " + route.getM_SumValue());
			// System.out.println("N�mero de Voos: " + route.getM_lstFlights().size());
			// System.out.println("--------------------------------");
		}

	}

	public void loadFlightsAndRoutes() throws Exception {
		listaFlights.clear();
		listaRoutes.clear();

		flights = new HashMap<String, Flight>();

		long routeIdCounter = 1;

		SimpleDateFormat formatComplete = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		SimpleDateFormat formatShort = new SimpleDateFormat("HH:mm");

		File fXmlFile = new File(flightsFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("route");

		// Routes
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNodeRoutes = nList.item(temp);

			if (nNodeRoutes.getNodeType() == Node.ELEMENT_NODE) {

				Route route = new Route();
				route.setM_id(routeIdCounter);

				NodeList flightNodes = nNodeRoutes.getChildNodes();

				for (int i = 0; i < flightNodes.getLength(); i++) {

					Node nNodeFlight = flightNodes.item(i);

					if (nNodeFlight.getNodeType() != Node.ELEMENT_NODE)
						continue;

					Element eElement = (Element) nNodeFlight;

					String id = eElement.getAttribute("id");

					String flightID = eElement.getElementsByTagName("flightID").item(0).getTextContent();
					String origin = eElement.getElementsByTagName("origin").item(0).getTextContent();
					String destination = eElement.getElementsByTagName("destination").item(0).getTextContent();
					String departureTime = eElement.getElementsByTagName("departureTime").item(0).getTextContent();
					String arrivalTime = eElement.getElementsByTagName("arrivalTime").item(0).getTextContent();
					String flightValue = eElement.getElementsByTagName("flightValue").item(0).getTextContent();
					String fuel = eElement.getElementsByTagName("fuel").item(0).getTextContent();

					Flight flight = new Flight();
					flight.setM_FlightID(flightID);
					flight.setM_origem(origin);
					flight.setM_destino(destination);

					if (departureTime.length() == 5) { // "HH:mm"
						flight.setM_dataEtd(new Date(formatShort.parse(departureTime).getTime()));
					} else { // "MM/dd/yyyy HH:mm"
						flight.setM_dataEtd(new Date(formatComplete.parse(departureTime).getTime()));
					}

					if (arrivalTime.length() == 5) {
						flight.setM_dataEta(new Date(formatShort.parse(arrivalTime).getTime()));
					} else {
						flight.setM_dataEta(new Date(formatComplete.parse(arrivalTime).getTime()));
					}

					Double valorFuel = Double.parseDouble(fuel);
					flight.setM_fuelKG(valorFuel);
					Double doubleFlightValue = Double.parseDouble(flightValue);
					flight.setM_flightValue(doubleFlightValue);

					flights.put(id, flight);
					listaFlights.add(flight); // Compatibilidade com modo antigo

					route.addFlight(flight);
				}

				listaRoutes.add(route);
				routeIdCounter++;
			}
		}

	}

	public void loadAirports() throws Exception {

		File fXmlFile = new File(airportFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("aerodrome");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				String name = eElement.getElementsByTagName("name").item(0).getTextContent();
				String location = eElement.getElementsByTagName("location").item(0).getTextContent();
				String iata = eElement.getElementsByTagName("iata").item(0).getTextContent();

				String icao = eElement.getElementsByTagName("icao").item(0).getTextContent();
				String latitude = eElement.getElementsByTagName("latitude").item(0).getTextContent();
				String longitude = eElement.getElementsByTagName("longitude").item(0).getTextContent();
				String maintenence_base = eElement.getElementsByTagName("maintenence_base").item(0).getTextContent();

				Airport airport = new Airport();

				airport.setM_iata(iata);
				airport.setM_icao(icao);
				airport.setM_latitude(Double.parseDouble(latitude));
				airport.setM_location(location);
				airport.setM_longitude(Double.parseDouble(longitude));
				airport.setM_maintenence_base(maintenence_base);
				airport.setM_name(name);
				airports.put(iata, airport);
			}
		}

	}

	public void loadAvailableFleet() throws Exception {

		listaDeAvioes.clear();

		HashMap<String, Aircraft> fleet = new HashMap<String, Aircraft>();

		File fXmlFile = new File(fleetFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		// Aircraft type
		Node fleetNode = doc.getElementsByTagName("fleet").item(0);
		this.aircraftType = ((Element) fleetNode).getAttribute("type");

		NodeList nList = doc.getElementsByTagName("aircraft");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				String id = eElement.getAttribute("id");
				String label = eElement.getElementsByTagName("label").item(0).getTextContent();
				String currentLocation = eElement.getElementsByTagName("currentLocation").item(0).getTextContent();
				String efficiency = eElement.getElementsByTagName("efficiency").item(0).getTextContent();
				String srlu = eElement.getElementsByTagName("srlu").item(0).getTextContent();

				Aircraft aircraft = new Aircraft();
				aircraft.setId(id);
				aircraft.setCurrLoc(currentLocation);
				aircraft.setFator(Double.parseDouble(efficiency));

				if (srlu.isEmpty()) {
					Double probabilityOfFailure = TreeParser.evalTree();
					aircraft.setProbabilityOfFailure(probabilityOfFailure);
				}

				else {
					aircraft.setProbabilityOfFailure(Double.parseDouble(srlu));
				}

				if (!label.equalsIgnoreCase("")) {
					aircraft.setNome(label);
				}

				fleet.put(id, aircraft);

				listaDeAvioes.add(aircraft);
			}
		}

	}

	public void startSimulation() {
		// Get a hold on JADE runtime
		Runtime rt = Runtime.instance();

		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);

		// Launch a complete platform on the 8888 port
		// create a default Profile
		Profile pMain = new ProfileImpl(null, 8888, null);

		System.out.println("Launching a whole in-process platform..." + pMain);
		AgentContainer mc = rt.createMainContainer(pMain);

		System.out.println("Launching the rma agent on the main container ...");
		// AgentController rma;
		// AgentController sniffer;
		// AgentController introspector;

		try {

			if (listaDeAvioes != null && !listaDeAvioes.isEmpty()) {
				for (int i = 0; i < listaDeAvioes.size(); i++) {
					AgentController acft = mc.createNewAgent(listaDeAvioes.get(i).getId().toString(),
							agents.AircraftAgent.class.getName(), new Object[] { listaDeAvioes.get(i), airports, overnight,downtime,maintenance_penalty});
					acft.start();
				}
			} else {
				logger.warning("A lista de Aircraft est� vazia.");
			}

			if (listaDeAvioes != null && !listaDeAvioes.isEmpty() && listaRoutes != null && !listaRoutes.isEmpty()) {
				AgentController tas = mc.createNewAgent("TAS", agents.TasAgent.class.getName(),
						new Object[] { listaRoutes, listaDeAvioes });
				tas.start();
			} else {
				logger.warning("A lista de Flights est� vazia.");
			}

			// rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new
			// Object[0]);
			// rma.start();

			// sniffer =
			// mc.createNewAgent("sniffer","jade.tools.sniffer.Sniffer", new
			// Object[0]);
			// sniffer.start();

			// introspector = mc.createNewAgent("introspector",
			// "jade.tools.introspector.Introspector", new Object[0]);
			// introspector.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
