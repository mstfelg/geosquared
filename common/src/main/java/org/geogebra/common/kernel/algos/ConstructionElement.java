/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.algos;

import java.util.TreeSet;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.GTemplate;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.Localization;

import com.google.j2objc.annotations.Weak;

/**
 * Element of the construction tree
 * 
 * @author Markus
 */
public abstract class ConstructionElement
		implements Comparable<ConstructionElement> {

	/** parent construction of this element */
	@Weak
	public transient Construction cons;
	/** parent kernel of this element */
	@Weak
	public transient Kernel kernel;

	private int constIndex = -1; // index in construction list

	private long ceID; // creation ID of this ConstructionElement, used for
						// sorting

	/**
	 * Creates new construction element
	 * 
	 * @param c
	 *            construction
	 */
	public ConstructionElement(final Construction c) {
		ceID = c.getApplication().getNextCeIDcounter();
		setConstruction(c);
	}

	/**
	 * @param c
	 *            new construction
	 */
	public void setConstruction(final Construction c) {
		cons = c;
		kernel = c.getKernel();
	}

	/**
	 * @return construction this element belongs to
	 */
	public Construction getConstruction() {
		return cons;
	}

	/**
	 * @return kernel
	 */
	public final Kernel getKernel() {
		return kernel;
	}

	/**
	 * Returns the smallest possible construction index for this object in its
	 * construction.
	 * 
	 * @return the smallest possible construction index for this object
	 */
	public abstract int getMinConstructionIndex();

	/**
	 * Returns the largest possible construction index for this object in its
	 * construction.
	 * 
	 * @return the largest possible construction index for this object
	 */
	public abstract int getMaxConstructionIndex();

	/**
	 * Returns construction index in current construction.
	 * 
	 * @return construction index in current construction.
	 */
	public int getConstructionIndex() {
		return constIndex;
	}

	/**
	 * Sets construction index in current construction. This method should only
	 * be called from Construction.
	 * 
	 * @param index
	 *            new construction index
	 */
	public void setConstructionIndex(int index) {
		constIndex = index;
	}

	/**
	 * Returns whether this construction element is in the construction list of
	 * its construction.
	 * 
	 * @return true for elements in construction list
	 */
	final public boolean isInConstructionList() {
		return constIndex > -1;
	}

	/**
	 * Returns whether this element is a breakpoint in the construction protocol
	 * 
	 * @return whether this element is a breakpoint in the construction protocol
	 */
	abstract public boolean isConsProtocolBreakpoint();

	/**
	 * Returns whether this object is available at the given construction step
	 * (this depends on this object's construction index).
	 * 
	 * @param step
	 *            construction step
	 * @return whether this object is available at the given construction step
	 */
	public boolean isAvailableAtConstructionStep(int step) {
		// Note: this method is overwritten by
		// GeoAxis in order to make the axes available
		// in empty constructions too (for step == -1)
		int pos = getConstructionIndex();
		return pos >= 0 && pos <= step;
	}

	/**
	 * Returns true for an independent GeoElement and false otherwise.
	 * 
	 * @return true for independent GeoElement
	 */
	public abstract boolean isIndependent();

	/**
	 * Returns XML representation of this object. GeoGebra File Format.
	 * 
	 * @param getListentersToo
	 *            true if should get also listeners
	 * @param sb
	 *            string builder
	 */
	public abstract void getXML(boolean getListentersToo, StringBuilder sb);

	/**
	 * Returns XML representation of this object. OGP format.
	 * 
	 * @param sb
	 *            string builder
	 */
	public void getXML_OGP(StringBuilder sb) {
		getXML(false, sb);
	}

	/**
	 * Removes this object and all dependent objects from the Kernel. If this
	 * object is not independent, it's parent algorithm is removed too.
	 */
	public abstract void remove();

	/**
	 * Updates this object.
	 */
	public abstract void update();

	/**
	 * Notifies all views to remove this object.
	 */
	public abstract void notifyRemove();

	/**
	 * Notifies all views to add this object.
	 */
	public abstract void notifyAdd();

	/**
	 * Returns an array with all GeoElements of this construction element.
	 * 
	 * @return an array with all GeoElements of this construction element.
	 */
	public abstract GeoElementND[] getGeoElements();

	/**
	 * @return true for GeoElements
	 */
	public abstract boolean isGeoElement();

	/**
	 * @return true for AlgoElements
	 */
	public abstract boolean isAlgoElement();

	/**
	 * Returns type and name of this construction element (e.g. "Point A").
	 * Note: may return ""
	 * 
	 * @return type and name of this construction element (e.g. "Point A").
	 */
	public abstract String getNameDescription();

	/**
	 * Returns textual description of the definition of this construction
	 * element (e.g. "Line through A and B"). Note: may return ""
	 * 
	 * @param tpl
	 *            string template
	 * @return textual description of the definition
	 */
	public abstract String getDefinitionDescription(StringTemplate tpl);

	/**
	 * Returns command that defines this construction element (e.g. "Line[A, B]"
	 * ). Note: may return ""
	 */

	/**
	 * Returns name of class. This is needed to allow code obfuscation.
	 */

	/**
	 * Returns the mode ID of a related tool.
	 * 
	 * @return mode ID, returns -1 if there is no related tool.
	 */
	public int getRelatedModeID() {
		return -1;
	}

	/* Comparable interface */

	/**
	 * Compares using creation ID. Older construction elements are larger. Note:
	 * 0 is only returned for this == obj.
	 */
	@Override
	public int compareTo(ConstructionElement obj) {
		if (this == obj) {
			return 0;
		}

		ConstructionElement ce = obj;
		if (ceID < ce.ceID) {
			return -1;
		}
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	/**
	 * return the construction element ID
	 * 
	 * @return the construction element ID
	 */
	public long getID() {

		return ceID;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/**
	 * @param tpl
	 *            string template
	 * @return command description, e.g. "Midpoint[A,B]"
	 */
	public abstract String getDefinition(StringTemplate tpl);

	public abstract String toString(GTemplate tpl);

	protected Localization getLoc() {
		return cons.getKernel().getLocalization();
	}

	/**
	 * adds all predecessors of this object to the given set the set is
	 * topologically sorted
	 * 
	 * @param set
	 *            set of predecessors
	 * @param onlyIndependent
	 *            whether only indpendent geos should be added
	 */
	abstract public void addPredecessorsToSet(final TreeSet<GeoElement> set,
			final boolean onlyIndependent);
}
