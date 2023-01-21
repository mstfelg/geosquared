package org.geogebra.common.geogebra3D.kernel3D;

import java.util.HashMap;

import org.geogebra.common.geogebra3D.kernel3D.geos.GeoAxis3D;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoClippingCube3D;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoPlane3DConstant;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoSpace;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Construction.Constants;
import org.geogebra.common.kernel.ConstructionCompanion;
import org.geogebra.common.kernel.ConstructionDefaults;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoAxisND;
import org.geogebra.common.kernel.kernelND.GeoDirectionND;
import org.geogebra.common.main.Localization;

/**
 * 3D construction companion
 * 
 * @author Mathieu
 */
public class ConstructionCompanion3D extends ConstructionCompanion {

	// axis objects
	private GeoAxis3D zAxis3D;
	private GeoPlane3DConstant xOyPlane;
	private GeoSpace space;
	private GeoClippingCube3D clippingCube;
	private String zAxis3DLocalName;
	private String xOyPlaneLocalName;
	private String spaceLocalName;

	/**
	 * default constructor
	 * 
	 * @param cons
	 *            construction
	 */
	public ConstructionCompanion3D(Construction cons) {
		super(cons);
	}

	@Override
	public void init() {
		super.init();

		zAxis3D = new GeoAxis3D(cons, GeoAxisND.Z_AXIS_3D);

		xOyPlane = new GeoPlane3DConstant(cons, GeoPlane3DConstant.XOY_PLANE);

		space = new GeoSpace(cons);

		clippingCube = new GeoClippingCube3D(cons);

	}

	@Override
	public GeoAxisND getZAxis() {
		return zAxis3D;
	}

	@Override
	public GeoDirectionND getXOYPlane() {
		return xOyPlane;
	}

	@Override
	public GeoDirectionND getSpace() {
		return space;
	}

	@Override
	public GeoElement getClippingCube() {
		return clippingCube;
	}

	/**
	 * creates the ConstructionDefaults consDefaults
	 */
	@Override
	public ConstructionDefaults newConstructionDefaults() {
		return new ConstructionDefaults3D(cons);
	}

	@Override
	protected void initGeoTables() {
		super.initGeoTables();

		HashMap<String, GeoElement> geoTable = cons.getGeoTable();

		geoTable.put("zAxis", zAxis3D);
		geoTable.put("xOyPlane", xOyPlane);
		geoTable.put("space", space);

		if (zAxis3DLocalName != null) {
			geoTable.put(zAxis3DLocalName, zAxis3D);
			geoTable.put(xOyPlaneLocalName, xOyPlane);
			geoTable.put(spaceLocalName, space);
		}

	}

	@Override
	public void updateLocalAxesNames() {

		super.updateLocalAxesNames();

		HashMap<String, GeoElement> geoTable = cons.getGeoTable();

		geoTable.remove(zAxis3DLocalName);
		geoTable.remove(xOyPlaneLocalName);
		geoTable.remove(spaceLocalName);

		Localization app = cons.getKernel().getLocalization();
		zAxis3DLocalName = app.getMenu("zAxis");
		xOyPlaneLocalName = app.getMenu("xOyPlane");
		spaceLocalName = app.getMenu("space");

		geoTable.put(zAxis3DLocalName, zAxis3D);
		geoTable.put(xOyPlaneLocalName, xOyPlane);
		geoTable.put(spaceLocalName, space);

	}

	@Override
	public Constants isConstantElement(GeoElement geo) {
		if (geo == zAxis3D) {
			return Constants.Z_AXIS;
		}
		if (geo == xOyPlane) {
			return Constants.XOY_PLANE;
		}
		if (geo == space) {
			return Constants.SPACE;
		}

		return super.isConstantElement(geo);
	}

	@Override
	public boolean is3D() {
		return true;
	}
}
