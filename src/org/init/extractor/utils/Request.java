package org.init.extractor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Clase que se encarga de los request http que sean necesarios
 * 
 * @author fbobbio
 *
 */
public class Request {


	/**
	 * Método que realiza el request HTTP que necesitemos según la cadena objetivo
	 * 
	 * @return string con la devolución de la consulta realizada
	 */
	public static String requestGeneral(String cadena) {
		URL url = null;
		try {
			url = new URL(cadena);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection connection = null;
		StringBuilder sb = null;
		String line = null;

		// Realizo la conexión y pido el retorno
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		return sb.toString();
	}

}
