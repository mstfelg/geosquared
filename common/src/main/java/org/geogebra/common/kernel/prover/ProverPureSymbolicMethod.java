package org.geogebra.common.kernel.prover;

import org.geogebra.common.kernel.algos.SymbolicParameters;
import org.geogebra.common.kernel.algos.SymbolicParametersAlgo;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.prover.polynomial.PPolynomial;
import org.geogebra.common.util.Prover;
import org.geogebra.common.util.Prover.ProofResult;
import org.geogebra.common.util.debug.Log;

/**
 * A prover which uses pure symbolic method to prove geometric theorems.
 * 
 * @author Simon Weitzhofer
 * @author Zoltan Kovacs
 *
 */
public class ProverPureSymbolicMethod {

	/**
	 * Proves the statement by using pure symbolic method
	 * 
	 * @param prover
	 *            the prover to be used
	 * @return if the proof was successful
	 */
	public static ProofResult prove(Prover prover) {

		GeoElement statement = prover.getStatement();

		if (statement instanceof SymbolicParametersAlgo) {
			SymbolicParametersAlgo statementSymbolic = (SymbolicParametersAlgo) statement;
			SymbolicParameters parameters = statementSymbolic
					.getSymbolicParameters();
			try {
				parameters.getFreeVariables();
				// TODO: write here Recio's prover
				// FIXME: No, something else is required here!
			} catch (NoSymbolicParametersException e) {
				return ProofResult.UNKNOWN;
			}
		} else if (statement
				.getParentAlgorithm() instanceof SymbolicParametersAlgo) {
			SymbolicParametersAlgo statementSymbolic = (SymbolicParametersAlgo) statement
					.getParentAlgorithm();
			try {
				PPolynomial[] poly = statementSymbolic.getPolynomials();
				for (PPolynomial polynomial : poly) {
					Log.debug(polynomial);
					if (!polynomial.isZero()) {
						return ProofResult.FALSE;
					}
				}
				return ProofResult.TRUE;
				// TODO: write here Recio's prover
				// FIXME: No, something else is required here!
			} catch (NoSymbolicParametersException e) {
				return ProofResult.UNKNOWN;
			}
		}
		return ProofResult.UNKNOWN;
	}
}
