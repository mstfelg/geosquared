package org.geogebra.cas;

import static org.geogebra.common.BaseUnitTest.hasValue;
import static org.geogebra.test.matcher.IsEqualStringIgnoreWhitespaces.equalToIgnoreWhitespaces;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Locale;

import org.geogebra.common.kernel.GeoGebraCasInterface;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.Traversing.CommandCollector;
import org.geogebra.common.kernel.cas.CasTestJsonCommon;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.geos.GeoCasCell;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.desktop.headless.AppDNoGui;
import org.geogebra.desktop.main.LocalizationD;
import org.geogebra.test.CASTestLogger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.himamis.retex.editor.share.util.Unicode;

public class ArbitraryConstIntegrationTest {

	static GeoGebraCasInterface cas;
	static Kernel kernel;
	static AppDNoGui app;

	/**
	 * Logs all tests which don't give the expected but a valid result.
	 */
	static CASTestLogger logger;

	/**
	 * Create app and cas.
	 */
	@BeforeClass
	public static void setupCas() {
		app = new AppDNoGui(new LocalizationD(3), false);

		// Set language to something else than English to test automatic
		// translation.
		app.setLanguage(Locale.GERMANY);
		// app.fillCasCommandDict();

		kernel = app.getKernel();
		cas = kernel.getGeoGebraCAS();
		logger = new CASTestLogger();

		// Setting the general timeout to 9 seconds. Feel free to change this.
		kernel.getApplication().getSettings().getCasSettings()
				.setTimeoutMilliseconds(9000);
	}

	/**
	 * Handles the logs about test warnings.
	 */
	@AfterClass
	public static void handleLogs() {
		logger.handleLogs();
	}

	/**
	 * Before every test: Clear the construction list to make sure there is
	 * nothing already defined.
	 */
	@Before
	public void beforeTest() {
		kernel.clearConstruction(true);
	}

	/**
	 * @param input
	 *            The input.
	 * @param expectedResult
	 *            The expected result.
	 * @param validResults
	 *            Valid, but undesired results.
	 */
	private static void ta(String input, String expectedResult,
			String... validResults) {
		String result = getOutput(cellFromInput(input));

		assertThat(result, equalToIgnoreWhitespaces(logger, input,
				expectedResult, validResults));
	}

	@Test
	public void solveODE_0() {
		ta("SolveODE[y'=7y^2x^3]", "y = 4 / (4 * c_{1} - 7 * x^(4))");
	}

	@Test
	public void solveODE_1() {
		ta("SolveODE[y''-5y'+6y=0]", "y = c_{1} * " + Unicode.EULER_STRING
				+ "^(3 * x) + c_{2} * " + Unicode.EULER_STRING + "^(2 * x)");
	}

	@Test
	public void solveODE_2() {
		ta("SolveODE[y'=5y-3]",
				"y = c_{1} * " + Unicode.EULER_STRING + "^(5*x) + 3 / 5");
	}

	@Test
	public void solveODE_3() {
		ta("SolveODE[y'+y=10]",
				"y = c_{1} * " + Unicode.EULER_STRING + "^(-x) + 10");
	}

	@Test
	public void solveODE_4() {
		ta("SolveODE[y' = (3 - y) / 2]",
				"y = c_{1} * " + Unicode.EULER_STRING + "^((-x)/ 2) + 3");
	}

	@Test
	public void solveODE_5() {
		ta("SolveODE[y' = -2 + y]",
				"y = c_{1} * " + Unicode.EULER_STRING + "^(x) + 2");
	}

	@Test
	public void solveODE_6() {
		ta("SolveODE[y' = y(y - 2)]",
				"y = -2 / (c_{1} *" + Unicode.EULER_STRING + "^(2*x) - 1)");
	}

	@Test
	public void solveODE_7() {
		ta("SolveODE[y''=y]", "y = c_{1} *" + Unicode.EULER_STRING
				+ "^(x) + c_{2} *" + Unicode.EULER_STRING + "^(-x)");
	}

	@Test
	public void solveODE_8() {
		ta("SolveODE[2y''+y'-y=0]",
				"y = c_{1} *" + Unicode.EULER_STRING + "^(-x) + c_{2} *"
						+ Unicode.EULER_STRING + "^(x/2)",
				"y = c_{1} *" + Unicode.EULER_STRING + "^(x / 2) + c_{2} *"
						+ Unicode.EULER_STRING + "^(-x)");
	}

	@Test
	public void solveODE_9() {
		ta("SolveODE[y''-5y=0]",
				"y = c_{1} *" + Unicode.EULER_STRING + "^(sqrt(5) * x) + c_{2} *"
						+ Unicode.EULER_STRING + "^(-sqrt(5) * x)");
	}

	@Test
	public void solveODE_10() {
		ta("SolveODE[2y''+3y'=0]",
				"y = c_{1} *" + Unicode.EULER_STRING + "^(-3 * x / 2) + c_{2}");
	}

	@Test
	public void solveODE_11() {
		ta("SolveODE[y''+2y' + 101y = 0]",
				"y=c_{1} * cos(10 * x) *" + Unicode.EULER_STRING
						+ "^(-x) + c_{2} * " + Unicode.EULER_STRING
						+ "^(-x)* sin(10 * x)");
	}

	@Test
	public void solveODE_12() {
		ta("SolveODE[y'' + 4y' + 4y = 0]", "y=c_{1} * x * " + Unicode.EULER_STRING
				+ "^(-2 * x) + c_{2} * " + Unicode.EULER_STRING + "^(-2 * x)");
	}

	@Test
	public void solveODE_13() {
		ta("SolveODE[y''=2y]",
				"y = c_{1} * " + Unicode.EULER_STRING + "^(sqrt(2) * x) + c_{2} * "
						+ Unicode.EULER_STRING + "^(-sqrt(2) * x)");
	}

	@Test
	public void arbIntShouldHaveIncrement1() {
		cellFromInput("Invert(sin(x))");
		GeoNumeric k1 = (GeoNumeric) kernel.lookupLabel("k_{1}");
		assertEquals(1.0, k1.getAnimationStep(), 0.0);
		assertFalse(k1.isAutoStep());
	}

	/**
	 * @param input
	 *            The input.
	 * @param inputUpdate
	 *            The input to update the cell.
	 * @param expectedResult
	 *            The expected result.
	 * @param validResults
	 *            Valid, but undesired results.
	 */
	private static void casCellupdate(String input, String inputUpdate,
			String expectedResult, String... validResults) {
		String result;

		try {
			GeoCasCell f = cellFromInput(input);

			f.setInput(inputUpdate);
			f.computeOutput();

			result = getOutput(f);
		} catch (Throwable t) {
			String sts = CasTestJsonCommon.stacktrace(t);

			result = t.getClass().getName() + ":" + t.getMessage() + sts;
		}

		assertThat(result, equalToIgnoreWhitespaces(logger, input,
				expectedResult, validResults));
	}

	private static GeoCasCell cellFromInput(String input) {
		GeoCasCell f = new GeoCasCell(kernel.getConstruction());
		kernel.getConstruction().addToConstructionList(f, false);

		f.setInput(input);
		f.computeOutput();
		return f;
	}

	@Test
	public void arbConst_Integration_1() {
		casCellupdate("Integral[x]", "SolveODE[2y''+3y'=0]",
				"y = c_{1} *" + Unicode.EULER_STRING + "^(-3 * x / 2) + c_{2}");
	}

	@Test
	public void arbConst_Integration_2() {
		casCellupdate("SolveODE[y''+9y=0]", "SolveODE[y''+4y=0]",
				"y = c_{1} * cos(2 * x) + c_{2} * sin(2 * x)");
	}

	@Test
	public void casCellLatexShouldSHowName() {
		GeoCasCell cell = cellFromInput("SolveODE(2x)");
		assertEquals("\\mathbf{y = c_{1} + x^{2}}", cell.getLaTeXOutput());
	}

	/**
	 * Note that first cell is updated after second cell.
	 * 
	 * @param cell1Input
	 *            The input of first cell.
	 * @param cell2Input
	 *            The input of second cell.
	 * @param cell1InputUpdate
	 *            The input to update first cell.
	 * @param cell2InputUpdate
	 *            The input to update second cell.
	 * @param expectedResult1
	 *            The expected result for the first cell.
	 * @param expectedResult2
	 *            The expected result for the second cell.
	 */
	private static void casCellupdate2(String cell1Input, String cell2Input,
			String cell1InputUpdate, String cell2InputUpdate,
			String expectedResult1, String expectedResult2) {
		String result1, result2;

		try {
			GeoCasCell f1 = new GeoCasCell(kernel.getConstruction());
			kernel.getConstruction().addToConstructionList(f1, false);

			f1.setInput(cell1Input);
			f1.computeOutput();

			GeoCasCell f2 = new GeoCasCell(kernel.getConstruction());
			kernel.getConstruction().addToConstructionList(f2, false);

			f2.setInput(cell2Input);
			f2.computeOutput();

			f2.setInput(cell2InputUpdate);
			f2.computeOutput();

			result2 = getOutput(f2);

			f1.setInput(cell1InputUpdate);
			f1.computeOutput();

			result1 = getOutput(f1);

		} catch (Throwable t) {
			String sts = CasTestJsonCommon.stacktrace(t);
			result1 = t.getClass().getName() + ":" + t.getMessage() + sts;
			result2 = t.getClass().getName() + ":" + t.getMessage() + sts;
		}
		String[] alternatives = new String[0];
		assertThat(result1, equalToIgnoreWhitespaces(logger, cell1Input,
				expectedResult1, alternatives));
		assertThat(result2, equalToIgnoreWhitespaces(logger, cell2Input,
				expectedResult2, alternatives));
	}

	private static String getOutput(GeoCasCell f2) {
		HashSet<Command> commands = new HashSet<>();

		f2.getInputVE().traverse(CommandCollector.getCollector(commands));
		boolean includesNumericCommand = false;
		if (!commands.isEmpty()) {
			for (Command cmd : commands) {
				String cmdName = cmd.getName();
				// Numeric used
				includesNumericCommand = includesNumericCommand
						|| ("Numeric".equals(cmdName)
								&& cmd.getArgumentNumber() > 1);
			}
		}

		return f2.getValue() != null
				? f2.getValue()
						.toString(includesNumericCommand
								? StringTemplate.testNumeric
								: StringTemplate.testTemplate)
				: f2.getOutput(StringTemplate.testTemplate);

	}

	/**
	 * Before redefinition: c_1 in first row, c_2 in second.
	 * 
	 * After redefine 2: c_2 and c_3 in second
	 * 
	 * After redefine 1: c_1 and c_4 in first
	 */
	@Test
	public void arbConst_Integration_3() {
		casCellupdate2("Integral[x]", "Integral[sin(x)]", "SolveODE[y''+9y=0]",
				"SolveODE[y''+4y=0]", "y = c_{1} * cos(3 * x) + c_{4} * sin(3 * x)",
				"y = c_{2} * cos(2 * x) + c_{3} * sin(2 * x)");
	}

	/**
	 * Add first cell, update it, add second cell, update it.
	 * 
	 * @param cell1Input
	 *            The input of first cell.
	 * @param cell2Input
	 *            The input of second cell.
	 * @param cell1InputUpdate
	 *            The input to update first cell.
	 * @param cell2InputUpdate
	 *            The input to update second cell.
	 * @param expectedResult1
	 *            The expected result for cell 1.
	 * @param expectedResult2
	 *            The expected result for cell 2.
	 */
	private static void casCellupdate3(String cell1Input, String cell2Input,
			String cell1InputUpdate, String cell2InputUpdate,
			String expectedResult1, String expectedResult2) {
		String result1, result2;

		try {
			GeoCasCell f1 = new GeoCasCell(kernel.getConstruction());
			kernel.getConstruction().addToConstructionList(f1, false);

			f1.setInput(cell1Input);
			f1.computeOutput();

			f1.setInput(cell1InputUpdate);
			f1.computeOutput();

			result1 = getOutput(f1);

			GeoCasCell f2 = new GeoCasCell(kernel.getConstruction());
			kernel.getConstruction().addToConstructionList(f2, false);

			f2.setInput(cell2Input);
			f2.computeOutput();

			f2.setInput(cell2InputUpdate);
			f2.computeOutput();

			result2 = getOutput(f2);

		} catch (Throwable t) {
			String sts = CasTestJsonCommon.stacktrace(t);
			result1 = t.getClass().getName() + ":" + t.getMessage() + sts;
			result2 = t.getClass().getName() + ":" + t.getMessage() + sts;
		}

		assertThat(result1, equalToIgnoreWhitespaces(logger, cell1Input,
				expectedResult1));
		assertThat(result2, equalToIgnoreWhitespaces(logger, cell2Input,
				expectedResult2));
	}

	@Test
	/**
	 * First cell: c_1 before redefine, c_1 and c_2 after
	 * 
	 * Second cell: c_3 before redefine, c_3 and c_4 after
	 */
	public void arbConst_Integration_4() {
		casCellupdate3("Integral[x]", "Integral[sin(x)]", "SolveODE[y''+9y=0]",
				"SolveODE[y''+4y=0]", "y = c_{1} * cos(3 * x) + c_{2} * sin(3 * x)",
				"y = c_{3} * cos(2 * x) + c_{4} * sin(2 * x)");
	}

	@Test
	public void constMulti() {
		ta("Simplify[SolveODE[ y*ln(2)]]", "y = 2^(x) * c_{1}");
		assertEquals(1, app.getGgbApi().getValue("c_{1}"), 0.01);
		ta("SolveODE[ x]", "y = c_{2} + 1 / 2 * x^(2)");
		assertEquals(0, app.getGgbApi().getValue("c_2"), 0.01);
	}

	@Test
	public void reloadTest() {
		ta("f(x):=sin(x)", "sin(x)");
		ta("F(x):=Integral[sin(x)]", "-cos(x) + c_{1}");
		for (int i = 0; i < 2; i++) {
			app.getKernel().getAlgebraProcessor()
					.processAlgebraCommand("P=(1,1)", true);
			app.getKernel().getAlgebraProcessor()
					.processAlgebraCommand("Q=(1,1)", true);
			app.getGgbApi().undo();
			app.getGgbApi().undo();
		}
		assertEquals(app.getGgbApi().getValueString("$2", true),
				"F(x):=-cos(x) + c_{1}");
		String base64 = app.getGgbApi().getBase64();
		app.getKernel().clearConstruction(true);
		app.getGgbApi().setBase64(base64);
		assertEquals(app.getGgbApi().getValueString("$2", true),
				"F(x):=-cos(x) + c_{1}");
	}

	@Test
	public void apTest() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		GeoFunction gf = ap.evaluateToFunction("x+c_1", true, true);
		assertEquals(
				gf.getFunctionExpression().toString(StringTemplate.xmlTemplate),
				"x + arbconst(1)");
	}

	@Test
	public void reloadAppTest() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		ap.processAlgebraCommand("F(x)=Integral(sin(x)*sin(x), x)", false);
		checkAfterReload("F", "F(x) = -1 / 4 sin(2x) + 1 / 2 x");
	}

	private static void checkAfterReload(String name, String... valid) {
		String current = kernel.lookupLabel("F")
				.toString(StringTemplate.defaultTemplate);
		assertIn(valid, current);
		app.setXML(app.getXML(), true);
		current = kernel.lookupLabel("F")
				.toString(StringTemplate.defaultTemplate);
		assertIn(valid, current);

	}

	private static void assertIn(String[] valid, String current) {
		for (int i = 0; i < valid.length - 1; i++) {
			if (valid[i].equals(current)) {
				return;
			}
		}
		assertEquals(valid[valid.length - 1], current);

	}

	@Test
	public void reloadAppTestXY() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		ap.processAlgebraCommand("F(x,y)=Integral(sin(x)*sin(y-x), x)", false);
		checkAfterReload("F", "F(x, y) = 1 / 4 sin(2x - y) - 1 / 2 x cos(y)",
				"F(x, y) = -1 / 2 x cos(y) + 1 / 4 sin(2x - y)");
	}

	@Test
	public void reloadAppTest2Var() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		ap.processAlgebraCommand("F(t,x)=Integral(sin(x)*sin(t-x), x)", false);
		checkAfterReload("F", "F(t, x) = -1 / 4 sin(t - 2x) - 1 / 2 x cos(t)",
				"F(t, x) = -1 / 2 x cos(t) - 1 / 4 sin(t - 2x)");
	}

	@Test
	public void solveOdeShouldNotCreateConstantsForTrig() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		GeoElementND[] f = ap.processAlgebraCommand("F:SolveODE(-1-y^2, (1,2))", false);
		assertArrayEquals(new String[]{"F"}, app.getGgbApi().getAllObjectNames());
		assertThat(f[0], hasValue("tan(0π - x + tan"
				+ Unicode.SUPERSCRIPT_MINUS_ONE_STRING + "(2) + 1)"));
	}

	@Test
	public void shouldKeepValueAfterReload() {
		AlgebraProcessor ap = app.getKernel().getAlgebraProcessor();
		ap.processAlgebraCommand("f:=SolveODE(x)", false);
		ap.processAlgebraCommand("SetValue(c_1,3)", false);
		app.setXML(app.getXML(), true);
		assertThat(kernel.lookupLabel("f"), hasValue("3 + 1 / 2 x"
				+ Unicode.SUPERSCRIPT_2));
	}

}
