package org.geogebra.common.kernel.scripting;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.implicit.GeoImplicitCurve;
import org.junit.Test;

public class CmdSetValueTest extends BaseUnitTest {

	@Override
	public AppCommon createAppCommon() {
		return AppCommonFactory.create3D();
	}

	@Test
	public void setValueShouldMakeImplicitCurvesUndefined() {
		GeoImplicitCurve curve = add("eq1:x^2=y^3");
		add("SetValue(eq1,?)");
		assertThat(curve, hasValue("?"));
		getApp().setXML(getApp().getXML(), true);
		GeoElement curveReloaded = lookup("eq1");
		assertThat(curveReloaded, hasValue("?"));
	}

	@Test
	public void setValueShouldKeepImplicitCurveFormInXML() {
		GeoImplicitCurve curve = add("eq1:x^2=y^3");
		add("SetValue(eq1,?x^2=?y^3)");
		String curveXMLform = "(NaN * x^(2)) = (NaN * y^(3))";
		assertThat(curve.getXML(), containsString(curveXMLform));
		getApp().setXML(getApp().getXML(), true);
		GeoElement curveReloaded = lookup("eq1");
		assertThat(curveReloaded.getXML(), containsString(curveXMLform));
	}

	@Test
	public void matrixShouldKeepForm() {
		add("k=42");
		GeoElement m1 = add("m1={{1,2},{3,4}}");
		GeoElement m2 = add("m2={{1,2+k},{k,4}}");
		add("SetValue(m1,?)");
		add("SetValue(m2,?)");
		assertEquals("{{?, ?}, {?, ?}}", m1.getDefinition(StringTemplate.defaultTemplate));
		assertEquals("{{?, ?}, {?, ?}}", m2.getDefinition(StringTemplate.defaultTemplate));
		reload();
		m1 = lookup("m1");
		m2 = lookup("m2");
		assertEquals("{{?, ?}, {?, ?}}", m1.getDefinition(StringTemplate.defaultTemplate));
		assertEquals("{{?, ?}, {?, ?}}", m2.getDefinition(StringTemplate.defaultTemplate));
		assertThat(lookup("k"), hasValue("42"));
	}

	@Test
	public void listShouldStayUndefined() {
		add("k=42");
		GeoElement l1 = add("l1={1,2,3}");
		GeoElement l2 = add("l2={1,2,k,k+2}");
		add("SetValue(l1,?)");
		add("SetValue(l2,?)");
		assertEquals("", l1.getDefinition(StringTemplate.defaultTemplate));
		assertEquals("{}", l2.getDefinition(StringTemplate.defaultTemplate));
		reload();
		l1 = lookup("l1");
		l2 = lookup("l2");
		assertThat(l1, not(isDefined()));
		assertThat(l2, not(isDefined()));
	}

	@Test
	public void numberShouldStayUndefined() {
		add("k=42");
		GeoElement n = add("n=3k+1");
		add("SetValue(n,?)");
		assertEquals("?", n.getDefinition(StringTemplate.defaultTemplate));
		reload();
		n = lookup("n");
		assertThat(n, not(isDefined()));
	}

	@Test
	public void functionShouldStayUndefined() {
		add("k=42");
		GeoElement f = add("f(x)=3k+x");
		GeoElement g = add("g(x)=3k+x");
		add("SetValue(f,?)");
		add("SetValue(g,?)");
		assertEquals("?", f.getDefinition(StringTemplate.defaultTemplate));
		assertEquals("?", g.getDefinition(StringTemplate.defaultTemplate));
		reload();
		f = lookup("f");
		g = lookup("f");
		assertThat(f, not(isDefined()));
		assertThat(g, not(isDefined()));
	}

	@Test
	public void pointShouldKeepType() {
		add("k=42");
		GeoElement pt3d = add("P=(1,2,k)");
		GeoElement pt2d = add("Q=(1,k)");
		add("SetValue(P,?)");
		add("SetValue(Q,?)");
		assertEquals("(?, ?, ?)", pt3d.getDefinition(StringTemplate.defaultTemplate));
		assertEquals("(?, ?)", pt2d.getDefinition(StringTemplate.defaultTemplate));
		reload();
		pt3d = lookup("P");
		pt2d = lookup("Q");
		assertThat(pt3d, not(isDefined()));
		assertThat(pt2d, not(isDefined()));
	}
}
