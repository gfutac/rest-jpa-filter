package com.gfutac.test;

import com.gfutac.query.filter.parser.*;
import com.gfutac.restfilter.filter.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import java.util.Deque;
import java.util.Objects;

public class FilterOperatorTests {

    private Deque<FilterToken> _getTokens(String expression) {
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);
        var visitor = new FilterParseTreeVisitor();
        visitor.visitParse(parser.parse());

        return visitor.getTokens();
    }

    @Test
    public void givenStringExpressionEQ_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier=35",
                "someIdentifier = 35",
                "someIdentifier= 35",
                "someIdentifier =35",
                "someIdentifier=\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_EQ, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionNE_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier!=35",
                "someIdentifier != 35",
                "someIdentifier!= 35",
                "someIdentifier !=35",
                "someIdentifier!=\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_NE, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionGT_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier>35",
                "someIdentifier > 35",
                "someIdentifier> 35",
                "someIdentifier >35",
                "someIdentifier>\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_GT, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionGE_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier>=35",
                "someIdentifier >= 35",
                "someIdentifier>= 35",
                "someIdentifier >=35",
                "someIdentifier>=\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_GE, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionLT_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier<35",
                "someIdentifier < 35",
                "someIdentifier< 35",
                "someIdentifier <35",
                "someIdentifier<\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LT, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionLE_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier<=35",
                "someIdentifier <= 35",
                "someIdentifier<= 35",
                "someIdentifier <=35",
                "someIdentifier<=\t35",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LE, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals(35L, parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionLIKE_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier~\"string expression\"",
                "someIdentifier ~ \"string expression\"",
                "someIdentifier~ \"string expression\"",
                "someIdentifier ~\"string expression\"",
                "someIdentifier~\t\"string expression\"",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LIKE, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals("string expression", parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test
    public void givenStringExpressionNLIKE_whenGetTokens_thenCorrect() {
        var expressions = new String[]{
                "someIdentifier!~\"string expression\"",
                "someIdentifier !~ \"string expression\"",
                "someIdentifier!~ \"string expression\"",
                "someIdentifier !~\"string expression\"",
                "someIdentifier!~\t\"string expression\"",
        };

        for (var expression : expressions) {
            var res = this._getTokens(expression);
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(Objects.requireNonNull(parsedExpression).getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_NLIKE, parsedExpression.getFilterExpression().getComparator());
            Assert.assertEquals("string expression", parsedExpression.getFilterExpression().getValue());
        }
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator_whenGetTokens_thenException() {

        var expression = "someIdentifier # 55";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator2_whenGetTokens_thenException() {

        var expression = "someIdentifier !# 55";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator3_whenGetTokens_thenException() {

        var expression = "someIdentifier asd 55";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator4_whenGetTokens_thenException() {

        var expression = "someIdentifier \"asd\" 55";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator5_whenGetTokens_thenException() {

        var expression = "someIdentifier 55";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }

    @Test(expected = FilterParseException.class)
    public void givenStringExpressionWithUnknownComparator7_whenGetTokens_thenException() {

        var expression = "someIdentifier";

        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(FilterParseErrorListener.INSTANCE);

        parser.parse();
    }
}
