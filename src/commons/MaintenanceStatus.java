package commons;

public enum MaintenanceStatus {

	T1("OPPORTUNITY"), T2("MANDATORY"), T3("NO_MAINTENANCE");
	private String descricao;

	MaintenanceStatus(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}