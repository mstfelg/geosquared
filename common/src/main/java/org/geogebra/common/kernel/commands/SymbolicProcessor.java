package org.geogebra.common.kernel.commands;

import java.util.ArrayList;
import java.util.HashSet;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.Equation;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.arithmetic.ExpressionValue;
import org.geogebra.common.kernel.arithmetic.FunctionNVar;
import org.geogebra.common.kernel.arithmetic.FunctionVariable;
import org.geogebra.common.kernel.arithmetic.Inspecting;
import org.geogebra.common.kernel.arithmetic.MyList;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.arithmetic.SymbolicMode;
import org.geogebra.common.kernel.arithmetic.Traversing;
import org.geogebra.common.kernel.arithmetic.ValidExpression;
import org.geogebra.common.kernel.arithmetic.variable.Variable;
import org.geogebra.common.kernel.cas.AlgoDependentSymbolic;
import org.geogebra.common.kernel.geos.GeoDummyVariable;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.geogebra.common.main.MyError;
import org.geogebra.common.main.localization.CommandErrorMessageBuilder;
import org.geogebra.common.plugin.Operation;
import org.geogebra.common.util.SymbolicUtil;
import org.geogebra.common.util.debug.Log;

import com.google.j2objc.annotations.Weak;

/**
 * Processor for symbolic elements
 * @author Zbynek
 */
public class SymbolicProcessor {
	@Weak
	private Kernel kernel;
	@Weak
	private Construction cons;

	/**
	 * Detect assignments of the type a=f(a) so that we can treat them as
	 * equations
	 */
	private static final class RecursiveEquationFinder implements Inspecting {
		private final ExpressionValue ve;

		protected RecursiveEquationFinder(ExpressionValue ve) {
			this.ve = ve;
		}

		@Override
		public boolean check(ExpressionValue v) {
			String label = ve.wrap().getLabel();
			if (v instanceof GeoSymbolic && label != null) {
				return ((GeoSymbolic) v).getValue().inspect(this);
			}
			if (v instanceof Variable) {
				return ((Variable) v).getName().equals(label);
			}
			return v instanceof GeoDummyVariable && ((GeoDummyVariable) v)
					.getVarName().equals(label);
		}
	}

	private static final class SubExpressionEvaluator implements Traversing {
		private ExpressionValue root;
		private SymbolicProcessor processor;
		private EvalInfo evalInfo;

		public SubExpressionEvaluator(SymbolicProcessor symbolicProcessor,
				ExpressionValue root, EvalInfo evalInfo) {
			this.processor = symbolicProcessor;
			this.root = root;
			this.evalInfo = evalInfo;
		}

		@Override
		public ExpressionValue process(ExpressionValue ev) {
			if (ev instanceof Command && ev != root.unwrap()) {
				GeoSymbolic symbolic = processor.evalSymbolicNoLabel(ev, evalInfo);
				ExpressionValue outputValue = symbolic.getValue().unwrap();
				if (outputValue instanceof NumberValue
						&& !((NumberValue) outputValue).isDefined()) {
					// If processing of sub-expression failed
					return ev;
				}
				return symbolic;
			}
			if (ev instanceof GeoDummyVariable && ((GeoDummyVariable) ev)
					.getElementWithSameName() != null) {
				return ((GeoDummyVariable) ev).getElementWithSameName();
			}
			return ev;
		}
	}

	/**
	 * @param kernel kernel
	 */
	public SymbolicProcessor(Kernel kernel) {
		this.kernel = kernel;
		this.cons = kernel.getConstruction();
	}

	/**
	 * @param replaced symbolic expression
	 * @return evaluated expression
	 */
	protected GeoSymbolic doEvalSymbolicNoLabel(ExpressionNode replaced, EvalInfo info) {
		ExpressionValue expressionValue = replaced.unwrap();
		Command cmd;
		CommandDispatcher cmdDispatcher = kernel.getAlgebraProcessor().cmdDispatcher;

		try {
			if (expressionValue instanceof Command) {
				cmd = (Command) replaced.unwrap();
				if (!cmdDispatcher.isAllowedByNameFilter(Commands.valueOf(cmd.getName()))) {
					throw new MyError(kernel.getLocalization(), MyError.Errors.UnknownCommand);
				}
				if (!kernel.getGeoGebraCAS().isCommandAvailable(cmd)
						&& isInvalidArgNumberInFallback(cmd)) {
					throw buildArgNumberError(cmd);
				}
			}
			if (replaced.inspect(Inspecting.vectorDivisionFinder)) {
				throw new MyError(kernel.getLocalization(), MyError.Errors.IllegalDivision);
			}

		} catch (Exception e) {
			Log.debug(e.getMessage());
		}

		HashSet<GeoElement> vars = replaced
				.getVariables(SymbolicMode.SYMBOLIC_AV);
		ArrayList<GeoElement> noDummyVars = new ArrayList<>();
		if (vars != null) {
			for (GeoElement var : vars) {
				if (var instanceof GeoDummyVariable) {
					cons.getCASdummies()
							.add(((GeoDummyVariable) var).getVarName());
				} else if (var != null) {
					noDummyVars.add(var);
				}
			}
		}
		GeoSymbolic sym;
		if (noDummyVars.size() > 0) {
			AlgoDependentSymbolic ads =
					new AlgoDependentSymbolic(cons,
							replaced, noDummyVars, info.getArbitraryConstant(),
							info.isLabelOutput());
			sym = (GeoSymbolic) ads.getOutput(0);
		} else {
			sym = new GeoSymbolic(cons);
			sym.setArbitraryConstant(info.getArbitraryConstant());
			sym.setDefinition(replaced);
			if (info.isLabelOutput()) {
				// add to cons before computing: arbitrary constant should be *after* this in XML
				cons.addToConstructionList(sym, false);
			}
			sym.computeOutput();
		}
		SymbolicUtil.handleSolveNSolve(sym);
		return sym;
	}

	private boolean isInvalidArgNumberInFallback(Command cmd) {
		boolean oldSilent = kernel.getConstruction().isSuppressLabelsActive();
		boolean invalidArgNumber = false;
		try {
			kernel.getConstruction().setSuppressLabelCreation(true);
			EvalInfo info = new EvalInfo(false, false)
					.withScripting(false);
			kernel.getAlgebraProcessor().getCommandDispatcher()
					.processCommand(cmd.deepCopy(kernel), info);
		} catch (MyError err) {
			invalidArgNumber = err.getErrorType() == MyError.Errors.IllegalArgumentNumber;
		} catch (Throwable t) {
			// something else went wrong
		} finally {
			kernel.getConstruction().setSuppressLabelCreation(oldSilent);
		}
		return invalidArgNumber;
	}

	private MyError buildArgNumberError(Command cmd) {
		CommandErrorMessageBuilder builder =
				new CommandErrorMessageBuilder(kernel.getLocalization());
		return MyError.forCommand(kernel.getLocalization(),
				builder.buildArgumentNumberError(cmd.getName(), cmd.getArgumentNumber()),
				cmd.getName(),
				null, MyError.Errors.IllegalArgumentNumber);
	}

	protected GeoElement evalSymbolicNoLabel(ExpressionValue ve) {
		return evalSymbolicNoLabel(ve, new EvalInfo());
	}

	/**
	 * @param ve input expression
	 * @return processed geo
	 */
	protected GeoSymbolic evalSymbolicNoLabel(ExpressionValue ve, EvalInfo info) {
		ve.resolveVariables(
				new EvalInfo(false).withSymbolicMode(SymbolicMode.SYMBOLIC_AV));

		// Maybe throw exception to terminate processing
		ve.toString(StringTemplate.latexTemplateCAS);

		if (ve.unwrap() instanceof Command) {
			String cmdName = ((Command) ve.unwrap()).getName();
			if (Commands.Sequence.name().equals(cmdName)
					|| Commands.Assume.name().equals(cmdName)) {
				return doEvalSymbolicNoLabel(ve.wrap(), info);
			}
		}
		EvalInfo subInfo = new EvalInfo().withArbitraryConstant(info.getArbitraryConstant())
				.withLabels(false);
		SubExpressionEvaluator evaluator = new SubExpressionEvaluator(this, ve, subInfo);
		ExpressionNode replaced = ve.traverse(evaluator).wrap();
		if (replaced.inspect(new RecursiveEquationFinder(ve))) {
			replaced = new Equation(kernel,
					new GeoDummyVariable(cons, ve.wrap().getLabel()), replaced)
					.wrap();
			ve.wrap().setLabel(null);
		}
		return doEvalSymbolicNoLabel(replaced, info);
	}

	/**
	 * @param equ equation
	 * @param info evaluation flags
	 * @return equation or assignment
	 */
	protected ValidExpression extractAssignment(Equation equ, EvalInfo info) {
		String lhsName = extractLabel(equ, info);
		if (lhsName != null) {
			ExpressionNode lhs = equ.getLHS();
			ExpressionNode rhs = equ.getRHS();
			FunctionNVar lhsDefinition = getRedefiningFunction(equ);
			ValidExpression extractedFunction = rhs;
			FunctionVariable[] vars = null;
			if (isFunctionCall(lhs)) {
				vars = extractFunctionVariables(lhs);
			} else if (isFunctionNCall(lhs)) {
				vars = extractFunctionNVariables(lhs);
			} else if (lhsDefinition != null) {
				vars = lhsDefinition.getFunctionVariables();
			}
			if (vars != null) {
				extractedFunction = kernel.getArithmeticFactory().newFunction(rhs, vars);
			}
			extractedFunction.setLabel(lhsName);
			return extractedFunction;
		}
		return equ;
	}

	private String extractLabel(Equation equ, EvalInfo info) {
		ExpressionNode lhs = equ.getLHS();
		GeoSymbolic symbolic = getRedefinitionObject(equ);
		if (symbolic != null && !kernel.getConstruction().isFileLoading()) {
			String lhsName = ((GeoSymbolic) lhs.getLeft()).getLabelSimple();
			return info.isLabelRedefinitionAllowedFor(lhsName) ? lhsName : null;
		}
		return null;
	}

	private GeoSymbolic getRedefinitionObject(Equation equation) {
		ExpressionNode lhs = equation.getLHS();
		if (lhs.getLeft() instanceof GeoSymbolic && (isFunctionCall(lhs) || isFunctionNCall(lhs))) {
			return (GeoSymbolic) lhs.getLeft();
		}
		return null;
	}

	private boolean isFunctionCall(ExpressionNode value) {
		return value.getOperation() == Operation.FUNCTION
				&& value.getRight() instanceof FunctionVariable;
	}

	private boolean isFunctionNCall(ExpressionNode value) {
		if (value.getOperation() == Operation.FUNCTION_NVAR
				&& value.getRight() instanceof MyList) {
			MyList args = (MyList) value.getRight();
			for (int i = 0; i < args.size(); i++) {
				ExpressionValue arg = args.getItem(i);
				if (!(arg instanceof FunctionVariable)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private FunctionVariable[] extractFunctionVariables(ExpressionNode value) {
		assert isFunctionCall(value);
		return new FunctionVariable[]{(FunctionVariable) value.getRight()};
	}

	private FunctionVariable[] extractFunctionNVariables(ExpressionNode value) {
		assert isFunctionNCall(value);
		MyList args = (MyList) value.getRight();
		FunctionVariable[] vars = new FunctionVariable[args.size()];
		for (int i = 0; i < args.size(); i++) {
			ExpressionValue arg = args.getItem(i);
			if (arg instanceof FunctionVariable) {
				vars[i] = (FunctionVariable) arg;
			} else {
				vars[i] =
						new FunctionVariable(kernel, arg.toString(StringTemplate.defaultTemplate));
			}
		}
		return vars;
	}

	private FunctionNVar getRedefiningFunction(Equation equation) {
		GeoSymbolic symbolic = getRedefinitionObject(equation);
		if (symbolic != null) {
			ExpressionNode node = symbolic.getDefinition();
			ExpressionValue value = node != null ? node.unwrap() : null;
			if (value instanceof FunctionNVar) {
				return (FunctionNVar) value;
			}
		}
		return null;
	}

	/**
	 * @param expression expression
	 * @param info evaluation flags
	 */
	public void updateLabel(ValidExpression expression, EvalInfo info) {
		if (expression.unwrap() instanceof Equation) {
			String lhsName = extractLabel((Equation) expression.unwrap(), info);
			if (lhsName != null) {
				expression.setLabel(lhsName);
			}
		}
	}

}
