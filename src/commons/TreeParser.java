package commons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class TreeParser {

	public static Double evalTree() {

		Properties prop = new Properties();
		String file_tree = "";
		String file_tree_value = "";
		HashMap<String, Double> values = new HashMap<String, Double>();
		String[] cut_sets;
		Double s_rul = 0.0;
		double cut_set_partial = 0.0;
		double cut_set_final = 1.0;

		try {
			prop.load(new FileInputStream("config.properties"));
			file_tree = prop.getProperty("tree.data");
			file_tree_value = prop.getProperty("tree.value");

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Read the prob values of components
		try (BufferedReader br = new BufferedReader(new FileReader(file_tree_value))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = line.replaceAll("\\s+", "");
				String[] data = line.split("=");
				values.put(data[0], Double.parseDouble(data[1]));
				line = br.readLine();

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file_tree))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();

			}
			String everything = sb.toString();
			everything = everything.replaceAll("\\s+", "");
			cut_sets = everything.split("\\|");

			
			for (String cut_set : cut_sets) {
				cut_set = cut_set.replace("(", "");
				cut_set = cut_set.replace(")", "");
				double mult = 0.0;

				String aux[] = cut_set.split("&");
				mult = values.get(aux[0]) / 100.0;
				for (int i = 1; i < aux.length; i++) {
					try {
						mult = mult * (values.get(aux[i]) / 100.0);
					}catch (Exception e) {
						System.out.println("Nao encontrou componente");
					}					
				}
				cut_set_partial = 1.0 - mult;
				cut_set_final = cut_set_final * cut_set_partial;			}
			
			s_rul = (1 - cut_set_final)*100;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s_rul;
	}

}
