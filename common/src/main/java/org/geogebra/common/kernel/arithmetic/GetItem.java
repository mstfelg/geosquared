package org.geogebra.common.kernel.arithmetic;

/**
 * Common interface for lists and commands so that CAS handlers can work with
 * Midpoint[a,b] and logb(3,5) the same way
 * 
 * @author zbynek
 *
 */
public interface GetItem extends ExpressionValue {
	/**
	 * 
	 * @param i
	 *            index
	 * @return argument of command / item of list at given position
	 */
	public ExpressionValue getItem(int i);

	/**
	 * needed to distinguish eg igamma with 2 or 3 args
	 * 
	 * @return length
	 */
	public int getLength();

	/**
	 * @return label of expected result
	 */
	public String getLabel();
}
