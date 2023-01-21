/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.algos;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.util.Cloner;
import org.geogebra.common.euclidian.draw.DrawBarGraph;
import org.geogebra.common.euclidian.draw.DrawBarGraph.DrawType;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.kernel.statistics.AlgoUsingUniqueAndFrequency;
import org.geogebra.common.util.debug.Log;

/**
 * Bar chart algorithm.
 * 
 * @author G. Sturr
 * 
 */
public class AlgoBarChart extends AlgoUsingUniqueAndFrequency
		implements DrawInformationAlgo, ChartStyleAlgo {

	private ChartStyle chartStyle = new ChartStyle(null);

	/** Bar chart from expression **/
	public static final int TYPE_BARCHART_EXPRESSION = 0;

	/** Bar chart from raw data and given width **/
	public static final int TYPE_BARCHART_RAWDATA = 1;

	/** Bar chart from (values,frequencies) **/
	public static final int TYPE_BARCHART_FREQUENCY_TABLE = 2;

	/** Bar chart from (values,frequencies) with given width **/
	public static final int TYPE_BARCHART_FREQUENCY_TABLE_WIDTH = 3;

	/** Stick graph **/
	public static final int TYPE_STICKGRAPH = 10;

	/** Step graph **/
	public static final int TYPE_STEPGRAPH = 20;

	/** Graph of a discrete probability distribution **/
	public static final int TYPE_BARCHART_BINOMIAL = 40;
	public static final int TYPE_BARCHART_PASCAL = 41;
	public static final int TYPE_BARCHART_POISSON = 42;
	public static final int TYPE_BARCHART_HYPERGEOMETRIC = 43;
	public static final int TYPE_BARCHART_BERNOULLI = 44;
	public static final int TYPE_BARCHART_ZIPF = 45;

	// largest possible number of rectangles
	private static final int MAX_RECTANGLES = 10000;

	// output
	private GeoNumeric sum;

	// input
	private GeoNumberValue a;
	private GeoNumberValue b;
	private GeoNumberValue p1;
	private GeoNumberValue p2;
	private GeoNumberValue p3;
	private GeoList list1;
	private GeoList list2;

	// local fields
	private GeoElement widthGeo;
	private GeoElement isCumulative;
	private GeoElement isHorizontal;
	private GeoElement p1geo;
	private GeoElement p2geo;
	private GeoElement p3geo;
	private GeoBoolean hasJoin;
	private GeoElement pointType;
	private GeoNumeric scale;

	private int type;
	private int N; // # of intervals
	private double[] yval; // y value (= min) in interval 0 <= i < N
	private double[] leftBorder; // leftBorder (x val) of interval 0 <= i < N
	private String[] value; // value string for each bar
	private double barWidth;
	private double freqMax;
	private double dataSize;

	private String toolTipText;

	// flag to determine if result sum measures area or length
	private boolean isAreaSum = true;

	/******************************************************
	 * BarChart[&lt;interval start&gt;,&lt;interval stop&gt;, &lt;list of
	 * heights&gt;]
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param a
	 *            first value
	 * @param b
	 *            second value
	 * @param list1
	 *            list
	 */
	public AlgoBarChart(Construction cons, String label, GeoNumberValue a,
			GeoNumberValue b, GeoList list1) {
		super(cons);

		type = TYPE_BARCHART_EXPRESSION;

		this.a = a;
		this.b = b;
		this.list1 = list1;

		// output
		sum = new GeoNumeric(cons) {
			@Override
			public String getTooltipText(final boolean colored,
					final boolean alwaysOn) {
				return toolTipText;
			}
		};

		setInputOutput(); // for AlgoElement
		compute();
		sum.setDrawable(true);
		sum.setLabel(label);
	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of raw data&gt;, &lt;bar
	 * width&gt;]
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param list1
	 *            list
	 * @param width
	 *            width
	 */
	public AlgoBarChart(Construction cons, String label, GeoList list1,
			GeoNumeric width) {
		this(cons, list1, null, width, null, null, null, TYPE_BARCHART_RAWDATA);
		sum.setLabel(label);

	}

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param list1
	 *            list
	 * @param width
	 *            width
	 * @param scale
	 *            scale
	 */
	public AlgoBarChart(Construction cons, String label, GeoList list1,
			GeoNumeric width, GeoNumeric scale) {
		this(cons, list1, null, width, null, null, null, scale,
				TYPE_BARCHART_RAWDATA);
		sum.setLabel(label);

	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of raw data&gt;, &lt;bar
	 * width&gt;]
	 * 
	 * @param cons
	 *            construction
	 * @param list1
	 *            first list
	 * @param width
	 *            width
	 */
	public AlgoBarChart(Construction cons, GeoList list1, GeoNumeric width) {
		this(cons, list1, null, width, null, null, null, TYPE_BARCHART_RAWDATA);
	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of values&gt;, &lt;list of
	 * frequencies&gt;]
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 */
	public AlgoBarChart(Construction cons, String label, GeoList list1,
			GeoList list2) {

		this(cons, list1, list2, null, null, null, null,
				TYPE_BARCHART_FREQUENCY_TABLE);
		sum.setLabel(label);
	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of values&gt;, &lt;list of
	 * frequencies&gt;] (no label)
	 * 
	 * @param cons
	 *            construction
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 */
	public AlgoBarChart(Construction cons, GeoList list1, GeoList list2) {

		this(cons, list1, list2, null, null, null, null,
				TYPE_BARCHART_FREQUENCY_TABLE);
	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of values&gt;, &lt;list of
	 * frequencies&gt;, &lt;bar width&gt;]
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 * @param width
	 *            width
	 */
	public AlgoBarChart(Construction cons, String label, GeoList list1,
			GeoList list2, GeoNumberValue width) {

		this(cons, list1, list2, width, null, null, null,
				TYPE_BARCHART_FREQUENCY_TABLE_WIDTH);
		sum.setLabel(label);
	}

	/******************************************************
	 * BarChart[&lt;a&gt;,&lt;b&gt;, &lt;list of values&gt;, &lt;list of
	 * frequencies&gt;, &lt;bar width&gt;] (no label)
	 * 
	 * @param cons
	 *            construction
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 * @param width
	 *            width
	 */
	public AlgoBarChart(Construction cons, GeoList list1, GeoList list2,
			GeoNumberValue width) {

		this(cons, list1, list2, width, null, null, null,
				TYPE_BARCHART_FREQUENCY_TABLE_WIDTH);
	}

	/******************************************************
	 * General constructor with label
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 * @param width
	 *            width
	 * @param isHorizontal
	 *            true if horizontal
	 * @param join
	 *            true if join
	 * @param pointType
	 *            point type
	 * @param type
	 *            type id
	 * 
	 */
	public AlgoBarChart(Construction cons, String label, GeoList list1,
			GeoList list2, GeoNumberValue width, GeoBoolean isHorizontal,
			GeoBoolean join, GeoNumeric pointType, int type) {

		this(cons, list1, list2, width, isHorizontal, join, pointType, type);
		sum.setLabel(label);

	}

	/******************************************************
	 * General constructor
	 * 
	 * @param cons
	 *            construction
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 * @param width
	 *            width
	 * @param isHorizontal
	 *            true if horizontal
	 * @param join
	 *            true if join
	 * @param pointType
	 *            point type
	 * @param type
	 *            type id
	 */
	public AlgoBarChart(Construction cons, GeoList list1, GeoList list2,
			GeoNumberValue width, GeoBoolean isHorizontal, GeoBoolean join,
			GeoNumeric pointType, int type) {

		this(cons, list1, list2, width, isHorizontal, join, pointType, null,
				type);

	}

	/******************************************************
	 * General constructor
	 * 
	 * @param cons
	 *            construction
	 * @param list1
	 *            first list
	 * @param list2
	 *            second list
	 * @param width
	 *            width
	 * @param isHorizontal
	 *            true if horizontal
	 * @param join
	 *            true if join
	 * @param pointType
	 *            point type
	 * @param scale
	 *            scale
	 * @param type
	 *            type id
	 */
	public AlgoBarChart(Construction cons, GeoList list1, GeoList list2,
			GeoNumberValue width, GeoBoolean isHorizontal, GeoBoolean join,
			GeoNumeric pointType, GeoNumeric scale, int type) {
		super(cons);

		this.type = type;

		this.list1 = list1;
		this.list2 = list2;
		if (width != null) {
			widthGeo = width.toGeoElement();
		}
		this.isHorizontal = isHorizontal;
		this.hasJoin = join;
		this.pointType = pointType;

		this.scale = scale;

		sum = new GeoNumeric(cons) {
			@Override
			public String getTooltipText(final boolean colored,
					final boolean alwaysOn) {
				return toolTipText;
			}
		};

		setInputOutput(); // for AlgoElement
		compute();
		sum.setDrawable(true);
	}

	/******************************************************
	 * Discrete distribution bar chart
	 * 
	 * @param cons
	 *            construction
	 * @param label
	 *            label
	 * @param p1
	 *            first value
	 * @param p2
	 *            second value
	 * @param p3
	 *            third value
	 * @param isCumulative
	 *            true if cumulative
	 * @param type
	 *            type id
	 */
	public AlgoBarChart(Construction cons, String label, GeoNumberValue p1,
			GeoNumberValue p2, GeoNumberValue p3, GeoBoolean isCumulative,
			int type) {

		super(cons);

		this.type = type;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		p1geo = p1.toGeoElement();
		if (p2 != null) {
			p2geo = p2.toGeoElement();
		}
		if (p3 != null) {
			p3geo = p3.toGeoElement();
		}
		this.isCumulative = isCumulative;

		sum = new GeoNumeric(cons) {
			@Override
			public String getTooltipText(final boolean colored,
					final boolean alwaysOn) {
				return toolTipText;
			}
		};

		setInputOutput(); // for AlgoElement
		compute();
		sum.setDrawable(true);
		sum.setLabel(label);
		if (yval == null) {
			yval = new double[0];
			leftBorder = new double[0];
		}
	}

	/******************************************************
	 * Copy constructor for discrete distribution bar chart
	 * 
	 * @param p1
	 *            first value
	 * @param p2
	 *            second value
	 * @param p3
	 *            third value
	 * @param isCumulative
	 *            true if cumulative
	 * @param type
	 *            type id
	 */
	protected AlgoBarChart(GeoNumberValue p1, GeoNumberValue p2,
			GeoNumberValue p3, GeoBoolean isCumulative, int type,
			GeoNumberValue a, GeoNumberValue b, double[] vals, double[] borders,
			int N) {

		super(p1.getConstruction(), false);

		this.type = type;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		p1geo = p1.toGeoElement();
		if (p2 != null) {
			p2geo = p2.toGeoElement();
		}
		if (p3 != null) {
			p3geo = p3.toGeoElement();
		}
		this.isCumulative = isCumulative;
		this.a = a;
		this.b = b;
		this.yval = vals;

		this.leftBorder = borders;
		this.N = N;
	}

	// ==================================================
	// Copy constructors
	// ==================================================

	private AlgoBarChart(Construction cons, GeoNumberValue a, GeoNumberValue b,
			double[] vals, double[] borders, int N) {
		super(cons, false);

		type = TYPE_BARCHART_EXPRESSION;

		this.a = a;
		this.b = b;
		this.yval = vals;
		this.leftBorder = borders;
		this.N = N;

	}

	private AlgoBarChart(Construction cons, GeoNumeric width, double[] vals,
			double[] borders, int N) {
		super(cons, false);
		type = TYPE_BARCHART_RAWDATA;

		this.widthGeo = width;
		this.yval = vals;
		this.leftBorder = borders;
		this.N = N;

	}

	private AlgoBarChart(Construction cons, double[] vals, double[] borders,
			int N) {
		super(cons, false);
		type = TYPE_BARCHART_FREQUENCY_TABLE;

		this.yval = vals;
		this.leftBorder = borders;
		this.N = N;
	}

	private AlgoBarChart(Construction cons, GeoNumberValue width, double[] vals,
			double[] borders, int N) {
		super(cons, false);
		type = TYPE_BARCHART_FREQUENCY_TABLE_WIDTH;

		widthGeo = width.toGeoElement();

		this.yval = vals;
		this.leftBorder = borders;
		this.N = N;

	}

	// ======================================================
	// InputOutput
	// ======================================================

	// for AlgoElement
	@Override
	protected void setInputOutput() {

		ArrayList<GeoElement> list = new ArrayList<>();

		switch (type) {
		default:
			// do nothing
			break;

		case TYPE_BARCHART_EXPRESSION:

			input = new GeoElement[3];
			input[0] = a.toGeoElement();
			input[1] = b.toGeoElement();
			input[2] = list1;
			break;

		case TYPE_BARCHART_RAWDATA:
			createHelperAlgos(list1, scale);

			// fall through
		case TYPE_BARCHART_FREQUENCY_TABLE:
		case TYPE_BARCHART_FREQUENCY_TABLE_WIDTH:

			list.add(list1);
			if (list2 != null) {
				list.add(list2);
			}
			if (widthGeo != null) {
				list.add(widthGeo);
			}

			if (scale != null) {
				list.add(scale);
			}

			input = new GeoElement[list.size()];
			input = list.toArray(input);
			break;

		case TYPE_STICKGRAPH:

			list.add(list1);
			if (list2 != null) {
				list.add(list2);
			}

			if (isHorizontal != null) {
				list.add(isHorizontal);
			}

			input = new GeoElement[list.size()];
			input = list.toArray(input);
			break;

		case TYPE_STEPGRAPH:

			list.add(list1);
			if (list2 != null) {
				list.add(list2);
			}

			if (hasJoin != null) {
				list.add(hasJoin);
			}
			if (pointType != null) {
				list.add(pointType);
			}

			input = new GeoElement[list.size()];
			input = list.toArray(input);
			break;

		case TYPE_BARCHART_BERNOULLI:
		case TYPE_BARCHART_BINOMIAL:
		case TYPE_BARCHART_PASCAL:
		case TYPE_BARCHART_HYPERGEOMETRIC:
		case TYPE_BARCHART_POISSON:
		case TYPE_BARCHART_ZIPF:
			ArrayList<GeoElement> inputList = new ArrayList<>();
			inputList.add(p1geo);
			if (p2geo != null) {
				inputList.add(p2geo);
			}
			if (p3geo != null) {
				inputList.add(p3geo);
			}
			if (isCumulative != null) {
				inputList.add(isCumulative);
			}

			input = new GeoElement[inputList.size()];
			input = inputList.toArray(input);
			break;
		}
		setOutputLength(1);
		setOutput(0, sum);
		setDependencies(); // done by AlgoElement

	}

	// ======================================================
	// Getters/Setters
	// ======================================================

	@Override
	public Commands getClassName() {
		return Commands.BarChart;
	}

	/**
	 * @return the resulting sum
	 */
	public GeoNumeric getSum() {
		return sum;
	}

	/**
	 * @return the isCumulative
	 */
	public GeoElement getIsCumulative() {
		return isCumulative;
	}

	/**
	 * @return maximum frequency of a bar chart
	 */
	public double getFreqMax() {

		freqMax = 0.0;
		for (int k = 0; k < yval.length; ++k) {
			freqMax = Math.max(yval[k], freqMax);
		}
		return freqMax;
	}

	/**
	 * @return y values (heights) of a bar chart
	 */
	public double[] getYValue() {
		return yval;
	}

	/**
	 * @return left class borders of a bar chart
	 */
	public double[] getLeftBorder() {
		return leftBorder;
	}

	/**
	 * @return values of a bar chart formatted as string (for frequency tables)
	 */
	public String[] getValue() {
		return value;
	}

	/**
	 * @return type of the bar chart
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return lower bound for sums
	 */
	public GeoNumberValue getA() {
		return a == null ? new GeoNumeric(cons, Double.NaN) : a;
	}

	/**
	 * @return upper bound for sums
	 */
	public GeoNumberValue getB() {
		return b == null ? new GeoNumeric(cons, Double.NaN) : b;
	}

	/**
	 * @return list of function values
	 */
	public double[] getValues() {
		return yval;
	}

	/**
	 * number of intervals
	 * 
	 * @return number of intervals
	 */
	@Override
	public int getIntervals() {
		return N;
	}

	/**
	 * @return bar width
	 */
	public double getWidth() {
		return barWidth;
	}

	/**
	 * @return discrete graph parameter p1
	 */
	public NumberValue getP1() {
		return p1;
	}

	/**
	 * @return discrete graph parameter p2
	 */
	public GeoNumberValue getP2() {
		return p2;
	}

	/**
	 * @return discrete graph parameter p3
	 */
	public GeoNumberValue getP3() {
		return p3;
	}

	/**
	 * @return the type of graph to draw
	 */
	public DrawType getDrawType() {

		// case 1: step graphs
		if (type == TYPE_STEPGRAPH) {
			if (hasJoin != null && hasJoin.getBoolean()) {
				return DrawType.STEP_GRAPH_CONTINUOUS;
			}
			return DrawType.STEP_GRAPH_JUMP;
		}

		// case 2: cumulative discrete probability
		else if (isCumulative != null
				&& ((GeoBoolean) isCumulative).getBoolean()) {
			return DrawType.STEP_GRAPH_CONTINUOUS;

			// case 3: all other types use either horizontal or vertical bars
		} else if (isHorizontal != null
				&& ((GeoBoolean) isHorizontal).getBoolean()) {
			return DrawType.HORIZONTAL_BAR;

		} else {
			return DrawType.VERTICAL_BAR;
		}
	}

	/**
	 * @return true if points are drawn with the graph
	 */
	public boolean hasPoints() {
		return type == TYPE_STICKGRAPH || type == TYPE_STEPGRAPH;
	}

	/**
	 * @return point style
	 */
	public int getPointType() {

		if (type == TYPE_STICKGRAPH) {
			return DrawBarGraph.POINT_LEFT;
		}
		if (pointType == null) {
			return DrawBarGraph.POINT_NONE;
		}

		int p = (int) ((GeoNumeric) pointType).getDouble();
		if (p < -2 || p > 2) {
			p = DrawBarGraph.POINT_NONE;
		}
		return p;

	}

	// ======================================================
	// Compute
	// ======================================================

	@Override
	public void compute() {

		isAreaSum = true;

		switch (type) {

		default:
			// do nothing
			break;
		case TYPE_BARCHART_FREQUENCY_TABLE:
		case TYPE_BARCHART_FREQUENCY_TABLE_WIDTH:
			computeWithFrequency();
			break;

		case TYPE_STICKGRAPH:
		case TYPE_STEPGRAPH:

			isAreaSum = false;

			if (list1 == null || !list1.isDefined()) {
				sum.setUndefined();
				return;
			}

			if (list1.getGeoElementForPropertiesDialog().isGeoPoint()) {
				computeFromPointList(list1);
			} else {
				if (list2 == null) {
					sum.setUndefined();
					return;
				}
				barWidth = 0.0;
				computeFromValueFrequencyLists(list1, list2);
			}
			break;

		case TYPE_BARCHART_EXPRESSION:
			computeWithExp();
			break;

		case TYPE_BARCHART_RAWDATA:
			computeWithRawData();
			break;

		case TYPE_BARCHART_BINOMIAL:
		case TYPE_BARCHART_POISSON:
		case TYPE_BARCHART_HYPERGEOMETRIC:
		case TYPE_BARCHART_PASCAL:
		case TYPE_BARCHART_ZIPF:

			if (!prepareDistributionLists()) {
				sum.setUndefined();
				return;
			}
			barWidth = -1;
			computeWithFrequency();
			break;

		}
	}

	private void computeWithExp() {
		GeoElement geo; // temporary var

		if (!(a.isDefined() && b.isDefined() && list1.isDefined())) {
			sum.setUndefined();
			return;
		}

		N = list1.size();

		double ad = a.getDouble();
		double bd = b.getDouble();

		double ints = list1.size();
		if (ints < 1) {
			sum.setUndefined();
			return;
		} else if (ints > MAX_RECTANGLES) {
			N = MAX_RECTANGLES;
		} else {
			N = (int) Math.round(ints);
		}

		barWidth = (bd - ad) / N;

		if (yval == null || yval.length < N) {
			yval = new double[N];
			leftBorder = new double[N];
		}
		value = new String[N];

		double ySum = 0;

		for (int i = 0; i < N; i++) {
			leftBorder[i] = ad + i * barWidth;

			geo = list1.get(i);
			if (geo.isGeoNumeric()) {
				yval[i] = ((GeoNumeric) geo).getDouble();
			} else {
				yval[i] = 0;
			}

			value[i] = kernel.format(ad + i * barWidth / 2,
					StringTemplate.defaultTemplate);

			ySum += yval[i];
		}

		// calc area of rectangles
		sum.setValue(ySum * barWidth);
		dataSize = ySum;

	}

	private void computeWithRawData() {

		if (widthGeo == null || !widthGeo.isDefined()) {
			sum.setUndefined();
			return;
		}
		barWidth = ((GeoNumeric) widthGeo).getDouble();
		if (barWidth < 0) {
			sum.setUndefined();
			return;
		}

		computeFromValueFrequencyLists(algoFreq.getValue(),
				algoFreq.getResult());

	}

	private void computeWithFrequency() {

		if (list1 == null || !list1.isDefined()) {
			sum.setUndefined();
			return;
		}
		if (!list2.isDefined() || list1.size() == 0
				|| list1.size() != list2.size()) {
			sum.setUndefined();
			return;
		}
		if (list1.size() == 0 || list1.size() != list2.size()) {
			sum.setUndefined();
			return;
		}

		if (type == TYPE_BARCHART_FREQUENCY_TABLE_WIDTH) {

			if (widthGeo == null || !widthGeo.isDefined()) {
				sum.setUndefined();
				return;
			}
			barWidth = ((GeoNumeric) widthGeo).getDouble();
			if (barWidth < 0) {
				sum.setUndefined();
				return;
			}

		} else {
			barWidth = -1;
		}

		computeFromValueFrequencyLists(list1, list2);

	}

	private void computeFromValueFrequencyLists(GeoList xList, GeoList yList) {

		if (barWidth < 0) {
			if (xList.size() > 1) {
				double x1, x2;
				if (xList.get(1).isGeoNumeric()) {
					x1 = xList.get(0).evaluateDouble();
					x2 = xList.get(1).evaluateDouble();
				} else {
					// use integers 1,2,3 ... for non-numeric data
					x1 = 1;
					x2 = 2;
				}

				if (!Double.isNaN(x1) && !Double.isNaN(x2)) {
					barWidth = x2 - x1;
				} else {
					sum.setUndefined();
					return;
				}
			} else {
				barWidth = 0.5;
			}
		}

		N = xList.size();
		if (yval == null || yval.length < N) {
			yval = new double[N];
			leftBorder = new double[N];
		}

		value = new String[N];
		for (int i = 0; i < N; i++) {
			value[i] = xList.get(i)
					.toValueString(StringTemplate.defaultTemplate);
		}

		double ySum = 0;
		double x = 0;
		if (yList.size() < N) {
			sum.setUndefined();
			return;
		}

		for (int i = 0; i < N; i++) {
			if (xList.get(i).isGeoNumeric()) {
				x = xList.get(i).evaluateDouble();
			} else {
				// use integers 1,2,3 ... to position non-numeric data
				x = i + 1;
			}

			if (!Double.isNaN(x)) {
				leftBorder[i] = x - barWidth / 2;
			} else {
				sum.setUndefined();
				return;
			}

			// frequencies
			double y = yList.get(i).evaluateDouble();
			if (!Double.isNaN(y)) {
				yval[i] = y;
				ySum += y;
			} else {
				sum.setUndefined();
				return;
			}
		}

		// set the sum
		if (isAreaSum) {
			// sum = total area
			sum.setValue(Math.abs(ySum) * barWidth);
		} else {
			// sum = total length
			sum.setValue(Math.abs(ySum));
		}
		dataSize = ySum;

	}

	/**
	 * Computes stick or step graph from a list of points
	 * 
	 * @param list
	 *            point list
	 */
	private void computeFromPointList(GeoList list) {

		N = list.size();
		if (yval == null || yval.length < N) {
			yval = new double[N];
			leftBorder = new double[N];
		}

		value = new String[N];

		double ySum = 0;

		for (int i = 0; i < N; i++) {

			GeoElement geo = list.get(i);
			Coords coords = ((GeoPointND) geo).getCoordsInD3();
			double x = coords.getX();
			if (!Double.isNaN(x)) {
				leftBorder[i] = x - barWidth / 2;
			} else {
				sum.setUndefined();
				return;
			}

			value[i] = kernel.format(x, StringTemplate.defaultTemplate);

			double y = coords.getY();
			if (!Double.isNaN(y)) {
				yval[i] = y;
				ySum += y;
			} else {
				sum.setUndefined();
				return;
			}
		}

		// sum = total length
		sum.setValue(ySum);
	}

	// ======================================================
	// Probability Distributions
	// ======================================================

	/**
	 * Prepares list1 and list2 for use with probability distribution bar charts
	 */
	private boolean prepareDistributionLists() {
		IntegerDistribution dist = null;
		int first = 0, last = 0;
		try {
			// get the distribution and the first, last list values for given
			// distribution type
			switch (type) {
			default:
				// do nothing
				break;
			case TYPE_BARCHART_BINOMIAL:
				if (!(p1geo.isDefined() && p2geo.isDefined())) {
					return false;
				}
				int n = (int) Math.round(p1.getDouble());
				double p = p2.getDouble();
				dist = new BinomialDistribution(n, p);
				first = 0;
				last = n;
				break;

			case TYPE_BARCHART_PASCAL:
				if (!(p1geo.isDefined() && p2geo.isDefined())) {
					return false;
				}
				n = (int) Math.round(p1.getDouble());
				p = p2.getDouble();
				dist = new PascalDistribution(n, p);

				first = 0;
				last = (int) Math.max(1, kernel.getXmax() + 1);
				break;
			case TYPE_BARCHART_ZIPF:
				if (!(p1geo.isDefined() && p2geo.isDefined())) {
					return false;
				}
				n = (int) Math.round(p1.getDouble());
				p = p2.getDouble();
				dist = new ZipfDistribution(n, p);

				first = 0;
				last = n;
				break;
			case TYPE_BARCHART_POISSON:
				if (!p1geo.isDefined()) {
					return false;
				}
				double lambda = p1.getDouble();
				dist = new PoissonDistribution(lambda);
				first = 0;
				last = (int) Math.max(1, kernel.getXmax() + 1);
				break;

			case TYPE_BARCHART_HYPERGEOMETRIC:
				if (!(p1geo.isDefined() && p2geo.isDefined()
						&& p3geo.isDefined())) {
					return false;
				}
				int pop = (int) p1.getDouble();
				int successes = (int) p2.getDouble();
				int sample = (int) p3.getDouble();
				dist = new HypergeometricDistribution(pop, successes,
						sample);
				first = Math.max(0, successes + sample - pop);
				last = Math.min(successes, sample);
				break;
			}

			// load class list and probability list
			loadDistributionLists(first, last, dist);
		} catch (Exception e) {
			Log.debug(e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Utility method, creates and loads list1 and list2 with classes and
	 * probabilities for the probability distribution bar charts
	 */
	private void loadDistributionLists(int first, int last,
			IntegerDistribution dist) throws Exception {
		if (list1 == null) {
			list1 = new GeoList(cons);
		} else {
			list1.clear();
		}
		if (list2 == null) {
			list2 = new GeoList(cons);
		} else {
			list2.clear();
		}
		double prob;
		double cumProb = 0;

		for (int i = first; i <= last; i++) {
			list1.addNumber(i, this);
			prob = dist.probability(i);
			cumProb += prob;
			if (isCumulative != null
					&& ((GeoBoolean) isCumulative).getBoolean()) {
				list2.addNumber(cumProb, this);
			} else {
				list2.addNumber(prob, this);
			}
		}
	}

	// ======================================================
	// Copy
	// ======================================================

	@Override
	public DrawInformationAlgo copy() {
		int N1 = this.getIntervals();
		switch (this.getType()) {
		case TYPE_BARCHART_EXPRESSION:
			return new AlgoBarChart(cons,
					(GeoNumberValue) getA().deepCopy(kernel),
					(GeoNumberValue) getB().deepCopy(kernel),
					Cloner.clone(getValues()), Cloner.clone(getLeftBorder()),
					N1);
		case TYPE_BARCHART_FREQUENCY_TABLE:
			return new AlgoBarChart(kernel.getConstruction(),
					Cloner.clone(getValues()), Cloner.clone(getLeftBorder()),
					N1);
		case TYPE_BARCHART_FREQUENCY_TABLE_WIDTH:
			return new AlgoBarChart(cons,
					(GeoNumberValue) getA().deepCopy(kernel),
					Cloner.clone(getValues()), Cloner.clone(getLeftBorder()),
					N1);
		default: // TYPE_BARCHART_RAWDATA
			return new AlgoBarChart(cons,
					(GeoNumberValue) widthGeo.deepCopy(kernel),
					Cloner.clone(getValues()), Cloner.clone(getLeftBorder()),
					N1);
		}
	}

	@Override
	public void remove() {
		super.remove();
		if (isProtectedInput()) {
			return;
		}

		removeHelperAlgos();
	}

	/**
	 * Update tooltip for a bar.
	 * 
	 * @param index
	 *            bar index
	 */
	public void setToolTipText(int index) {
		int freq = (int) yval[index];
		double percent = 100 * freq / dataSize;
		StringBuilder sb = new StringBuilder();
		sb.append(getLoc().getMenu("Value"));
		sb.append(" = ");
		sb.append(value[index]);
		sb.append("<br>");
		sb.append(getLoc().getMenu("Count"));
		sb.append(" = ");
		sb.append(kernel.format(freq, StringTemplate.defaultTemplate));
		sb.append("<br>");
		sb.append(kernel.format(percent, StringTemplate.defaultTemplate));
		sb.append("%");

		toolTipText = sb.toString();
	}

	@Override
	public ChartStyle getStyle() {
		return chartStyle;
	}
}
