package org.geogebra.common.geogebra3D.euclidian3D.draw;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.ManagerShaders;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Textures;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPlane3D;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.plugin.EuclidianStyleConstants;

/**
 * Class for drawing 3D constant planes.
 * 
 * @author matthieu
 *
 */
public class DrawPlaneConstant3D extends DrawPlane3D {

	private DrawAxis3D xAxis;
	private DrawAxis3D yAxis;

	/**
	 * Common constructor
	 * 
	 * @param a_view3D
	 *            view
	 * @param a_plane3D
	 *            plane
	 * @param xAxis
	 *            x axis
	 * @param yAxis
	 *            y axis
	 */
	public DrawPlaneConstant3D(EuclidianView3D a_view3D, GeoPlane3D a_plane3D,
			DrawAxis3D xAxis, DrawAxis3D yAxis) {

		super(a_view3D, a_plane3D);

		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}

	@Override
	protected boolean updateForItSelf() {

		double[] xMinMax = xAxis.getDrawMinMax();
		double[] yMinMax = yAxis.getDrawMinMax();

		GeoPlane3D geo = (GeoPlane3D) getGeoElement();

		geo.setGridCorners(xMinMax[0], yMinMax[0], xMinMax[1], yMinMax[1]);

		geo.setGridDistances(getView3D().getGridDistances(0),
				getView3D().getGridDistances(1));

		if (getView3D().getShowPlane() || getView3D().getShowGrid()) {
			super.updateGeometry();
		}

		updateGeometriesVisibility();

		return true;
	}

	@Override
	protected void updateForView() {
		if (getView3D().viewChangedByRotate()) {
			boolean oldViewDirectionIsParallel = viewDirectionIsParallel;
			checkViewDirectionIsParallel();
			if (oldViewDirectionIsParallel != viewDirectionIsParallel) {
				// maybe have to update the outline
				setWaitForUpdate();
			}
		}
	}

	@Override
	protected void setMinMax() {
		// follow axes values
	}

	@Override
	protected boolean isGridVisible() {
		return ((GeoPlane3D) getGeoElement()).isGridVisible();
	}

	@Override
	protected int getGridThickness() {
		if (shouldBePackedForManager()) {
			return isGridVisible() ? 1 : 0;
		}
		return 1;
	}

	@Override
	public void drawGeometry(Renderer renderer) {

		drawPlate(renderer);

	}

	@Override
	protected void setLineTextureHidden(Renderer renderer) {
		renderer.getRendererImpl().setDashTexture(Textures.DASH_SHORT);
	}

	@Override
	public int getLineType() {
		return EuclidianStyleConstants.LINE_TYPE_DASHED_SHORT;
	}

	@Override
	public void drawOutline(Renderer renderer) {
		// no outline
	}

	@Override
	protected void updateBounds(double xmin, double xmax, double ymin,
			double ymax) {
		// no bounds update
	}

	@Override
	public void enlargeBounds(Coords min, Coords max, boolean dontExtend) {
		// no bounds update
	}

	@Override
	public GColor getSurfaceColor() {
		if (getPlane().isPlateVisible()) {
			return super.getSurfaceColor();
		}
		return ManagerShaders.COLOR_INVISIBLE;
	}

	@Override
	public boolean isVisible() {
		return isGridVisible() || getPlane().isPlateVisible();
	}

}
