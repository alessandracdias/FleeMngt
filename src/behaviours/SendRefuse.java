package behaviours;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendRefuse extends OneShotBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6794549923416482849L;
		
	@Override
	public void action() {
		DataStore v_ds = getDataStore();
		ACLMessage v_cfp = (ACLMessage) v_ds.get("CFP");
		
		ACLMessage v_aclRefuse = v_cfp.createReply();
		v_aclRefuse.setPerformative(ACLMessage.REFUSE);
		myAgent.send(v_aclRefuse);
				
	}

}
