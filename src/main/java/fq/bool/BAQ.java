//package fq.bool;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.CommonTokenStream;
//
//public class BAQ {
//	public static void main(String[] args) throws Exception {
//
//		Map<String, Object> variables = new HashMap<String, Object>() {{
//			put("A", true);
//			put("a", true);
//			put("B", false);
//			put("b", false);
//			put("C", 42.0);
//			put("c", 42.0);
//			put("D", -999.0);
//			put("d", -1999.0);
//			put("E", 42.001);
//			put("e", 142.001);
//			put("F", 42.001);
//			put("f", 42.001);
//			put("G", -1.0);
//			put("g", -1.0);
//		}};
//
//		String[] expressions = {
//				"1 > 2",
//				"1 >= 1.0",
//				"TRUE = FALSE",
//				"FALSE = FALSE",
//				"A OR B",
//				"B",
//				"A = B",
//				"c = C",
//				"E > D",
//				"B OR (c = B OR (A = A AND c = C AND E > D))",
//				"(A = a OR B = b OR C = c AND ((D = d AND E = e) OR (F = f AND G = g)))"
//		};
//
//		for (String expression : expressions) {
//			BoolExpressionLexer lexer = new BoolExpressionLexer(new ANTLRInputStream(expression));
//			BoolExpressionParser parser = new BoolExpressionParser(new CommonTokenStream(lexer));
//			Object result = new EvalVisitor(variables).visit(parser.parse());
//			System.out.printf("%-70s -> %s\n", expression, result);
//		}
//	}
//}
//
//class EvalVisitor extends BoolExpressionBaseVisitor<Object> {
//
//	private final Map<String, Object> variables;
//
//	public EvalVisitor(Map<String, Object> variables) {
//		this.variables = variables;
//	}
//
//	public Object visitParse(BoolExpressionParser.ParseContext ctx) {
//		return super.visit(ctx.expression());
//	}
//
//	public Object visitDecimalExpression(BoolExpressionParser.NumExprContext ctx) {
//		return Double.valueOf(ctx.DECIMAL().getText());
//	}
//
//	public Object visitIdentifierExpression(BoolExpressionParser.IdExprContext ctx) {
//		return variables.get(ctx.IDENTIFIER().getText());
//	}
//
//	public Object visitNotExpression(BoolExpressionParser.NegateExprContext ctx) {
//		return !((Boolean)this.visit(ctx.expression()));
//	}
//
//	public Object visitParenExpression(BoolExpressionParser.ParenExpContext ctx) {
//		return super.visit(ctx.expression());
//	}
//
//	public Object visitComparatorExpression(BoolExpressionParser.CompareExprContext ctx) {
//		if (ctx.cp.EQ() != null) {
//			return this.visit(ctx.left).equals(this.visit(ctx.right));
//		}
//		else if (ctx.cp.LE() != null) {
//			return asDouble(ctx.left) <= asDouble(ctx.right);
//		}
//		else if (ctx.cp.GE() != null) {
//			return asDouble(ctx.left) >= asDouble(ctx.right);
//		}
//		else if (ctx.cp.LT() != null) {
//			return asDouble(ctx.left) < asDouble(ctx.right);
//		}
//		else if (ctx.cp.GT() != null) {
//			return asDouble(ctx.left) > asDouble(ctx.right);
//		}
//		throw new RuntimeException("not implemented: comparator operator " + ctx.op.getText());
//	}
//
//	public Object visitBinaryExpression(BoolExpressionParser.BinaryExprContext ctx) {
//		if (ctx.op.AND() != null) {
//			return asBoolean(ctx.left) && asBoolean(ctx.right);
//		}
//		else if (ctx.op.OR() != null) {
//			return asBoolean(ctx.left) || asBoolean(ctx.right);
//		}
//		throw new RuntimeException("not implemented: binary operator " + ctx.op.getText());
//	}
//
//	public Object visitBoolExpression(BoolExpressionParser.BoolContext ctx) {
//		return Boolean.valueOf(ctx.getText());
//	}
//	
//	public Object visitExpression(BoolExpressionParser.BoolContext ctx) {
//		return Boolean.valueOf(ctx.getText());
//	}
//	
//	public Object visitBoolExpression(BoolExpressionParser.BoolContext ctx) {
//		return Boolean.valueOf(ctx.getText());
//	}
//
//	private boolean asBoolean(BoolExpressionParser.PrimaryContext ctx) {
//		return (boolean)visit(ctx);
//	}
//
//	private double asDouble(BoolExpressionParser.PrimaryContext ctx) {
//		return (double)visit(ctx);
//	}
//}