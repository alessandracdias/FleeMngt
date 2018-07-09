package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import commons.Aircraft;
import commons.Flight;

public class CalcFlight {

	/**
	 * @param ETD
	 * @param ETA
	 * @return
	 */
	public static boolean isMaiorTAT(Date ETD, Date ETA) {
		return Data.calculaDiasDiferencaEntreDatas(ETD, ETA) > 40;
	}

	/**
	 * @param listaFlight
	 */
	public static void ordenaPorData(List<Flight> listaFlight) {
		Collections.sort(listaFlight, new Comparator<Flight>() {
			@Override
			public int compare(Flight f1, Flight f2) {
				return f1.getM_dataEtd().compareTo(f2.getM_dataEtd());
			}
		});
	}

	/**
	 * Oredena a lista de aviões por id
	 * 
	 * @param lista
	 */
	public static void ordenaAircraftPorID(List<Aircraft> lista) {
		Collections.sort(lista, new Comparator<Aircraft>() {
			@Override
			public int compare(Aircraft o1, Aircraft o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
	}

	/**
	 * Oredena a lista de voos por id
	 * 
	 * @param lista
	 */
	public static void ordenaPorFlightID(List<Flight> lista) {
		Collections.sort(lista, new Comparator<Flight>() {
			@Override
			public int compare(Flight o1, Flight o2) {
				return o1.getM_FlightID().compareTo(o2.getM_FlightID());
			}
		});
	}

	/**
	 * Oredena a lista de voos pelos aviaões.
	 * 
	 * @param lista
	 */
	public static void ordenaPorFlightAircraft(List<Flight> lista) {
		Collections.sort(lista, new Comparator<Flight>() {
			@Override
			public int compare(Flight o1, Flight o2) {
				return o1.getM_Aircraft().compareTo(o2.getM_Aircraft());
			}
		});
	}

}
