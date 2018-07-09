package behaviours;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import agents.TasAgent;
import commons.Aircraft;
import commons.Flight;
import commons.Route;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;

/**
 * @author ivopdm
 *
 */
public class CheckUnassigned extends OneShotBehaviour {

	/**
	 * Verifica as rotas que ainda nao tem aviao alocado.
	 * Em caso de haver voo para ser alocado retorna um inteiro para sinalizar isso, representado por TasAgent.CONTAINS_UNASSIGNED.
	 * Em caso de NAO haver voo para ser alocado retorna um inteiro para sinalizar isso, representado por TasAgent.ALL_ASSIGNED.
	 */
	private static final long serialVersionUID = 1L;
	private Map<Route,Aircraft> m_assignment;
	private int m_trigger;
	private final Logger m_logger = Logger.getMyLogger(getClass().getName());
	
	/** @ConstructorProperties
	 * Inicializa o HashMap<Route,String>
	 * Route: contem informacao da rota como voos, combustivel requerido e valor
	 * String: pode ser unassigned ou nome do aviao que vai fazer aquele voo
	 *
	 * @param p_assignment
	 */
	
	public CheckUnassigned(HashMap<Route,Aircraft> p_assignment){
		this.m_assignment = p_assignment;		
	}
	
	@Override
	public void action() {
		
		for (Entry<Route, Aircraft> entry : m_assignment.entrySet()) {
			m_logger.info(entry.getKey().getM_id() + " => " + entry.getValue());			
		}
		
		//if(m_assignment.containsValue(TasAgent.FLIGHT_UNASSIGNED)){
		if(m_assignment.containsValue(null)){
			m_trigger = TasAgent.CONTAINS_UNASSIGNED;
			m_logger.info("CONTAINS_UNASSIGNED");
		}else{
			m_trigger = TasAgent.ALL_ASSIGNED;
			m_logger.info("ALL_ASSIGNED");
		}
			
	}

	@Override
	public int onEnd() {
		return m_trigger;
	}
	
	
}
