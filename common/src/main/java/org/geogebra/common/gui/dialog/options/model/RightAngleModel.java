package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.AngleProperties;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.App;

public class RightAngleModel extends BooleanOptionModel {

	public RightAngleModel(IBooleanOptionListener listener, App app) {
		super(listener, app);
	}

	@Override
	public void updateProperties() {
		AngleProperties geo0 = (AngleProperties) getGeoAt(0);
		getListener().updateCheckbox(geo0.isEmphasizeRightAngle());
	}

	@Override
	public String getTitle() {
		return "EmphasizeRightAngle";
	}

	@Override
	public boolean isValidAt(int index) {
		GeoElement geo = getGeoAt(index);

		return geo instanceof AngleProperties && !geo.isGeoList()
				|| isAngleList(geo);
	}

	@Override
	public boolean getValueAt(int index) {
		// not used here, as updateProperties is overridden.
		return false;
	}

	@Override
	public void apply(int index, boolean value) {
		AngleProperties geo = (AngleProperties) getObjectAt(index);
		geo.setEmphasizeRightAngle(value);
		geo.updateVisualStyle(GProperty.ANGLE_STYLE);
	}

}
