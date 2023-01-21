package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.AngleProperties;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoAngle;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;

public class DecoAngleModel extends NumberOptionsModel {
	private IDecoAngleListener listener;

	public interface IDecoAngleListener extends IComboListener {
		void setArcSizeMinValue();
	}

	public DecoAngleModel(App app) {
		super(app);
	}

	private AngleProperties getAnglePropertiesAt(int index) {
		return (AngleProperties) getObjectAt(index);
	}

	@Override
	public void updateProperties() {

		AngleProperties geo0 = getAnglePropertiesAt(0);
		listener.setSelectedIndex(geo0.getDecorationType());

	}

	public void setListener(IDecoAngleListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean isValidAt(int index) {
		return (getObjectAt(index) instanceof AngleProperties);
	}

	@Override
	protected void apply(int index, int value) {
		AngleProperties geo = getAnglePropertiesAt(index);
		geo.setDecorationType(value);
		// addded by Loic BEGIN
		// check if decoration could be drawn
		if (geo.getArcSize() < 20 && (geo
				.getDecorationType() == GeoElementND.DECORATION_ANGLE_THREE_ARCS
				|| geo.getDecorationType() == GeoElementND.DECORATION_ANGLE_TWO_ARCS)) {
			geo.setArcSize(20);
			listener.setArcSizeMinValue();
		}
		// END
		geo.updateVisualStyleRepaint(GProperty.DECORATION);
	}

	@Override
	protected int getValueAt(int index) {
		return getAnglePropertiesAt(index).getDecorationType();
	}

	public static int getDecoTypeLength() {
		return GeoAngle.getDecoTypes().length;
	}

	@Override
	public PropertyListener getListener() {
		return listener;
	}

}
