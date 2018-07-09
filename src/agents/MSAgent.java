package agents;

import behaviours.ScheduleMtx;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MSAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String AGENT_NAME = "MSAgent";
	
	
	@Override
	protected void setup() {
		super.setup();
		getAID().setLocalName(AGENT_NAME);
		
		Behaviour schedMtx = new ScheduleMtx();		
		addBehaviour(schedMtx);
		
	}

}
