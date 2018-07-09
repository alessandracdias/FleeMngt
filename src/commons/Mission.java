package commons;

public class Mission {
	private long missionLength; //Length of the mission i for aircraft j
	private long missionDeprec; //Depreciation coefficient of mission i	
	private int missionID;
	
	
	public Mission(int missionID) {
		super();
		this.missionID = missionID;
	}
	public long getMissionLength() {
		return missionLength;
	}
	public void setMissionLength(long missionLength) {
		this.missionLength = missionLength;
	}
	public long getMissionDeprec() {
		return missionDeprec;
	}
	public void setMissionDeprec(long missionDeprec) {
		this.missionDeprec = missionDeprec;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + missionID;
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
		Mission other = (Mission) obj;
		if (missionID != other.missionID)
			return false;
		return true;
	}
	
	
}
