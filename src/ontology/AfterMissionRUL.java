package ontology;

import jade.content.Predicate;


public class AfterMissionRUL implements Predicate{
	
	private int vehicleID;
	private double RUL;
	
	public int getVehicleID() {
		return vehicleID;
	}
	
	public void setVehicleID(int vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public double getRUL() {
		return RUL;
	}
	
	public void setRUL(double rUL) {
		RUL = rUL;
	}
	
	

}
