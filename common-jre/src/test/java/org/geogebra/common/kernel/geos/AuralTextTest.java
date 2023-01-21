package org.geogebra.common.kernel.geos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.AppCommon3D;
import org.geogebra.common.plugin.EventType;
import org.geogebra.common.plugin.script.GgbScript;
import org.geogebra.test.RegexpMatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AuralTextTest {

	static AppCommon3D app;

	@Before
	public void startApp() {
		app = AppCommonFactory.create3D();
	}

	private static void aural(String in, String... out) {
		GeoElementND[] geos = add(in);
		String aural = geos[0].getAuralText(new ScreenReaderBuilderDot(app.getLocalization()));
		String[] sentences = aural.split("\\.");
		Assert.assertTrue(aural.endsWith("."));
		assertEquals(out.length, sentences.length);
		for (int i = 0; i < out.length; i++) {
			if (!sentences[i].matches(".*" + out[i] + ".*")) {
				assertEquals(out[i], sentences[i]);
			}
		}
	}

	private static GeoElementND[] add(String in) {
		return app.getKernel().getAlgebraProcessor().processAlgebraCommand(in,
				true);
	}

	@Test
	public void pointAural() {
		aural("(1,1)", "Point", "1 comma 1", "arrow", "edit");
		aural("Point(xAxis)", "Point", "0 comma 0", "plus and minus", "edit");
	}

	@Test
	public void point3DAural() {
		aural("(1,1,1)", "Point", "arrow", "edit");
		aural("Point(zAxis)", "Point", "plus and minus", "edit");
	}

	@Test
	public void numberAural() {
		aural("sl=Slider(-5,5)", "Slider", "start animation", "increase",
				"decrease", "edit");
		assertEquals("Slider sl equals 0",
				((GeoNumeric) get("sl")).getAuralText());
		aural("4", "Number");
	}

	private GeoElementND get(String string) {
		return app.getKernel().lookupLabel(string);
	}

	@Test
	public void numberCaptionAural() {
		add("vec=Slider(-5,5)");
		add("SetCaption(vec,\"Vector v = %v\")");
		aural("vec", "Slider Vector v  equals  0", "start animation", "increase",
				"decrease", "edit");
		assertEquals("Slider Vector v  equals  0",
				((GeoNumeric) get("vec")).getAuralText());
	}

	@Test
	public void checkboxAural() {
		aural("checkbox()", "Checkbox", "uncheck", "edit");
		aural("false", "Checkbox", " check", "edit");
	}

	@Test
	public void dropdownAural() {
		GeoElementND[] geos = add("mylist={x,-x}");
		GeoList dropdown = (GeoList) geos[0];
		dropdown.setDrawAsComboBox(true);
		dropdown.setEuclidianVisible(true);
		dropdown.updateRepaint();
		aural("mylist", "dropdown mylist", "Element x selected", "Press space to open",
				"edit");
		assertEquals("x 1 of 2 Press up arrow and down arrow to go to different options."
				+ " Press enter to select.", dropdown.getAuralTextAsOpened());
		assertEquals("Element x selected Dropdown closed ",
				dropdown.getAuralTextForSpace());
	}

	@Test
	public void plainListAural() {
		GeoElementND[] geos = add("plain={1,2,3}");
		aural("plain", "List plain", "edit");
		GeoList plainList = (GeoList) geos[0];
		plainList.setScript(new GgbScript(app, "42"), EventType.CLICK);
		aural("plain", "List plain", "activate", "edit");
		assertNull(plainList.getAuralTextForSpace());
	}

	@Test
	public void textAural() {
		aural("LaTeX(\"b=a+\\mathbf{x^2}\")", "b equals a plus x squared", "edit");
		aural("LaTeX(\"a+\\mathbf{x^3}\")", "a plus x cubed", "edit");
		aural("LaTeX(\"a+\\mathbf{x^4}\")", "a plus x to the power of 4 end power", "edit");
		aural("LaTeX(\"a_{bcd}\")", "a start subscript bcd end subscript", "edit");
		aural("LaTeX(\"\\sqrt{x}\")", "start square root x end root", "edit");
		aural("LaTeX(\"\\sqrt[3]{x}\")", "start cube root x end root", "edit");
		aural("LaTeX(\"\\frac{x}{2}\")", "start fraction x over 2 end fraction", "edit");
		aural("LaTeX(\"\\vec{x}\")", " vector x", "edit");
		aural("LaTeX(\"\\fgcolor{red}{\\text{red text}}\")", "red text",
				"edit");
		aural("LaTeX(\"\\bgcolor{red}{\\text{not red text}}\")", "not red text",
				"edit");
		aural("TableText({{1,2,3},{3,4,5}})", "\\{\\{1,2,3\\},\\{3,4,5\\}\\}",
				"edit");
		aural("FractionText(1.5)", "start fraction 3 over 2 end fraction", "edit");
		aural("LaTeX(\"\\scalebox{0.5}{hello}\")", "hello", "edit");
		aural("LaTeX(\"\\rotatebox{90}{hello}\")", "hello", "edit");
		aural("LaTeX(\"\\textsf{textsf} \\mathsf{mathsf} \\sf{sf}\")",
				"textsfmathsfsf", "edit");
		aural("LaTeX(\"\\textit{textit} \\mathit{mathit} \\it{it}\")",
				"textitmathitit", "edit");
		aural("LaTeX(\"\\texttt{texttt} \\mathtt{mathtt} \\tt{tt}\")",
				"textttmathtttt", "edit");
		aural("LaTeX(\"\\textbf{textbf} \\mathbf{mathbf} \\bf{bf}\")",
				"textbfmathbfbf", "edit");
		aural("LaTeX(\"\\textsc{textsc} \\sc{sc}\")", "textscsc", "edit");
		aural("LaTeX(\"nothing follows: \\phantom{shouldn't be read}\")",
				"nothingfollows:", "edit");
		aural("LaTeX(\"nothing follows: \\vphantom{shouldn't be read}\")",
				"nothingfollows:", "edit");
		aural("LaTeX(\"nothing follows: \\hphantom{shouldn't be read}\")",
				"nothingfollows:", "edit");
		aural("LaTeX(\"\\xleftrightarrow{p}j\")", "pj", "edit");
		aural("LaTeX(\"\\underrightarrow{p}j\")", "pj", "edit");
		aural("LaTeX(\"\\overrightarrow{p}j\")", "pj", "edit");
		aural("LaTeX(\"\\widehat{p}\")", "p with \u0302", "edit");
		aural("LaTeX(\"\\underline{p}j\")", "pj", "edit");

	}

	@Test
	public void readLaTeXCaption() {
		GeoElementND[] pointA = add("A = (1,2)");
		pointA[0].setCaption("$ \\sqrt {x}$");
		auralWhichContainsTheOutput("A", "start square root x end root");
		GeoElementND[] pointB = add("B = (2,2)");
		pointB[0].setCaption(" $ \\text{this is my nice caption}$");
		auralWhichContainsTheOutput("B", "this is my nice caption");
		GeoElementND[] pointC = add("C = (3,3)");
		GeoElementND[] text = add("LaTeX(\"\\text{I am dynamic }\\it{text}\")");
		pointC[0].setDynamicCaption((GeoText) text[0]);
		auralWhichContainsTheOutput("C", "I am dynamic text");
	}

	@Test
	public void readComma() {
		GeoElementND[] pointA = add("A = (1,2)");
		assertEquals("open parenthesis 1 comma 2 close parenthesis",
				pointA[0].toValueString(StringTemplate.screenReaderAscii).trim());
	}

	@Test
	public void inputBoxShouldReadCaptionOrLabel() {
		GeoInputBox box = (GeoInputBox) add("myBox=InputBox()")[0];
		assertEquals("Input Box myBox", box.getAuralText().trim());
		box.setCaption("$\\frac{1}{2}$");
		assertEquals("Input Box start fraction 1 over 2 end fraction", box.getAuralText().trim());
		box.setCaption("plainText");
		assertEquals("Input Box plainText", box.getAuralText().trim());
	}

	@Test
	public void inputBoxShouldNotReadHiddenLabel() {
		GeoInputBox box = (GeoInputBox) add("myBox=InputBox()")[0];
		box.setLabelVisible(false);
		assertEquals("Input Box", box.getAuralText().trim());
		box.setCaption("$\\frac{1}{2}$");
		assertEquals("Input Box start fraction 1 over 2 end fraction", box.getAuralText().trim());
	}

	private static void auralWhichContainsTheOutput(String in, String... out) {
		GeoElementND[] geos = add(in);
		String aural = geos[0].getAuralText(new ScreenReaderBuilderDot(app.getLocalization()));
		String[] sentences = aural.split("\\.");
		Assert.assertTrue(aural.endsWith("."));
		if (out[0].matches(".*\\(.*")) {
			out[0] = out[0].replace("(", "\\(");
		}
		if (out[0].matches(".*\\).*")) {
			out[0] = out[0].replace(")", "\\)");
		}
		assertThat(sentences[0], RegexpMatch.matches(".*" + out[0] + ".*"));
	}
}
