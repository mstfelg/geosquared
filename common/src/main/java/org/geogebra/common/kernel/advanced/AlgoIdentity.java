package org.geogebra.common.kernel.advanced;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoNumeric;

/**
 * Algo for Identity[n]
 * 
 * @author zbynek
 *
 */
public class AlgoIdentity extends AlgoElement {
	private GeoNumberValue n;
	private GeoList result;

	/**
	 * @param c
	 *            construction
	 * @param label
	 *            label for output
	 * @param n
	 *            size of the matrix
	 */
	public AlgoIdentity(Construction c, String label, GeoNumberValue n) {
		super(c);
		this.n = n;
		result = new GeoList(c);
		setInputOutput();
		compute();
		result.setLabel(label);
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[] { n.toGeoElement() };
		setOnlyOutput(result);
		setDependencies();
	}

	@Override
	public void compute() {
		result.clear();
		int size = (int) n.getDouble();
		if (size <= 0) {
			result.setUndefined();
			return;
		}
		for (int i = 0; i < size; i++) {
			GeoList toAdd = new GeoList(cons);
			for (int j = 0; j < size; j++) {
				toAdd.add(new GeoNumeric(cons, i == j ? 1 : 0));
			}
			result.add(toAdd);
		}

	}

	/**
	 * @return resulting identity matrix
	 */
	public GeoList getResult() {
		return result;
	}

	@Override
	public Commands getClassName() {
		return Commands.Identity;
	}

}
