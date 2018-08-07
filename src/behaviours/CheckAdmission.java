package behaviours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import agents.AircraftAgent;
import commons.Aircraft;
import commons.Airport;
import commons.Flight;
import commons.MaintenanceStatus;
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
	 * Behaviour/acao responsavel por validar se a instancia de AircraftAgent deve
	 * participar da negociacao/competicao para realizar o voo
	 */
	private static final long serialVersionUID = 8628498715010608266L;
	private Route m_route;
	private Aircraft m_acft;
	private final Double VALORCOMBUSTIVEL = Double.valueOf(531D);
	private DataStore ds;

	private List<Flight> m_listaRotaProposta = new ArrayList<Flight>();
	private Proposal proposal = new Proposal();
	private static final String AIRPORTS = "Airports";
	private HashMap<String, Airport> m_airports = new HashMap<String, Airport>();
	private static final String OVERNIGHT = "OVERNIGHT";
	private static final String DOWNTIME = "DOWNTIME";
	private static final String MAINTENANCE_PENALTY = "MAINTENANCE_PENALTY";
	private Double maintenance_penalty = 0.0;
	private Integer downtime = 0;
	private Integer overnight = 0;

	private final Logger m_logger = Logger.getMyLogger(getClass().getName());

	@Override
	public void action() {
		ds = getDataStore();
		ACLMessage v_cfp = (ACLMessage) ds.get("CFP");
		m_acft = (Aircraft) ds.get(myAgent.getLocalName());
		m_airports = (HashMap<String, Airport>) ds.get(AIRPORTS);
		maintenance_penalty = (Double) ds.get(MAINTENANCE_PENALTY);
		downtime = (Integer) ds.get(DOWNTIME);
		overnight = (Integer) ds.get(OVERNIGHT);

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
		Boolean firs_test = false;
		Boolean second_test = true;
		Boolean proposta_aceita = false;

		try {
			CalcFlight.ordenaPorData(m_route.getM_lstFlights());

			List<Flight> flights = m_route.getM_lstFlights();
			Flight v_route1stFlt = flights.get(0);

			if (v_route1stFlt.getM_origem().equals(m_acft.getCurrLoc())) {
				firs_test = true;

				if (m_acft.getMaintenanceStatus().equals(MaintenanceStatus.T1)) {
					preco = ((m_route.getM_SumFuelKG() / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL)
							+ m_acft.getPrice() + maintenance_penalty;
				} else {
					preco = ((m_route.getM_SumFuelKG() / 1000) * m_acft.getFator() * VALORCOMBUSTIVEL)
							+ m_acft.getPrice();
				}
				v_fltValue = m_route.getM_SumValue();
				v_fltValue -= preco;
				ds.put(myAgent.getLocalName() + "_PROPOSAL", v_fltValue);
			}

			if (m_acft.getMaintenanceStatus().equals(MaintenanceStatus.T2)) {
				String v_last_airport = flights.get(flights.size() - 1).getM_destino();
				if (m_airports.containsKey(v_last_airport)) {
					if (m_airports.get(v_last_airport).isMaintenence_base())
						second_test = true;
					else
						second_test = false;
				} else {
					m_logger.warning(myAgent.getLocalName() + "O Aeroporto nao existe!");
					second_test = false;
				}

				if (overnight == 0 && second_test == false) {

					for ( int i=0; i< flights.size(); i++)
					{
						String v_airport = flights.get(i).getM_destino();
						if (m_airports.get(v_airport).isMaintenence_base()) {
							Long departure = flights.get(i+1).getM_dataEtd().getTime();
							Long arrived = flights.get(i).getM_dataEta().getTime();
							int diffMin = (int) ((departure - arrived) / (60 * 1000));
							if (downtime <= diffMin) {
								second_test = true;
								m_logger.info(myAgent.getLocalName() + " => ADMISSION OK" + v_airport);
								break;
							} else {
								second_test = false;
							}
						}
						else
						{
							second_test = false;
						}
					}
					

				}

			}

		} catch (Exception e) {
			m_logger.warning(myAgent.getLocalName() + e.getMessage());
		} finally {
			preco = null;
			v_fltValue = null;
		}

		if (firs_test == true && second_test == true)
			proposta_aceita = true;

		return proposta_aceita;
	}

}
