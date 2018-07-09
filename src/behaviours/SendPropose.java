package behaviours;

import java.io.IOException;

import commons.Aircraft;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendPropose extends OneShotBehaviour {

	/**
	 * Behaviour do agente AircraftAgent responsavel pela acao de enviar uma
	 * proposta para a instancia do agente TasAgent.
	 */
	private static final long serialVersionUID = 3591700095749070623L;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		
		DataStore v_ds = getDataStore();
		Aircraft acft  = (Aircraft) v_ds.get(myAgent.getLocalName());
		
		Double v_prop = (Double) v_ds.get(myAgent.getLocalName() + "_PROPOSAL");
		acft.setM_propose(v_prop);
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");

		ACLMessage v_aclPropose = v_cfp.createReply();
		v_aclPropose.setPerformative(ACLMessage.PROPOSE);
	
		
		try {
			//alesdias
			 v_aclPropose.setContentObject(acft);
			//v_aclPropose.setContentObject(v_prop);
		} catch (IOException e) {
			e.printStackTrace();
		}

		myAgent.send(v_aclPropose);

		m_logger.info(myAgent.getLocalName() + " proposes -> " + v_prop);
					
	}

}
