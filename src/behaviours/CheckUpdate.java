package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.TasAgent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class CheckUpdate extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean m_finished;
	private int m_proposal_counter;
	private MessageTemplate m_mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	private Logger m_logger = Logger.getMyLogger(getClass().getName());
	@Override
	public void action() {
		DataStore v_ds;
		v_ds = getDataStore();
		
		List<ACLMessage> v_proposeList = (ArrayList<ACLMessage>) v_ds.get(TasAgent.KEY_PROPONENT_LIST);
		
		m_finished = false;
		ACLMessage v_aclMsgInfo = myAgent.receive(m_mt );
		
		
		if (v_aclMsgInfo != null) {
			if(m_proposal_counter++==v_proposeList.size()){
				m_finished = true;
				m_proposal_counter = 0;
			}
						
			m_logger.info("Recebeu INFORM");
		} else {
			block();
		}
	}

	@Override
	public boolean done() {
		return m_finished;
	}

}
