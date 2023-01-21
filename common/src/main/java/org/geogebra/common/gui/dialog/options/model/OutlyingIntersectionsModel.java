package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.LimitedPath;
import org.geogebra.common.main.App;

public class OutlyingIntersectionsModel extends BooleanOptionModel {

	public OutlyingIntersectionsModel(IBooleanOptionListener listener,
			App app) {
		super(listener, app);
	}

	private LimitedPath getLimitedPathAt(int index) {
		return (LimitedPath) getObjectAt(index);
	}

	@Override
	public boolean isValidAt(int index) {
		return (getObjectAt(index) instanceof LimitedPath);
	}

	@Override
	public boolean getValueAt(int index) {
		return getLimitedPathAt(index).allowOutlyingIntersections();
	}

	@Override
	public void apply(int index, boolean value) {
		LimitedPath geo = getLimitedPathAt(index);
		geo.setAllowOutlyingIntersections(value);
		geo.toGeoElement().updateRepaint();
	}

	@Override
	public String getTitle() {
		return "allowOutlyingIntersections";
	}
}
