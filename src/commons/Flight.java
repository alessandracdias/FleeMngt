package commons;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author ivopdm
 * 
 */
public class Flight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_FlightID;
	private String m_origem;
	private String m_destino;
	private Date m_dataEtd;
	private Date m_dataEta;
	private Double m_fuelKG;
	private Double m_flightValue;
	// variavel auxiliar para ordenacao da lista.
	private String m_Aircraft;

	public String getM_FlightID() {
		return m_FlightID;
	}

	public String getM_origem() {
		return m_origem;
	}

	public void setM_origem(String m_origem) {
		this.m_origem = m_origem;
	}

	public String getM_destino() {
		return m_destino;
	}

	public void setM_destino(String m_destino) {
		this.m_destino = m_destino;
	}

	public Date getM_dataEtd() {
		return m_dataEtd;
	}

	public void setM_dataEtd(Date m_dataEtd) {
		this.m_dataEtd = m_dataEtd;
	}

	public Date getM_dataEta() {
		return m_dataEta;
	}

	public void setM_dataEta(Date m_dataEta) {
		this.m_dataEta = m_dataEta;
	}

	public Double getM_fuelKG() {
		return m_fuelKG;
	}
	
	public Double getFuelKG(Aircraft aircraft) {
		return null;
	}

	public void setM_fuelKG(Double m_fuelKG) {
		this.m_fuelKG = m_fuelKG;
	}

	public void setM_FlightID(String m_FlightID) {
		this.m_FlightID = m_FlightID;
	}

	public Double getM_flightValue() {
		return m_flightValue;
	}

	public void setM_flightValue(Double m_flightValue) {
		this.m_flightValue = m_flightValue;
	}

	public String getM_Aircraft() {
		return m_Aircraft;
	}

	public void setM_Aircraft(String m_Aircraft) {
		this.m_Aircraft = m_Aircraft;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_FlightID == null) ? 0 : m_FlightID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (m_FlightID == null) {
			if (other.m_FlightID != null)
				return false;
		} else if (!m_FlightID.equals(other.m_FlightID))
			return false;
		return true;
	}
	
	public String toJSON() {
		String output = "{ ";
		
		output += " flightId: " + this.m_FlightID + ",";
		output += " origin: " + this.m_origem + ",";
		output += " destination: " + this.m_destino + "}";
		
		return output;
	}
	
	public String toString() {
		return toJSON();
	}

}
