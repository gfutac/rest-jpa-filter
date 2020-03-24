package com.gfutac.test;

import com.gfutac.restfilter.filter.FilterLexer;
import com.gfutac.restfilter.filter.FilterParser;
import com.gfutac.query.filter.parser.FilterTokenType;
import com.gfutac.query.filter.parser.FilterParseTreeVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;

public class FilterGeneralTests {

    @Test
    public void test_1() {
        var expression = "someIdentifier = 35";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(FilterTokenType.EXPRESSION, res.peek().getTokenType());
        Assert.assertEquals("someIdentifier", res.peek().getFilterExpression().getOperand());
    }

    @Test
    public void test_2() {
        var expression = "someIdentifier = 35 AND otherIdentifier < 4";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(3, res.size());

        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("someIdentifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("otherIdentifier", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_3() {
        var expression = "(someIdentifier = 35 AND otherIdentifier < 4)";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(3, res.size());

        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("someIdentifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("otherIdentifier", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_4() {
        var expression = "(someIdentifier = 35 AND otherIdentifier < 4) OR thirdIdentifier > 4444";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();


        Assert.assertEquals(5, res.size());

        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("someIdentifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("otherIdentifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("thirdIdentifier", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void givenIdentifierWithDot_whenGettingOneSearchOperation_thenCorrect() {
        var expression = "some.identifier = 35";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(FilterTokenType.EXPRESSION, res.peek().getTokenType());
        Assert.assertEquals("some.identifier", res.peek().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.COMPARATOR_EQ, res.peek().getFilterExpression().getComparator());
        Assert.assertEquals(35L, res.peek().getFilterExpression().getValue());
    }

    @Test
    public void test_6() {
        var expression = "some.identifier = \"some text here!\"";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(FilterTokenType.EXPRESSION, res.peek().getTokenType());
        Assert.assertEquals("some.identifier", res.peek().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.COMPARATOR_EQ, res.peek().getFilterExpression().getComparator());
        Assert.assertEquals("some text here!", res.peek().getFilterExpression().getValue());
    }

    @Test
    public void test_7() {
        var expression = "some.identifier = \"some text here!\" AND (other.nested.identifier <= 255)";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a * (b) -> * a b
        Assert.assertEquals(3, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("some.identifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("other.nested.identifier", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_8() {
        var expression = "some.identifier ~ \"some text here!\" OR (other.nested.identifier <= 255 AND ident = 4)";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a + (b * c) -> + a * b c
        Assert.assertEquals(5, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals("some.identifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("other.nested.identifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("ident", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_9() {
        var expression = "some.identifier ~ \"some text here!\" AND other.nested.identifier <= 255 OR ident = 4";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a * b + c -> + * a b c
        Assert.assertEquals(5, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("some.identifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("other.nested.identifier", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("ident", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_10() {
        var expression = "a = 1 OR (b = 2 OR (c = 3 AND d = 4))";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a+(b+(c*d)) -> + a + b * c d
        Assert.assertEquals(7, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals("a", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals("b", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("c", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("d", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_11() {
        var expression = "a = 1 OR (b = 2 AND c = 3) OR d = 4";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a + (b * c) + d -> + + a * b c d
        Assert.assertEquals(7, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals("a", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("b", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("c", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("d", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_12() {
        var expression = "a = 1 OR b = 2 AND c = 3 OR d = 4";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        // a + b * c + d -> + + a * b c d
        Assert.assertEquals(7, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals(FilterTokenType.BINARY_OR, res.pollFirst().getTokenType());
        Assert.assertEquals("a", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals("b", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("c", res.pollFirst().getFilterExpression().getOperand());
        Assert.assertEquals("d", res.pollFirst().getFilterExpression().getOperand());
    }

    @Test
    public void test_13() {
        var expression = "some.identifier ~ \"some \\\"text here!\"";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(FilterTokenType.EXPRESSION, res.peek().getTokenType());
        Assert.assertEquals("some.identifier", res.peek().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.COMPARATOR_LIKE, res.peek().getFilterExpression().getComparator());
        Assert.assertEquals("some \"text here!", res.peek().getFilterExpression().getValue());
    }

    @Test
    public void test_14() {
        var expression = "some.date = date\"2020-03-21T00:52:40.950Z\"";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(FilterTokenType.EXPRESSION, res.peek().getTokenType());
        Assert.assertEquals("some.date", res.peek().getFilterExpression().getOperand());
        Assert.assertEquals(FilterTokenType.COMPARATOR_EQ, res.peek().getFilterExpression().getComparator());
        Assert.assertEquals(ZonedDateTime.parse("2020-03-21T00:52:40.950Z"), res.peek().getFilterExpression().getValue());
    }

    @Test
    public void test_15() {
        var expression = "some.date = date\"2020-03-21T00:52:40.950Z\" AND other.identifier = 555";
        var lexer = new FilterLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new FilterParser(tokens);

        var visitor = new FilterParseTreeVisitor();;
        visitor.visitParse(parser.parse());
        var res = visitor.getTokens();

        Assert.assertEquals(3, res.size());
        Assert.assertEquals(FilterTokenType.BINARY_AND, res.pollFirst().getTokenType());
        Assert.assertEquals(ZonedDateTime.parse("2020-03-21T00:52:40.950Z"), res.pollFirst().getFilterExpression().getValue());
        Assert.assertEquals(555L, res.pollFirst().getFilterExpression().getValue());
    }
}
