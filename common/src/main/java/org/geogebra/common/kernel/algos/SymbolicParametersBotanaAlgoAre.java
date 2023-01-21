package org.geogebra.common.kernel.algos;

import org.geogebra.common.kernel.prover.NoSymbolicParametersException;
import org.geogebra.common.kernel.prover.polynomial.PPolynomial;

/**
 * This interface describes the symbolic parameters of algorithms for the Botana
 * method, where the algorithm checks a statement (usually in AlgoAre... form).
 * Based on Simon's SymbolicParametersAlgo.java.
 * 
 * @author Zoltan Kovacs
 *
 */
public interface SymbolicParametersBotanaAlgoAre {

	/**
	 * Calculates the sets of polynomials of an object for the Botana method
	 * 
	 * @return arrays of the polynomials
	 * @throws NoSymbolicParametersException
	 *             if it is not possible to obtain suitable polynomials
	 */
	public PPolynomial[][] getBotanaPolynomials()
			throws NoSymbolicParametersException;
}
