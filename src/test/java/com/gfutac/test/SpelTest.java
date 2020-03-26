package com.gfutac.test;

import com.gfutac.Application;
import com.gfutac.model.Author;
import com.gfutac.query.filter.parser.FilterExpression;
import com.gfutac.query.filter.parser.FilterTokenType;
import com.gfutac.query.filter.specification.FilterSpecification;
import com.gfutac.query.filter.specification.FilterSpecificationBuilder;
import com.gfutac.repositories.AuthorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@Transactional
@Rollback
@ActiveProfiles("integration")
@Profile("integration")
public class SpelTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private StandardEvaluationContext filterExpressionContext;

    @Test
    public void spel_test1() {
        var parser = new SpelExpressionParser();
        var expr = parser.parseExpression("(a + b) * c");

        Object root = new Object() {
            public Specification<Author> a = new FilterSpecificationBuilder<Author>().buildOne("authorId", FilterTokenType.COMPARATOR_EQ, 1L);
            public Specification<Author> b = new FilterSpecificationBuilder<Author>().buildOne("name", FilterTokenType.COMPARATOR_EQ, "George Martin");
            public Specification<Author> c = new FilterSpecificationBuilder<Author>().buildOne("authorId", FilterTokenType.COMPARATOR_EQ, "2");
        };

        FilterSpecification<Author> specification = expr.getValue(filterExpressionContext, root, FilterSpecification.class);
        var res = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(2L, res.get(0).getAuthorId());
    }

    @Test
    public void spel_test2() {
        var parser = new SpelExpressionParser();

        var root = new Object() {
            public FilterSpecification<Object> fe (String field, String comparator, Object value) {
                return new FilterSpecificationBuilder<>().buildOne(field, FilterTokenType.valueOf(comparator), value);
            };
        };

        var expr = parser.parseExpression("fe('authorId', 'COMPARATOR_EQ', 1)");

        FilterSpecification<Author> specification = expr.getValue(filterExpressionContext, root, FilterSpecification.class);
        var res = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(1L, res.get(0).getAuthorId());
    }
}
