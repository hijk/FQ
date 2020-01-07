package fq.bool;

import java.util.ArrayList;

import fq.matrix.RankMatrix;

public class BAQVisitor extends BoolExpressionBaseVisitor<Object> {

	private final RankMatrix rm;

	public BAQVisitor(RankMatrix rm) {
		this.rm = rm;
	}

	@Override
	public Object visitParse(BoolExpressionParser.ParseContext ctx) {
		return super.visit(ctx.expression());
	}
	
	@Override
	public Object visitBoolExpr(BoolExpressionParser.BoolExprContext ctx){
		return Boolean.valueOf(ctx.getText());
	}

	@Override
	public Object visitNumExpr(BoolExpressionParser.NumExprContext ctx) {
		return Integer.parseInt(ctx.DECIMAL().getText());
	}

	@Override
	public Object visitIdExpr(BoolExpressionParser.IdExprContext ctx)  {
		String text = ctx.IDENTIFIER().getText();
		String id = text.substring(5, text.length()-1);
		return Integer.parseInt(id);
	}

	@Override
	public Object visitNegateExpr(BoolExpressionParser.NegateExprContext ctx) {
		//TODO:no implemented yet.
		return !((Boolean)this.visit(ctx.expression()));
	}

	@Override
	public Object visitParentExpr(BoolExpressionParser.ParentExprContext ctx) {
		return super.visit(ctx.expression());
	}

	@Override
	public Object visitCompareExpr(BoolExpressionParser.CompareExprContext ctx) {
		Integer id = (Integer)this.visit(ctx.left);
		Integer rank = (Integer)this.visit(ctx.right);
		QueryResult r = new QueryResult();
		
		if (ctx.cp.EQ() != null) {
			r.result.add(new Index(id,rank));
			return r;
		}
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
		throw new RuntimeException("not implemented: comparator operator " + ctx.cp.getText());
	}

	@Override
	public Object visitBinaryExpr(BoolExpressionParser.BinaryExprContext ctx) {
		QueryResult l = (QueryResult)visit(ctx.left);
		QueryResult r = (QueryResult)visit(ctx.right);
		if (ctx.op.AND() != null) {
			l.result.retainAll(r.result);
			return l;
		}
		else if (ctx.op.OR() != null) {
			l.result.removeAll(r.result);
			l.result.addAll(r.result);
			return l;
		}
		throw new RuntimeException("not implemented: binary operator " + ctx.op.getText());
	}

}