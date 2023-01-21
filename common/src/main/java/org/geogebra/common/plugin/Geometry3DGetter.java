package org.geogebra.common.plugin;

import org.geogebra.common.kernel.geos.GeoElement;

/**
 * interface to get geometries from 3D renderer
 */
public interface Geometry3DGetter {

	/**
	 * geometry type so the getter can sort in several parts
	 */
	public enum GeometryType {
		/** geometry from an axis */
		AXIS("axes"),
		/** geometry from a surface */
		SURFACE("surfaces"),
		/** geometry from a curve or line */
		CURVE("curves");

		/** name for export */
		final public String name;

		GeometryType(String name) {
			this.name = name;
		}
	}

	/**
	 * 
	 * @param geo
	 *            geo
	 * @param type
	 *            geometry type
	 * @return true if it handles the geometry type
	 */
	public boolean handles(GeoElement geo, GeometryType type);

	/**
	 * start new geometry
	 * 
	 * @param type
	 *            geometry type
	 */
	public void startGeometry(GeometryType type);

	/**
	 * add vertex, normal, color element
	 * 
	 * @param x
	 *            vertex x-coord
	 * @param y
	 *            vertex y-coord
	 * @param z
	 *            vertex z-coord
	 * @param nx
	 *            normal x-coord
	 * @param ny
	 *            normal y-coord
	 * @param nz
	 *            normal z-coord
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 * @param a
	 *            opacity
	 */
	public void addVertexNormalColor(double x, double y, double z, double nx,
			double ny, double nz, double r, double g, double b, double a);

	/**
	 * add 3 indices (as triangle)
	 * 
	 * @param i1
	 *            vertex index
	 * @param i2
	 *            vertex index
	 * @param i3
	 *            vertex index
	 */
	public void addTriangle(int i1, int i2, int i3);
}
