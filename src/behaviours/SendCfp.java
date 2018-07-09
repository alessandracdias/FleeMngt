package behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import agents.TasAgent;
import commons.Aircraft;
import commons.Route;
import jade.core.AID;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendCfp extends OneShotBehaviour {

	/**
	 * Envia mensagem do tipo CFP para 
	 * instancias de AircraftAgent.
	 * Conteudo de a mensagem CFP e a ID do voo que precisa de um aviao
	 */
	private static final long serialVersionUID = 3903080487616288791L;
	private HashMap<Route,Aircraft> m_assignment;

	private List<AID> m_recList;
	private List<Route> m_unassignedList = new ArrayList<Route>();
	private final Logger m_logger = Logger.getLogger(getClass().getName()); 
	private ACLMessage m_cfp = new ACLMessage(ACLMessage.CFP);

	public SendCfp(HashMap<Route,Aircraft> p_assignment, List<AID> p_recList) {
		this.m_assignment = p_assignment;
		this.m_recList = p_recList;
		m_cfp.clearAllReceiver();
		//Set receiver for CFP
		for (AID aid : m_recList) {
			m_cfp.addReceiver(aid);
			m_logger.log(Level.INFO, "Receiver: {0}", aid.getLocalName() );
		}
	}

	@Override
	public void action() {

		DataStore v_ds;
		v_ds = getDataStore();

		fillUnAssList();

		try {
			Route v_route = m_unassignedList.remove(0);
			m_cfp.setContentObject(v_route);
			myAgent.send(m_cfp);
			
			int v_iteration = (Integer) v_ds.get(TasAgent.KEY_ITERATION);
			v_ds.put(TasAgent.KEY_ITERATION, v_iteration+1);
			
			v_ds.put(TasAgent.KEY_CURRENT_UNASSIGNED, v_route);

			m_logger.log(Level.INFO, "Unassigned Route -> {0}", 
					v_route.getM_id());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void fillUnAssList() {
		if(m_unassignedList.isEmpty()){
			for (Map.Entry<Route, Aircraft> v_unAssign : m_assignment.entrySet()) {

				//if(v_unAssign.getValue().equals(TasAgent.FLIGHT_UNASSIGNED)){
				if(v_unAssign.getValue() == null){	
					m_unassignedList.add(v_unAssign.getKey());
				}
			}

			// CalcFlight.ordenaPorData(m_unassignedList);
		}

	}

}
