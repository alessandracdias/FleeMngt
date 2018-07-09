package commons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jade.util.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import util.CalcFlight;
import util.Data;

public class CarregarDadosExcel {

	private final Logger logger = Logger.getMyLogger(getClass().getName());

	/**
	 * Metodo usado para montar a lista de avioes.
	 * 
	 * @param nomeTalela
	 * @return
	 */
	public List<Aircraft> montarListaAvioes(String nomeTalela) {
		List<Aircraft> listaAvioes = new java.util.ArrayList<Aircraft>();
		String v_caminho = "./resources/data/" + nomeTalela;
		// objeto relativo ao arquivo excel
		Workbook workbook = null;
		try {
			// Carrega planilha
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");// configura acentua��o
			// recupera arquivo desejado
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			// recupera pagina/planilha/aba do arquivo
			Sheet sheet = workbook.getSheet(0);
			// recupera numero de linhas
			int linhas = sheet.getRows();
			// percorre todas as linhas da planilha
			String compara = "0";
			// Random gerador = new Random();			
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					// verifica se coluna 0 (A) e linha row nao e vazia
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						if (!String.valueOf(sheet.getCell(7, row).getContents()).equals(compara)) {
							Aircraft aviao = new Aircraft();
							// recupera informacao da coluna A linha row.							
							aviao.setId(sheet.getCell(7, row).getContents().toString());
							compara = String.valueOf(sheet.getCell(7, row).getContents());
							if (!sheet.getCell(6, row).getContents().isEmpty()) {
								// recupera informacao da coluna B linha
								// row.
								aviao.setNome(sheet.getCell(6, row).getContents().toString());
							}
							// Double valor = Math.random() + 1;
							// valor = Double.valueOf(String.format(Locale.US,
							// "%.2f", valor));
							Double valor = (0.05 * Math.random()) + 1;
							valor = Double.valueOf(String.format(Locale.US, "%.4f", valor));
							aviao.setFator(valor);
							aviao.setCurrLoc(sheet.getCell(2, row).getContents().toString());
							aviao.setRoute(new ArrayList<Flight>());
							listaAvioes.add(aviao);
						}
					}
				}

			}

		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			// fechar
			if (workbook != null)
				workbook.close();
		}
		CalcFlight.ordenaAircraftPorID(listaAvioes);
		return listaAvioes;
	}

	/**
	 * Metodo usado para montar a lista de voos.
	 * 
	 * @param nomeTalela
	 * @return
	 */
	public List<Flight> montarListaFlights(String nomeTalela) {
		List<Flight> listaFlights = new java.util.ArrayList<Flight>();
		String v_caminho = "./resources/data/" + nomeTalela;
		// objeto relativo ao arquivo excel
		Workbook workbook = null;
		try {
			// Carrega planilha
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");// configura acentua��o
			// recupera arquivo desejado
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			// recupera pagina/planilha/aba do arquivo
			Sheet sheet = workbook.getSheet(0);
			// recupera numero de linhas
			int linhas = sheet.getRows();
			// percorre todas as linhas da planilha
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					Flight flight = new Flight();
					if (!sheet.getCell(1, row).getContents().isEmpty()) {
						flight.setM_FlightID(sheet.getCell(1, row).getContents().toString());
						//flight.setM_FlightID(String.valueOf(row + 1));
					}
					if (!sheet.getCell(2, row).getContents().isEmpty()) {
						flight.setM_origem(sheet.getCell(2, row).getContents().toString());
					}
					if (!sheet.getCell(3, row).getContents().isEmpty()) {
						flight.setM_destino(sheet.getCell(3, row).getContents().toString());
					}
					if (!sheet.getCell(4, row).getContents().isEmpty()) {
						flight.setM_dataEtd(
								Data.toDate(sheet.getCell(4, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					if (!sheet.getCell(5, row).getContents().isEmpty()) {
						flight.setM_dataEta(
								Data.toDate(sheet.getCell(5, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
					valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
					flight.setM_fuelKG(valorFuel);

					Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
					flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
					flight.setM_flightValue(flightValue);

					listaFlights.add(flight);
				}
			}
		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			// fechar
			if (workbook != null)
				workbook.close();
		}
		return listaFlights;
	}

	/**
	 * m�todo n�o usado mais.
	 * 
	 * @param nomeTalela
	 * @return
	 */
	public List<Route> montarListaRouteOLD(String nomeTalela) {
		List<Route> listaRotas = new java.util.ArrayList<Route>();
		String v_caminho = "./resources/data/" + nomeTalela;
		Workbook workbook = null;
		try {
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			Sheet sheet = workbook.getSheet(0);
			int linhas = sheet.getRows();

			Integer comparaRota = 0;
			Integer id = 1;
			Route route = new Route();

			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						if (Integer.valueOf(sheet.getCell(7, row).getContents()).equals(comparaRota)) {
							if (row != 1) {
								listaRotas.add(route);
							}
							route = new Route();
							route.setM_id(Long.valueOf(id));
							route.setM_lstFlights(new ArrayList<Flight>());
							id++;
						}
						comparaRota = Integer.valueOf(sheet.getCell(7, row).getContents());
						// monta o voo
						Flight flight = new Flight();
						if (!sheet.getCell(1, row).getContents().isEmpty()) {
							flight.setM_FlightID(sheet.getCell(1, row).getContents().toString());
						}
						if (!sheet.getCell(2, row).getContents().isEmpty()) {
							flight.setM_origem(sheet.getCell(2, row).getContents().toString());
						}
						if (!sheet.getCell(3, row).getContents().isEmpty()) {
							flight.setM_destino(sheet.getCell(3, row).getContents().toString());
						}
						if (!sheet.getCell(4, row).getContents().isEmpty()) {
							flight.setM_dataEtd(
									Data.toDate(sheet.getCell(4, row).getContents().toString(), Data.DATA_HORA_PADRAO));
						}
						if (!sheet.getCell(5, row).getContents().isEmpty()) {
							flight.setM_dataEta(
									Data.toDate(sheet.getCell(5, row).getContents().toString(), Data.DATA_HORA_PADRAO));
						}
						Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
						valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
						flight.setM_fuelKG(valorFuel);
						//route.setM_SumFuelKG(route.getM_SumFuelKG() + valorFuel);

						Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
						flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
						flight.setM_flightValue(flightValue);

						//route.setM_SumValue(route.getM_SumValue() + flightValue);

						route.getM_lstFlights().add(flight);
						if (linhas - 1 == row) {
							listaRotas.add(route);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			if (workbook != null)
				workbook.close();
		}
		return listaRotas;
	}
	

	/**
	 * m�todo usado para ordenar a lista de voos pelos avi�es.
	 * 
	 * @param nomeTalela
	 * @return
	 */
	public List<Flight> montarListaFlightsOrdenadaPorAircraft(String nomeTalela) {
		List<Flight> listaFlights = new java.util.ArrayList<Flight>();
		String v_caminho = "./resources/data/" + nomeTalela;
		Workbook workbook = null;
		try {
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			Sheet sheet = workbook.getSheet(0);
			int linhas = sheet.getRows();
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					Flight flight = new Flight();
					if (!sheet.getCell(1, row).getContents().isEmpty()) {						
						flight.setM_FlightID(sheet.getCell(1, row).getContents().toString());
						//flight.setM_FlightID(String.valueOf(row + 1));
					}
					if (!sheet.getCell(2, row).getContents().isEmpty()) {
						flight.setM_origem(sheet.getCell(2, row).getContents().toString());
					}
					if (!sheet.getCell(3, row).getContents().isEmpty()) {
						flight.setM_destino(sheet.getCell(3, row).getContents().toString());
					}
					if (!sheet.getCell(4, row).getContents().isEmpty()) {
						flight.setM_dataEtd(
								Data.toDate(sheet.getCell(4, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					if (!sheet.getCell(5, row).getContents().isEmpty()) {
						flight.setM_dataEta(
								Data.toDate(sheet.getCell(5, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
					valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
					flight.setM_fuelKG(valorFuel);

					Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
					flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
					flight.setM_flightValue(flightValue);

					// monta a variavel auxiliar
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						flight.setM_Aircraft(sheet.getCell(7, row).getContents().toString());
						listaFlights.add(flight);
					}
				}
			}
		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			// fechar
			if (workbook != null)
				workbook.close();
		}
		CalcFlight.ordenaPorFlightAircraft(listaFlights);
		return listaFlights;
	}

	/**
	 * m�todo usado para montar a lista de rotas orneda pelos avi�es.
	 * 
	 * @param nomeTalela
	 * @return
	 */
	public List<Route> montarListaRouteNovo(String nomeTalela) {
		List<Flight> listaVoos = new ArrayList<Flight>();
		List<Route> listaRotas = new ArrayList<Route>();
		Integer id = 1;
		String comparaRota = "0";
		Route route = new Route();
		try {
			listaVoos = montarListaFlightsOrdenadaPorAircraft(nomeTalela);
			if (!listaVoos.isEmpty()) {
				for (int i = 0; i < listaVoos.size(); i++) {
					System.out.println(listaVoos.get(i).getM_Aircraft());
					if (!listaVoos.get(i).getM_Aircraft().equals(comparaRota)) {
						if (i != 0) {
							listaRotas.add(route);
						}
						route = new Route();
						route.setM_id(Long.valueOf(id));
						route.setM_lstFlights(new ArrayList<Flight>());
						id++;
					}
					comparaRota = listaVoos.get(i).getM_Aircraft();
					Flight flight = new Flight();
					if (!listaVoos.get(i).getM_FlightID().isEmpty()) {
						flight.setM_FlightID(listaVoos.get(i).getM_FlightID());
					}
					if (!listaVoos.get(i).getM_origem().isEmpty()) {
						flight.setM_origem(listaVoos.get(i).getM_origem());
					}
					if (!listaVoos.get(i).getM_destino().isEmpty()) {
						flight.setM_destino(listaVoos.get(i).getM_destino());
					}
					if (listaVoos.get(i).getM_dataEtd() != null) {
						flight.setM_dataEtd(listaVoos.get(i).getM_dataEtd());
					}
					if (listaVoos.get(i).getM_dataEta() != null) {
						flight.setM_dataEta(listaVoos.get(i).getM_dataEta());
					}
					Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
					valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
					flight.setM_fuelKG(valorFuel);
					//route.setM_SumFuelKG(route.getM_SumFuelKG() + valorFuel);

					Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
					flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
					flight.setM_flightValue(flightValue);
					//route.setM_SumValue(route.getM_SumValue() + flightValue);

					route.getM_lstFlights().add(flight);

					if (i + 1 == listaVoos.size()) {
						listaRotas.add(route);
					}
				}
			}
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			listaVoos = null;
			id = null;
			comparaRota = null;
			route = null;
		}
		return listaRotas;
	}

}
