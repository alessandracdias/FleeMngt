package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.AircraftAgent;
import commons.Aircraft;
import commons.Flight;
import commons.Proposal;
import commons.Route;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import util.CalcFlight;

public class CheckAdmission extends OneShotBehaviour {

	/**
	 * Behaviour/acao responsavel por validar se a instancia de AircraftAgent
	 * deve participar da negociacao/competicao para realizar o voo
	 */
	private static final long serialVersionUID = 8628498715010608266L;
	private Route m_route;
	private Aircraft m_acft;
	private final Double VALORCOMBUSTIVEL = Double.valueOf(531D);
	private DataStore ds; 

	private List<Flight> m_listaRotaProposta = new ArrayList<Flight>();
	private Proposal proposal = new Proposal();

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		ds = getDataStore();
		ACLMessage v_cfp = (ACLMessage) ds.get("CFP");
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());

		try {
			// Recebe rota
			m_route = (Route) v_cfp.getContentObject();

		} catch (UnreadableException e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public int onEnd() {

		if (isAceitaPropostaVooCandidato()) {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION OK");

			return AircraftAgent.ADMISSION_OK;
		} else {
			m_logger.info(myAgent.getLocalName() + " => ADMISSION NOK");

			return AircraftAgent.ADMISSION_NOK;
		}

	}

	private Boolean isAceitaPropostaVooCandidato() {
		Double preco = Double.valueOf(0D);
		Double v_fltValue = Double.valueOf(0D);
		Boolean propostaAceita = false;

		try {
			CalcFlight.ordenaPorData(m_route.getM_lstFlights());
			
			Flight v_route1stFlt = m_route.getM_lstFlights().get(0);
			
			if (v_route1stFlt.getM_origem().equals(m_acft.getCurrLoc())) {
				propostaAceita = Boolean.TRUE;
				
				preco = ((m_route.getM_SumFuelKG()/1000) 
						* m_acft.getFator() 
						* VALORCOMBUSTIVEL)
						+ m_acft.getPrice();
				v_fltValue = m_route.getM_SumValue();
				v_fltValue -= preco;
				
				
				ds.put(myAgent.getLocalName() + "_PROPOSAL", v_fltValue);
				
			}	
 
		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			v_fltValue = null;
		}

		return propostaAceita;
	}

}
