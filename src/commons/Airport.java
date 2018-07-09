package commons;

import java.io.Serializable;

/**
 * 
 * @author alesdias
 * 
 */
public class Airport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_name;
	private String m_location;
	private String m_iata;
	private String m_icao;
	private Double m_longitude;
	private Double m_latitude;
	private String m_maintenence_base;

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getM_location() {
		return m_location;
	}

	public void setM_location(String m_location) {
		this.m_location = m_location;
	}

	public String getM_iata() {
		return m_iata;
	}

	public void setM_iata(String m_iata) {
		this.m_iata = m_iata;
	}

	public String getM_icao() {
		return m_icao;
	}

	public void setM_icao(String m_icao) {
		this.m_icao = m_icao;
	}

	public Double getM_longitude() {
		return m_longitude;
	}

	public void setM_longitude(Double m_longitude) {
		this.m_longitude = m_longitude;
	}

	public Double getM_latitude() {
		return m_latitude;
	}

	public void setM_latitude(Double m_latitude) {
		this.m_latitude = m_latitude;
	}

	public String getM_maintenence_base() {
		return m_maintenence_base;
	}

	public void setM_maintenence_base(String m_maintenence_base) {
		this.m_maintenence_base = m_maintenence_base;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean isMaintenence_base() {
		if (m_maintenence_base.contains("YES"))
			return true;
		else
			return false;
	}

}
