package org.init.extractor.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.init.extractor.excepciones.ImposibleMapearEventoException;

/**
 * Clase que tiene utilidades para el manejo de países
 * 
 * TODO: Cambiar esto por un extendTemplate o parse
 * 
 * @author fbobbio
 *
 */
public class PaisesUtil {

	private static String loc = "es_AR";
	private static Map<String, String> countries = initCountries();
	private static Map<String, Locale> localeMap = initCountryCodeMapping();
	
	private static Map<String, String> initCountries()
	{
		Map<String, String> countries = new HashMap<String,String>();
	    for (String iso : Locale.getISOCountries()) {
	        Locale l = new Locale(loc, iso);
	        countries.put(l.getDisplayCountry(), iso);
	    }
	    return countries;
	}
	
	private static Map<String, Locale> initCountryCodeMapping() {
		// TODO: arreglar teniendo en cuenta el método initCountries
	    String[] countries = Locale.getISOCountries();
	    Map<String, Locale> map = new HashMap<String, Locale>(countries.length);
	    for (String country : countries) {
	        Locale locale = new Locale(loc, country);
	        map.put(locale.getISO3Country().toUpperCase(), locale);
	    }
	    return map;
	}
	
	/**
	 * Método que convierte el nombre de un país a su código ISO en formato alfa2
	 * 
	 * @param countryName
	 * @return
	 */
	public static String countryNameToIso2Code(String countryName)
	{
		return countries.get(countryName);
	}
	
	/**
	 * Método que convierte el código de la ISO 3166 alfa 3 al código alfa 2 
	 * 
	 * @param iso2CountryCode
	 * @return
	 * @throws ImposibleMapearEventoException 
	 */
	public static String iso3CountryCodeToIso2CountryCode(String iso3CountryCode) throws ImposibleMapearEventoException {
		try
		{
			return localeMap.get(iso3CountryCode).getCountry();
		}
		catch (NullPointerException e)
		{
			throw new ImposibleMapearEventoException("No se pudo parsear el código de país " + iso3CountryCode, e);
		}
	}
	
	/**
	 * Método que convierte el código de la ISO 3166 alfa 2 al código alfa 3 
	 * 
	 * @param iso2CountryCode
	 * @return
	 */
	public static String iso2CountryCodeToIso3CountryCode(String iso2CountryCode){
	    Locale locale = new Locale(loc, iso2CountryCode);
	    return locale.getISO3Country();
	}
	
	/**
	 * Método que convierte el código de la ISO 3166 alfa-2 a nombre del país que corresponde
	 * 
	 * @param iso2CountryCode
	 * @return
	 */
	public static String iso2CountryCodeToCountryName(String iso2CountryCode) {
	    Locale locale = new Locale(loc, iso2CountryCode);
	    return locale.getDisplayCountry();
	}
	
	/**
	 * Método que convierte el código de la ISO 3166 alfa-3 a nombre del país que corresponde
	 * 
	 * @param iso3CountryCode
	 * @return
	 * @throws ImposibleMapearEventoException 
	 */
	public static String iso3CountryCodeToCountryName(String iso3CountryCode) throws ImposibleMapearEventoException {
	    Locale locale = new Locale(loc, iso3CountryCodeToIso2CountryCode(iso3CountryCode));
	    return locale.getDisplayCountry();
	}
}
