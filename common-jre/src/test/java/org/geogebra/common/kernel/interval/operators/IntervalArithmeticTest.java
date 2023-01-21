package org.geogebra.common.kernel.interval.operators;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.geogebra.common.kernel.interval.IntervalConstants.whole;
import static org.geogebra.common.kernel.interval.IntervalConstants.zero;
import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalConstants;
import org.junit.Assert;
import org.junit.Test;

public class IntervalArithmeticTest {
	private final IntervalNodeEvaluator evaluator = new IntervalNodeEvaluator();

	@Test
	public void testMultiplicationEmpty() {
		assertTrue(evaluator.multiply(new Interval(), interval(1, 1)).isUndefined());
	}

	@Test
	public void testMultiplicationPositiveWithPositive() {
		Assert.assertEquals(interval(2, 6),
				evaluator.multiply(interval(1, 2), interval(2, 3)));

		Assert.assertEquals(interval(4, POSITIVE_INFINITY),
				evaluator.multiply(interval(1, POSITIVE_INFINITY), interval(4, 6)));

		Assert.assertEquals(interval(POSITIVE_INFINITY, POSITIVE_INFINITY),
				evaluator.multiply(interval(1, POSITIVE_INFINITY),
						interval(POSITIVE_INFINITY, POSITIVE_INFINITY)));
	}

	@Test
	public void testMultiplicationPositiveWithNegative() {
		Assert.assertEquals(interval(-6, -2),
				evaluator.multiply(interval(1, 2), interval(-3, -2)));

		Assert.assertEquals(interval(NEGATIVE_INFINITY, -2),
				evaluator.multiply(interval(1, POSITIVE_INFINITY), interval(-3, -2)));
	}

	@Test
	public void testMultiplicationPositiveWithMixed() {
		Assert.assertEquals(interval(-4, 6),
				evaluator.multiply(interval(1, 2), interval(-2, 3)));

		Assert.assertEquals(interval(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				evaluator.multiply(interval(1, POSITIVE_INFINITY), interval(-2, 3)));
	}

	@Test
	public void testMultiplicationPositiveWithZero() {
		Assert.assertEquals(interval(0, 0),
				evaluator.multiply(interval(1, 2), interval(0, 0)));

		Assert.assertEquals(interval(0, 0),
				evaluator.multiply(interval(1, POSITIVE_INFINITY), interval(0, 0)));
	}

	@Test
	public void testMultiplicationNegativeWithPositive() {
		Assert.assertEquals(interval(-6, -2),
				evaluator.multiply(interval(-3, -2), interval(1, 2)));

		Assert.assertEquals(interval(NEGATIVE_INFINITY, -2),
				evaluator.multiply(interval(-3, -2), interval(1, POSITIVE_INFINITY)));
	}

	@Test
	public void testMultiplicationNegativeWithNegative() {
		Assert.assertEquals(interval(2, 6),
				evaluator.multiply(interval(-2, -1), interval(-3, -2)));

		Assert.assertEquals(interval(4, POSITIVE_INFINITY),
				evaluator.multiply(interval(NEGATIVE_INFINITY, -1), interval(-6, -4)));

		Assert.assertEquals(interval(POSITIVE_INFINITY, POSITIVE_INFINITY),
				evaluator.multiply(interval(NEGATIVE_INFINITY, -1),
						interval(NEGATIVE_INFINITY, NEGATIVE_INFINITY)));
	}

	@Test
	public void testMultiplicationNegativeWithMixed() {
		Assert.assertEquals(interval(-6, 4),
				evaluator.multiply(interval(-2, -1), interval(-2, 3)));

		Assert.assertEquals(IntervalConstants.whole(),
				evaluator.multiply(interval(NEGATIVE_INFINITY, -1), interval(-2, 3)));
	}

	@Test
	public void testMultiplicationNegativeWithZero() {
		assertEquals(zero(), evaluator.multiply(interval(-2, -1), zero()));
		assertEquals(zero(), evaluator.multiply(interval(NEGATIVE_INFINITY, -1), zero()));
	}

	@Test
	public void testMultiplicationMixedWithPositive() {
		Assert.assertEquals(interval(-4, 6),
				evaluator.multiply(interval(-2, 3), interval(1, 2)));

		assertEquals(whole(),
				evaluator.multiply(interval(-2, 3), interval(1, POSITIVE_INFINITY)));
	}

	@Test
	public void testMultiplicationMixedWithNegative() {
		Assert.assertEquals(interval(-6, 4),
				evaluator.multiply(interval(-2, 3), interval(-2, -1)));

		assertEquals(whole(),
				evaluator.multiply(interval(-2, 3), interval(NEGATIVE_INFINITY, -1)));
	}

	@Test
	public void testMultiplicationMixedWithMixed() {
		Assert.assertEquals(interval(-8, 12),
				evaluator.multiply(interval(-2, 3), interval(-1, 4)));

		assertEquals(whole(),
				evaluator.multiply(interval(NEGATIVE_INFINITY, 3),
						interval(-1, POSITIVE_INFINITY)));
	}

	@Test
	public void testMultiplicationMixedWithZero() {
		assertEquals(zero(), evaluator.multiply(interval(-2, 1), zero()));
		assertEquals(zero(), evaluator.multiply(interval(NEGATIVE_INFINITY, 1), zero()));
	}

	@Test
	public void testMultiplicationZeroWithAny() {
		assertEquals(zero(), evaluator.multiply(zero(), interval(-2, 1)));
		assertEquals(zero(), evaluator.multiply(zero(), interval(-2, -1)));
		assertEquals(zero(), evaluator.multiply(zero(), interval(1, 2)));
		assertEquals(zero(), evaluator.multiply(zero(), interval(0, 0)));
	}
	}