package org.geogebra.common.kernel.cas;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoElement;

/**
 * Abstract class for algos that use some CAS algo as helper
 */
public abstract class AlgoUsingTempCASalgo extends AlgoElement
		implements UsesCAS {
	/**
	 * CAS algo helper
	 */
	protected AlgoElement algoCAS;

	/**
	 * @param c
	 *            construction
	 */
	public AlgoUsingTempCASalgo(Construction c) {
		super(c);
	}

	/**
	 * @param c
	 *            construction
	 * @param addToConstructionList
	 *            whether we want this in construction list
	 */
	public AlgoUsingTempCASalgo(Construction c, boolean addToConstructionList) {
		super(c, addToConstructionList);
	}

	@Override
	public void remove() {
		if (removed) {
			return;
		}
		super.remove();
		if (algoCAS != null) {
			algoCAS.remove();
		}
	}

	/**
	 * Creates a temporary CAS algorithm, computes it, stores the results and
	 * deletes the algorithm from the construction
	 */
	public abstract void refreshCASResults();

}
