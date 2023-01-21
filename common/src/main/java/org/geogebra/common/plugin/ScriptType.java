package org.geogebra.common.plugin;

import java.util.HashMap;

import org.geogebra.common.main.App;
import org.geogebra.common.plugin.script.GgbScript;
import org.geogebra.common.plugin.script.JsScript;
import org.geogebra.common.plugin.script.Script;

/**
 * @author arno Script classes should get registered here.
 */
public enum ScriptType {
	/**
	 * GgbScript
	 */
	GGBSCRIPT("Script", "ggbscript") {
		@Override
		public Script newScript(App app, String text) {
			return new GgbScript(app, text);
		}
	},
	/**
	 * JsScript
	 */
	JAVASCRIPT("JavaScript", "javascript") {
		@Override
		public Script newScript(App app, String text) {
			return new JsScript(app, text);
		}
	};

	private String name;
	private String xmlName;
	private static HashMap<String, ScriptType> xmlMap = new HashMap<>();

	ScriptType(String name, String xmlName) {
		this.name = name;
		this.xmlName = xmlName;
	}

	/**
	 * Create a new script of this type
	 * 
	 * @param app
	 *            the application where the script lives
	 * @param text
	 *            the source code of the script
	 * @return a new Script object
	 */
	public abstract Script newScript(App app, String text);

	/**
	 * Get the script type's name
	 * 
	 * @return the name of the script type
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the script type XML attribute name
	 * 
	 * @return the XML attribute name
	 */
	public String getXMLName() {
		return xmlName;
	}

	/**
	 * @param eName
	 *            the xml name of the script type
	 * @return the script type with this name or null if none exists
	 */
	public static ScriptType getTypeWithXMLName(String eName) {
		return xmlMap.get(eName);
	}

	static {
		for (ScriptType tp : ScriptType.values()) {
			xmlMap.put(tp.getXMLName(), tp);
		}
	}

}
