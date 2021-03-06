package commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ivopdm 
 */
public class Aircraft implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String nome;
	private Double fator = Double.valueOf(1.0);
	private Double price = Double.valueOf(0.0);
	private List<Flight> route;
	private String currLoc;
	private int m_rul;
	private String m_mtxState;
	private Double m_propose;
	private static final Double T1_LIMIT = 30.0;
	private static final Double T2_LIMIT = 70.0;

	private MaintenanceStatus m_maintenanceStatus;

	public Double getM_propose() {
		return m_propose;
	}

	public void setM_propose(Double m_propose) {
		this.m_propose = m_propose;
	}

	public Aircraft() {
		setRoute(new ArrayList<Flight>());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getFator() {
		return fator;
	}

	public void setFator(Double fator) {
		this.fator = fator;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<Flight> getRoute() {
		return route;
	}

	public void setRoute(List<Flight> route) {
		this.route = route;
	}

	public String getCurrLoc() {
		return currLoc;
	}

	public void setCurrLoc(String currLoc) {
		this.currLoc = currLoc;
	}

	public int getRul() {
		return m_rul;
	}

	public String getMtxState() {
		return m_mtxState;
	}

	public MaintenanceStatus getMaintenanceStatus() {
		return m_maintenanceStatus;
	}

	public void setProbabilityOfFailure(Double probability) {
		if (probability >= T1_LIMIT && probability < T2_LIMIT)
			m_maintenanceStatus = MaintenanceStatus.T1;
		else if (probability >= T2_LIMIT)
			m_maintenanceStatus = MaintenanceStatus.T2;
		else
			m_maintenanceStatus = MaintenanceStatus.T3;
	}

	public String toXml() {
		String xml = "";
		xml += "<aircraft id=\"" + this.getId() + "\">\n";
		xml += "    <name>" + this.getNome() + "</name>\n";
		xml += "    <currentLocation>" + this.getCurrLoc() + "</currentLocation>\n";
		xml += "    <efficiency>" + this.getFator().toString() + "</efficiency>\n";
		xml += "</aircraft>\n";
		return xml;
	}

	public String toString() {
		return toXml();
	}

}
