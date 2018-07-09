package behaviours;

import org.apache.commons.math3.distribution.NormalDistribution;

import agents.MSAgent;
import commons.Aircraft;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;

public class CheckHealth extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Em ciclos, voos
	private NormalDistribution m_PhmPdf = new NormalDistribution(5, 2);
	private Aircraft m_acft;
	private DataStore ds; 
	private int m_reqMtx;
	private int m_oppMtx;
	private String m_healthState;
	private ACLMessage m_info = new ACLMessage(ACLMessage.INFORM);
	
	@Override
	public void action() {
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());
		//Consulta estimativa de tempo de vida util
		int v_rul = m_acft.getRul();
		
		if(v_rul < m_oppMtx){
			m_healthState = "NO_MTX";
		}else if(v_rul >= m_oppMtx && v_rul < m_reqMtx){
			m_healthState = "OPP_MTX";
		}else{
			m_healthState = "REQ_MTX";

		}
		
		if(!m_healthState.equals("NO_MTX")){
			m_info.setContent(m_healthState);
			m_info.addReceiver(new AID(MSAgent.AGENT_NAME, AID.ISLOCALNAME));
			
			myAgent.send(m_info);
			
		}
		
	}

	
}
