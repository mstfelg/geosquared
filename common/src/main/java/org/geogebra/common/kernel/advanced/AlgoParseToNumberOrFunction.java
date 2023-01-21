package org.geogebra.common.kernel.advanced;

import java.util.HashSet;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.arithmetic.SymbolicMode;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoFunctionNVar;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.common.kernel.kernelND.GeoElementND;

public class AlgoParseToNumberOrFunction extends AlgoElement {

	private final GeoText text;
	private final GeoElement result;
	private final Commands cmd;
	private final GeoList vars;
	private GeoElement[] inputForUpdateSetPropagation;
	private HashSet<GeoElement> referencedObjects;

	/**
	 * @param cons construction
	 * @param text text to be parsed
	 * @param vars function variables (for multivariable)
	 * @param cmd ParseToNumber or ParseToFunction
	 */
	public AlgoParseToNumberOrFunction(Construction cons, GeoText text,
			GeoList vars, Commands cmd) {
		super(cons);
		this.cmd = cmd;
		this.text = text;
		this.vars = vars;
		this.result = initResult();
		setInputOutput();
		compute();
	}

	private GeoElement initResult() {
		if (cmd == Commands.ParseToNumber) {
			return new GeoNumeric(cons);
		}
		return vars == null ? new GeoFunction(cons) : new GeoFunctionNVar(cons);
	}

	@Override
	protected void setInputOutput() {
		input = vars == null ? new GeoElement[] {text} : new GeoElement[] {text, vars};
		setOnlyOutput(result);
		setDependencies();
	}

	@Override
	public void compute() {
		GeoElementND num;
		AlgebraProcessor ap = kernel.getAlgebraProcessor();
		if (cmd == Commands.ParseToNumber) {
			num = ap.evaluateToNumeric(text.getTextString(), true);
			if (num != null) {
				updateReferences(num.getDefinition());
			}
		} else if (vars == null) {
			num = ap.evaluateToFunction(text.getTextString(), true);
			if (num != null) {
				if (!((GeoFunction) num).validate(false, false)) {
					num.setUndefined();
				}
				updateReferences(((GeoFunction) num).getFunctionExpression());
			}
		} else {
			vars.elements().filter(GeoElement::isGeoText).forEach(fVar ->
					cons.registerFunctionVariable(((GeoText) fVar).getTextString()));
			num = ap.evaluateToFunctionNVar(text.getTextString(),
							true, false);
			if (num != null) {
				updateReferences(((GeoFunctionNVar) num).getFunctionExpression());
			}
		}
		if (num == null) {
			result.setUndefined();
		} else {
			result.set(num);
		}
	}

	private void updateReferences(ExpressionNode definition) {
		referencedObjects = null;
		if (definition != null) {
			referencedObjects = definition.getVariables(SymbolicMode.NONE);
		}
		if (referencedObjects != null) {
			inputForUpdateSetPropagation = new GeoElement[referencedObjects.size() + 1];
			inputForUpdateSetPropagation[0] = text;
			int i = 1;
			for (GeoElement geo : referencedObjects) {
				inputForUpdateSetPropagation[i] = geo;
				i++;
				geo.addToUpdateSetOnly(this);
				if (result.hasAlgoUpdateSet()) {
					for (AlgoElement child: result.getAlgoUpdateSet()) {
						geo.addToUpdateSetOnly(child);
					}
				}
			}
		}
	}

	@Override
	public GetCommand getClassName() {
		return cmd;
	}

	@Override
	public GeoElement[] getInputForUpdateSetPropagation() {
		if (referencedObjects == null || referencedObjects.isEmpty()) {
			return input;
		}
		return inputForUpdateSetPropagation;
	}
}
