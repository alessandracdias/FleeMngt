package agents;

import java.util.HashMap;
import java.util.List;

import behaviours.CheckAdmission;
import behaviours.CheckCFP;
import behaviours.CheckHealth;
import behaviours.SendPropose;
import behaviours.SendRefuse;
import behaviours.UpdPrice;
import commons.Aircraft;
import commons.Airport;
import commons.Route;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.util.Logger;

public class AircraftAgent extends Agent {
	/**
	 * Aircraft Agent coordena acoes do aviao 
	 */
	private static final long serialVersionUID = 1634277483475631766L;
	private FSMBehaviour m_fsm;
	private final Logger logger = Logger.getMyLogger(getClass().getName());

	/**
	 * States: constantes que guardam o nome dos estados.
	 */
	public static final String CHECK_CFP = "Check_CFP";
	public static final String CHECK_ADMISSION = "Check_Admission";
	public static final String SEND_PROPOSE = "Send_Propose";
	public static final String SEND_REFUSE = "Send_Refuse";
	private static final String UPD_PRICE = "Update_Price";
	private static final String AIRPORTS = "Airports";


	private HashMap<String, Airport> m_airports = new HashMap<String, Airport>();

	/**
	 * Triggers: constantes que guardam numeros (sinais) que sao utilizados para
	 * habilitar transicao entre estados.
	 */
	public static final int ADMISSION_OK = 1;
	public static final int ADMISSION_NOK = 2;

	@Override
	protected void setup() {
		super.setup();

		logger.info("Starting up " + getLocalName());
		/**
		 * Behaviour executa uma maquina de estados cada estado e um behaviour as
		 * transicoes sao definidas abaixo, juntamente com os estados.
		 */

		m_fsm = new FSMBehaviour();

		// Recebe argumento passado na classe app.Main
		/**
		 * TODO Passar a localizacao atual do aviao Informacao do valor dos voos
		 * 
		 */
		//Atualizacao: passa os dados dos aeroportos para o agente
		Object[] args = new Object[2];
		args = this.getArguments();
		if (args.length > 0) {
			m_fsm.getDataStore().put(getLocalName(), (Aircraft) args[0]);
			m_airports = (HashMap<String, Airport>) args[1];
			m_fsm.getDataStore().put(AIRPORTS, m_airports);
		}

		/**
		 * REGISTER STATES: cria estados e guarda na maquina de estados. Cada estado e
		 * uma classe que estende a classe behaviour/oneshotbehaviour/simplebehaviour...
		 */
		Behaviour checkCfp = new CheckCFP();
		checkCfp.setDataStore(m_fsm.getDataStore());
		m_fsm.registerFirstState(checkCfp, CHECK_CFP);

		Behaviour checkAdmission = new CheckAdmission();
		checkAdmission.setDataStore(m_fsm.getDataStore());
		m_fsm.registerState(checkAdmission, CHECK_ADMISSION);

		Behaviour sendPropose = new SendPropose();
		sendPropose.setDataStore(m_fsm.getDataStore());
		m_fsm.registerState(sendPropose, SEND_PROPOSE);

		Behaviour sendRefuse = new SendRefuse();
		sendRefuse.setDataStore(m_fsm.getDataStore());
		m_fsm.registerState(sendRefuse, SEND_REFUSE);

		Behaviour updPrice = new UpdPrice();
		updPrice.setDataStore(m_fsm.getDataStore());
		m_fsm.registerState(updPrice, UPD_PRICE);

		// REGISTER TRANSITION
		m_fsm.registerDefaultTransition(CHECK_CFP, CHECK_ADMISSION);
		m_fsm.registerTransition(CHECK_ADMISSION, SEND_PROPOSE, ADMISSION_OK);
		m_fsm.registerTransition(CHECK_ADMISSION, SEND_REFUSE, ADMISSION_NOK);
		m_fsm.registerDefaultTransition(SEND_PROPOSE, UPD_PRICE);
		m_fsm.registerDefaultTransition(UPD_PRICE, CHECK_CFP);
		m_fsm.registerDefaultTransition(SEND_REFUSE, CHECK_CFP);

		addBehaviour(m_fsm);

//		Behaviour checkHealth = new CheckHealth();
//		checkHealth.setDataStore(m_fsm.getDataStore());
//		addBehaviour(checkHealth);

	}

	@Override
	protected void takeDown() {
		super.takeDown();
	}

}
