package com.himamis.retex.editor.share.editor;

/**
 * Syntax aware adapter for the editor
 * @author michael
 */
public interface SyntaxAdapter {

	/**
	 * @param exp
	 *            expression in ggb, LaTeX or Presentation MathML format
	 * @return expression converted into editor syntax if possible
	 */
	String convert(String exp);

	boolean isFunction(String casName);
}
