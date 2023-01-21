package org.geogebra.common.move.ggtapi.models.json;

/**
 * @author gabor
 * 
 *         Maybe we need some methods concerning JSON
 *
 */
public class JsonUtils {

	/**
	 * @param value
	 *            to escape
	 * @return escaped value
	 */
	public static String escapeValue(String value) {

		return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

}
