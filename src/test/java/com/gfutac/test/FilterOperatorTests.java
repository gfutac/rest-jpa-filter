package com.gfutac.test;

import com.gfutac.restfilter.filter.FilterExpressionVisitor;
import com.gfutac.restfilter.filter.FilterLexer;
import com.gfutac.restfilter.filter.FilterParser;
import com.gfutac.restfilter.filter.FilterTokenType;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

public class FilterOperatorTests {

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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_EQ, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_NE, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_GT, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_GE, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LT, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LE, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_LIKE, parsedExpression.getFilterExpression().getComparison());
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
            var lexer = new FilterLexer(CharStreams.fromString(expression));
            var tokens = new CommonTokenStream(lexer);
            var parser = new FilterParser(tokens);
            var visitor = new FilterExpressionVisitor();

            visitor.visitParse(parser.parse());
            var res = visitor.getTokens();
            var parsedExpression = res.peekFirst();

            Assert.assertEquals(1, res.size());
            Assert.assertNotNull(parsedExpression.getFilterExpression());
            Assert.assertEquals("someIdentifier", parsedExpression.getFilterExpression().getOperand());
            Assert.assertEquals(FilterTokenType.COMPARATOR_NLIKE, parsedExpression.getFilterExpression().getComparison());
            Assert.assertEquals("string expression", parsedExpression.getFilterExpression().getValue());
        }
    }
}
