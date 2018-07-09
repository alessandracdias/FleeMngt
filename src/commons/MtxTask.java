package commons;

public class MtxTask {
	private int mtxDownTime;
	private double cost;
	
	
	public MtxTask(int mtxDownTime, double cost) {
		super();
		this.mtxDownTime = mtxDownTime;
		this.cost = cost;
	}
	public int getMtxDownTime() {
		return mtxDownTime;
	}
	public void setMtxDownTime(int mtxDownTime) {
		this.mtxDownTime = mtxDownTime;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
}
