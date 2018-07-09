package behaviours;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import agents.TasAgent;
import commons.Aircraft;
import commons.Flight;
import commons.Route;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;

public class FinishAssignment extends OneShotBehaviour {

	/**
	 * Behaviour do agente TasAgent responsavel por apresentar o resultado final
	 * alocacao de avioes aos voos.
	 */
	private static final long serialVersionUID = 4418337351324599137L;
	private HashMap<Route, Aircraft> m_assignment;
	private final Double VALORCOMBUSTIVEL = Double.valueOf(531D);
	
	public FinishAssignment(HashMap<Route, Aircraft> p_assignment) {
		this.m_assignment = p_assignment;
	}

	@Override
	public void action() {
		Aircraft v_acft;
		Route v_route;
		double v_TU = 0.0;
		
		// aviao -voo -hora saida e -hora chegada
//		for (Map.Entry<Route, Aircraft> v_tas : m_assignment.entrySet()) {
//			// System.out.println("Rota => " + v_tas.getKey().getM_id() + " |
//			// Aviao => " + v_tas.getValue());
//			v_acft = v_tas.getValue();
//			v_route = v_tas.getKey();
//			
//			System.out.println("Aviao =>" + v_acft.getId());
//			System.out.println("Route => " + v_route.getM_id());
//			
//			v_TU += v_route.getM_SumValue() - 
//									(v_acft.getFator() * v_route.getM_SumFuelKG() * VALORCOMBUSTIVEL/1e3);
//									
//			for (Flight obj : v_tas.getKey().getM_lstFlights()) {
//				// String dataFormatada = new SimpleDateFormat("MM/dd/yyyy
//				// HH:mm").format(obj.getM_dataEtd());
//				System.out.println("Voo-> " + obj.getM_FlightID() + " Saindo de " + obj.getM_origem() + " as "
//						+ new SimpleDateFormat("HH:mm").format(obj.getM_dataEtd()) + " no dia "
//						+ new SimpleDateFormat("dd/MM/yy").format(obj.getM_dataEtd()) + ", com Destino a "
//						+ obj.getM_destino() + " as " + new SimpleDateFormat("HH:mm").format(obj.getM_dataEta())
//						+ " no dia " + new SimpleDateFormat("dd/MM/yy").format(obj.getM_dataEta()) + " .");
//			}
//			System.out.println("---------------------------------------------------------------------------");
//		}
		
		System.out.println("Aviao,Rota,Localizacao,Performance,Combustivel total da rota,Valor da rota");
		
		for (Map.Entry<Route, Aircraft> v_tas : m_assignment.entrySet()) {
			
			v_acft = v_tas.getValue();
			v_route = v_tas.getKey();
			
			
			System.out.println(v_acft.getId() + "," +
								v_route.getM_id() + "," +
								v_acft.getCurrLoc() + "," +
								v_acft.getFator() + "," +
								v_route.getM_SumFuelKG() + "," +
								v_route.getM_SumValue());
				
			v_TU += v_route.getM_SumValue() - 
					(v_acft.getFator() * v_route.getM_SumFuelKG() * VALORCOMBUSTIVEL/1e3);

			
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");		
		DataStore v_ds = getDataStore();
		System.out.println("Number of Iterations => " + (Integer) v_ds.get(TasAgent.KEY_ITERATION));
		System.out.println("Total Utility Value => " + v_TU);
		
	}

}
