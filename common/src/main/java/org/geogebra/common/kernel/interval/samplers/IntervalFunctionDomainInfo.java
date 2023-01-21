package org.geogebra.common.kernel.interval.samplers;

import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalConstants;

public class IntervalFunctionDomainInfo {

	private Interval domainBefore = IntervalConstants.undefined();

	public boolean hasZoomedOut(Interval domain) {
		return isMinLower(domain) && isMaxHigher(domain);
	}

	public boolean hasPannedLeft(Interval domain) {
		return isMinLower(domain);
	}

	public boolean hasPannedRight(Interval domain) {
		return isMaxHigher(domain);
	}

	private boolean isMaxHigher(Interval domain) {
		return domain.getHigh() > domainBefore.getHigh();
	}

	private boolean isMinLower(Interval domain) {
		return domain.getLow() < domainBefore.getLow();
	}

	public void update(Interval domain) {
		domainBefore = domain;
	}

}
