package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.App;

public class ShowObjectModel extends BooleanOptionModel {
	public interface IShowObjectListener extends IBooleanOptionListener {
		void updateCheckbox(boolean value, boolean isEnabled);

	}

	public ShowObjectModel(IShowObjectListener listener, App app) {
		super(listener, app);
	}

	@Override
	public void updateProperties() {
		// check if properties have same values
		GeoElement temp, geo0 = getGeoAt(0);
		boolean equalObjectVal = true;
		boolean showObjectCondition = geo0.getShowObjectCondition() != null;

		for (int i = 1; i < getGeosLength(); i++) {
			temp = getGeoAt(i);
			// same object visible value
			if (geo0.isSetEuclidianVisible() != temp.isSetEuclidianVisible()) {
				equalObjectVal = false;
				break;
			}

			if (temp.getShowObjectCondition() != null) {
				showObjectCondition = true;
			}
		}

		((IShowObjectListener) getListener()).updateCheckbox(equalObjectVal
				&& geo0.isSetEuclidianVisible(),
				!showObjectCondition);

	}

	@Override
	public String getTitle() {
		return "ShowObject";
	}

	@Override
	public boolean isValidAt(int index) {
		// TODO Auto-generated method stub
		boolean isValid = true;
		GeoElement geo = getGeoAt(index);
		if (!geo.isDrawable()
				// can't allow a free fixed number to become visible (as a
				// slider)
				|| (geo.isGeoNumeric() && geo.isLocked())) {
			isValid = false;

		}

		return isValid;
	}

	@Override
	public boolean getValueAt(int index) {
		// not used
		return false;
	}

	@Override
	public void apply(int index, boolean value) {
		GeoElement geo = getGeoAt(index);
		geo.setEuclidianVisible(value);
		geo.updateVisualStyleRepaint(GProperty.VISIBLE);
		storeUndoInfo();
	}

}