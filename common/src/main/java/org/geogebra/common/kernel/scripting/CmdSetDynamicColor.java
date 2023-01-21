package org.geogebra.common.kernel.scripting;

import java.util.ArrayList;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoDependentList;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CmdScripting;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.main.MyError;

/**
 * SetDynamicColor
 */
public class CmdSetDynamicColor extends CmdScripting {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdSetDynamicColor(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected final GeoElement[] perform(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg2;
		switch (n) {
		case 4:
			boolean[] ok = new boolean[n];
			arg2 = resArgs(c);

			if ((ok[1] = arg2[1] instanceof GeoNumberValue)
					&& (ok[2] = arg2[2] instanceof GeoNumberValue)
					&& (ok[3] = arg2[3] instanceof GeoNumberValue)) {
				GeoElement geo = arg2[0];

				ArrayList<GeoElement> listItems = new ArrayList<>();
				listItems.add(arg2[1]);
				listItems.add(arg2[2]);
				listItems.add(arg2[3]);
				// listItems.add((GeoElement) arg2[4]); // no opacity
				AlgoDependentList algo = new AlgoDependentList(cons, listItems,
						false);
				kernel.getConstruction().removeFromConstructionList(algo);
				GeoList list = algo.getGeoList();

				geo.setColorFunction(list);
				geo.updateVisualStyleRepaint(GProperty.COLOR);

				return new GeoElement[0];

			} else if (!ok[1]) {
				throw argErr(c, arg2[1]);
			} else if (!ok[2]) {
				throw argErr(c, arg2[2]);
			} else {
				throw argErr(c, arg2[3]);
			}
		case 5:
			ok = new boolean[n];
			arg2 = resArgs(c);
			if ((ok[1] = arg2[1] instanceof GeoNumberValue)
					&& (ok[2] = arg2[2] instanceof GeoNumberValue)
					&& (ok[3] = arg2[3] instanceof GeoNumberValue)
					&& (ok[4] = arg2[4] instanceof GeoNumberValue)) {

				GeoElement geo = arg2[0];

				ArrayList<GeoElement> listItems = new ArrayList<>();
				listItems.add(arg2[1]);
				listItems.add(arg2[2]);
				listItems.add(arg2[3]);
				listItems.add(arg2[4]); // opacity
				AlgoDependentList algo = new AlgoDependentList(cons, listItems,
						false);
				kernel.getConstruction().removeFromConstructionList(algo);
				GeoList list = algo.getGeoList();

				geo.setColorFunction(list);
				geo.updateRepaint();

				return new GeoElement[0];

			} else if (!ok[1]) {
				throw argErr(c, arg2[1]);
			} else if (!ok[2]) {
				throw argErr(c, arg2[2]);
			} else if (!ok[3]) {
				throw argErr(c, arg2[3]);
			} else {
				throw argErr(c, arg2[4]);
			}

		default:
			throw argNumErr(c);
		}
	}
}
