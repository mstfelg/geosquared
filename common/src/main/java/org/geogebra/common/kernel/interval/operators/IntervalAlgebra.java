package org.geogebra.common.kernel.interval.operators;

import static org.geogebra.common.kernel.interval.IntervalConstants.one;
import static org.geogebra.common.kernel.interval.IntervalConstants.undefined;
import static org.geogebra.common.kernel.interval.IntervalConstants.whole;
import static org.geogebra.common.kernel.interval.operators.RMath.powHigh;
import static org.geogebra.common.kernel.interval.operators.RMath.powLow;

import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalConstants;
import org.geogebra.common.util.DoubleUtil;

/**
 * Implements algebra functions in interval
 *
 *  fmod, pow, sqrt, nthRoot
 *
 * @author laszlo
 */
public class IntervalAlgebra {

	private final IntervalNodeEvaluator evaluator;

	/**
	 *
	 * @param evaluator {@link IntervalNodeEvaluator}
	 */
	public IntervalAlgebra(IntervalNodeEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * Computes x mod y (x - k * y)
	 * @param other argument.
	 * @return this as result
	 */
	Interval fmod(Interval interval, Interval other) {
		if (interval.isUndefined() || other.isUndefined()) {
			return undefined();
		}

		if (interval.isUndefined()) {
			return whole();
		}

		double yb = interval.getLow() < 0 ? other.getLow() : other.getHigh();
		double n = interval.getLow() / yb;
		if (n < 0) {
			n = Math.ceil(n);
		} else {
			n = Math.floor(n);
		}

		Interval result = new Interval(interval);
		Interval multiplicand = new Interval(other);
		// x mod y = x - n * y
		result.subtract(evaluator.multiply(multiplicand, new Interval(n)));
		return result;
	}

	/**
	 * @param interval to power.
	 * @param power of the interval
	 * @return power of the interval
	 */
	Interval pow(Interval interval, double power) {
		if (interval.isUndefined() || DoubleUtil.isEqual(power, 1)) {
			return new Interval(interval);
		}

		if (DoubleUtil.isEqual(power, -1)) {
			return evaluator.multiplicativeInverse(interval);
		}

		if (interval.isInverted()) {

			return evaluator.computeInverted(evaluator.pow(interval.extractLow(), power),
					evaluator.pow(interval.extractHigh(), power));
		}

		if (power == 0) {
			return powerOfZero(interval);
		} else if (power < 0) {
			Interval divide = evaluator.divide(one(),
					evaluator.pow(new Interval(interval), -power));
			return new Interval(divide);
		}

		if (!DoubleUtil.isInteger(power)) {
			if (interval.isOne()) {
				return interval;
			}
			return powerOfDouble(interval, power);
		}

		return powOfInteger(interval, Math.round(power));
	}

	private Interval powerOfDouble(Interval interval, double power) {
		Interval other = new Interval(power);
		if (interval.isInverted()) {
			Interval lnPower2 = lnPower(interval.extractHigh(), other);
			return evaluator.exp(new Interval(lnPower2));
		} else {
			return evaluator.exp(lnPower(interval, other));
		}
	}

	private Interval lnPower(Interval interval, Interval other) {
		return evaluator.multiply(evaluator.log(interval), other);
	}

	private Interval powOfInteger(Interval interval, long power) {
		if (interval.getHigh() < 0) {
			// [negative, negative]
			double yl = powLow(-interval.getHigh(), power);
			double yh = powHigh(-interval.getLow(), power);
			if ((power & 1) == 1) {
				// odd power
				return new Interval(-yh, -yl);
			} else {
				// even power
				return new Interval(yl, yh);
			}
		} else if (interval.getLow() < 0) {
			// [negative, positive]
			if ((power & 1) == 1) {
				return new Interval(-powLow(-interval.getLow(), power),
						powHigh(interval.getHigh(), power));
			} else {
				// even power means that any negative number will be zero (min value = 0)
				// and the max value will be the max of x.lo^power, x.hi^power
				return new Interval(0,
						powHigh(Math.max(-interval.getLow(), interval.getHigh()), power));
			}
		}

		// [positive, positive]
		return new Interval(powLow(interval.getLow(), power),
					powHigh(interval.getHigh(), power));

	}

	private Interval powerOfZero(Interval interval) {
		if (interval.getLow() == 0 && interval.getHigh() == 0) {
			// 0^0
			return undefined();
		}

		return one();
	}

	/**
	 * Power of an interval where power is also an interval
	 * that must be a singleton, ie [n, n]
	 * @param other interval power.
	 * @return this as result.
	 */
	Interval pow(Interval interval, Interval other) {
		if (other.isZero()) {
			return one();
		}

		if (!other.isSingleton()) {
			return powerOfInterval(interval, other);
		}

		return pow(interval, other.getLow());
	}

	private Interval powerOfInterval(Interval interval, Interval other) {
		if (interval.isUndefined() || other.isUndefined()) {
			return IntervalConstants.undefined();
		}

		if (other.isInverted()) {
			Interval extractedLow = pow(interval, other.extractLow());
			Interval extractedHigh = pow(interval, other.extractHigh());
			if (extractedHigh.isUndefined()) {
				return undefined();
			}
			return evaluator.computeInverted(extractedLow, extractedHigh);
		}

		double low = powLow(interval.getLow(), other.getLow());
		double high = powHigh(interval.getHigh(), other.getHigh());

		if (Double.isNaN(low) || Double.isNaN(high)) {
			return undefined();
		}

		if (interval.getLow() > - 1 && interval.getHigh() < 1) {
			return new Interval(high, low);
		}

		return new Interval(low, high);
	}
}
