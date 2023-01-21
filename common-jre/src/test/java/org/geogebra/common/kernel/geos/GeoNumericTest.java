package org.geogebra.common.kernel.geos;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.main.settings.config.AppConfigCas;
import org.junit.Test;

public class GeoNumericTest extends BaseUnitTest {

	@Test
	public void euclidianShowabilityOfOperationResult() {
		GeoNumeric numeric = addAvInput("4+6");
		assertThat(numeric.isEuclidianShowable(), is(false));
	}

	@Test
	public void testNumericIsNotDrawableInCas() {
		getApp().setConfig(new AppConfigCas());
		GeoNumeric numeric = addAvInput("2");
		assertThat(numeric.isEuclidianShowable(), is(false));
	}

	@Test
	public void testSliderIsVisibleInEv() {
		GeoNumeric numeric = new GeoNumeric(getConstruction());
		GeoNumeric.setSliderFromDefault(numeric, false);
		assertThat(numeric.isEuclidianShowable(), is(true));
	}

	@Test
	public void getLaTeXDescriptionRHS() {
		GeoNumeric numeric = addAvInput("1/2");

		String descriptionRHS =
				numeric.getLaTeXDescriptionRHS(
						true, StringTemplate.latexTemplate);
		assertThat(descriptionRHS, equalTo("0.5"));

		descriptionRHS =
				numeric.getLaTeXDescriptionRHS(
						false, StringTemplate.latexTemplate);
		assertThat(descriptionRHS, equalTo("\\frac{1}{2}"));
	}

	@Test
	public void getLaTeXAlgebraDescription() {
		GeoNumeric numeric = addAvInput("a = 1/2");

		String description =
				numeric.getLaTeXAlgebraDescription(
						true, StringTemplate.latexTemplate);
		assertThat(description, equalTo("a\\, = \\,0.5"));

		description =
				numeric.getLaTeXAlgebraDescription(
						false, StringTemplate.latexTemplate);
		assertThat(description, equalTo("a\\, = \\,\\frac{1}{2}"));
	}

	@Test
	public void testCopy() {
		GeoNumeric numeric = addAvInput("1");
		numeric.setDrawable(true, false);
		GeoNumeric copy = numeric.copy();
		assertThat(copy.isDrawable, is(true));
	}

	@Test
	public void sliderTagShouldStayInXmlAfterSetUndefined() {
		GeoNumeric slider = add("sl=Slider(0,1,.1)");
		assertThat(slider.isEuclidianVisible(), is(true));
		add("SetValue(sl,?)");
		slider.setDefinition(new ExpressionNode(getKernel(), Double.NaN));
		assertThat(slider.isEuclidianVisible(), is(false));
		String xml = slider.getXML();
		assertThat(xml, containsString("<slider"));
	}

	@Test
	public void undefinedSliderShouldBeSliderableAfterReload() {
		GeoNumeric slider = add("sl=Slider(0,1,.1)");
		add("SetValue(sl,?)");
		slider.setDefinition(new ExpressionNode(getKernel(), Double.NaN));
		reload();
		GeoElement reloaded = lookup("sl");
		add("SetValue(sl,.5)");
		assertThat(slider.isEuclidianVisible(), is(false));
		assertThat(((GeoNumeric) reloaded).isSliderable(), is(true));
	}
}