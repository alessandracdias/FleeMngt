package ontology;

import jade.content.onto.*;
import jade.content.schema.*;

public class IVHMOntology extends Ontology{
	//The name identifying this ontology
	public static final String ONTOLOGY_NAME = "IVHM-Ontology";

	//VOCABULARY
	public static final String AFTER_MISSION_RUL = "After Mission RUL";
	public static final String VEHICLEID = "VehicleID";
	public static final String RUL = "RUL";

	//The singleton instance of this ontology
	private static Ontology theInstance = new IVHMOntology();

	// Retrieve the singleton IVHM Ontology
	public static Ontology getInstance(){
		return theInstance;
	}

	// Private constructor
	private IVHMOntology(){
		// The IVHM ontology extends the basic ontology
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new PredicateSchema(AFTER_MISSION_RUL), AfterMissionRUL.class);

			//Structure of the schema for the AfterMissionRUL concept
			PredicateSchema cs = (PredicateSchema) getSchema(AFTER_MISSION_RUL);
			cs.add(VEHICLEID, 
					(PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(RUL, 
					(PrimitiveSchema) getSchema(BasicOntology.FLOAT));
			
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}


	}


}
