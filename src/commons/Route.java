package commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long m_id;
	private List<Flight> m_lstFlights;

	public Route() {
		m_lstFlights = new ArrayList<Flight>();
	}
	
	public boolean addFlight(Flight flight) {
		
		if (this.m_lstFlights.isEmpty()) {
			this.m_lstFlights.add(flight);
			return true;
		}
		else {
			Flight lastFlight = m_lstFlights.get(m_lstFlights.size()-1); 
			if (lastFlight.getM_destino().equalsIgnoreCase(flight.getM_origem())) {
				this.m_lstFlights.add(flight);
				return true;
			}
			else {
				System.err.println("Inconsistencia: o voo " + flight.getM_FlightID() + " nao pode ser adicionaro a roda pois nao eh uma continua do voo anterior.");
				return false;
			}
		}
	}
	
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public List<Flight> getM_lstFlights() {
		return m_lstFlights;
	}

	public void setM_lstFlights(List<Flight> m_lstFlights) {
		this.m_lstFlights = m_lstFlights;
	}

	public Double getM_SumValue() {
		
		if (this.m_lstFlights.size() == 0)
			return 0.0;

		double sumValue = 0;
		
		for (Flight flight : this.m_lstFlights) {
			sumValue += flight.getM_flightValue();
		}
		return sumValue;
	}

	public Double getM_SumFuelKG() {
		
		if (this.m_lstFlights.size() == 0)
			return 0.0;

		double sumFuelKG = 0;
		
		for (Flight flight : this.m_lstFlights) {
			sumFuelKG += flight.getM_fuelKG();
		}
		return sumFuelKG;
	}
	
	public boolean verifyConsitency() {
		
		String lastDestination = null;
		for (Flight flight : this.m_lstFlights) {
			
			if (lastDestination == null) continue;
			
			if (!flight.getM_origem().equalsIgnoreCase(lastDestination)) {
				return false;
			}
			
			lastDestination = flight.getM_destino();
		}
		
		return true;
	}
	
	public String toString() {
		String output = "";
		
		for (Flight flight : this.m_lstFlights) {
			output += flight.toString() + "\n";
		}
		
		return output;
	}
 	
}
