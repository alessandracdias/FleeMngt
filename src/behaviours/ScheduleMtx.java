package behaviours;

import java.util.List;

import commons.Aircraft;
import commons.Flight;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ScheduleMtx extends Behaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MessageTemplate m_mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	private Aircraft m_acft;
	
	@Override
	public void action() {
		ACLMessage v_aclInfo = myAgent.receive(m_mt);
		
		if(v_aclInfo != null){
			try {
				m_acft = (Aircraft) v_aclInfo.getContentObject();
				
				String v_mtxState = m_acft.getMtxState();
				List<Flight> v_acft_route = m_acft.getRoute();
				
				if(v_mtxState.equals("OPP_MTX")){
					
				}else if(v_mtxState.equals("REQ_MTX")){
					
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}else{
			block();
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
