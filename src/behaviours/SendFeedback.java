package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.TasAgent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SendFeedback extends OneShotBehaviour {

	/**
	 * Behaviour do agente AircraftAgent
	 * responsavel por enviar resposta para instancias
	 * vencedora (ACCEPT_PROPOSAL) e perdedoras (REJECT_PROPOSAL).
	 * O conteudo de ACCEPT_PROPOSAL e o valor que deve ser somado 
	 * ao preco atual que vai fazer parte de do behaviour UPD_PRICE
	 */
	private static final long serialVersionUID = 8097174469802047304L;
	
	
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	private ACLMessage v_msg2loser = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
	
	@Override
	public void action() {
		DataStore v_ds;
		v_ds = getDataStore();
				
		ACLMessage v_win_proposal = (ACLMessage) v_ds.get(TasAgent.KEY_WIN_PROPOSAL);
		Double v_bidIncrement = (Double) v_ds.get(TasAgent.KEY_BID_INCREMENT);
		List<ACLMessage> v_proposeList = (ArrayList<ACLMessage>) v_ds.get(TasAgent.KEY_PROPONENT_LIST);
		
		ACLMessage v_msg2win = v_win_proposal.createReply();		
		v_msg2win.setContent(v_bidIncrement.toString());
		v_msg2win.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		
		myAgent.send(v_msg2win);
				
		v_msg2loser.clearAllReceiver();
		for (ACLMessage aclMessage : v_proposeList) {
			if(aclMessage.getSender() != v_win_proposal.getSender())
				v_msg2loser.addReceiver(aclMessage.getSender());
		}
				
		myAgent.send(v_msg2loser);
		v_proposeList.clear();
		m_logger.info("FEEDBACK SENT TO ACFTs");
	}

}
