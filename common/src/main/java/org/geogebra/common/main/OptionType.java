package org.geogebra.common.main;

/**
 * Option panel types
 */
public enum OptionType {
	// Order matters for the selection menu. A separator is placed after
	// OBJECTS and SPREADSHEET to isolate the view options
	OBJECTS, EUCLIDIAN, EUCLIDIAN2, EUCLIDIAN_FOR_PLANE, EUCLIDIAN3D,

	CAS, SPREADSHEET, LAYOUT, DEFAULTS, ALGEBRA, GLOBAL
}