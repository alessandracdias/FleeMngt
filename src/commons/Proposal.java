package commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proposal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Flight> route = new ArrayList<Flight>();
	private Double price;

	public List<Flight> getRoute() {
		return route;
	}

	public void setRoute(List<Flight> route) {
		this.route = route;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
