package com.gfutac.test;

import com.gfutac.Application;
import com.gfutac.restfilter.filter.GenericSpecification;
import com.gfutac.restfilter.filter.GenericSpecificationBuilder;
import com.gfutac.restfilter.filter.SearchCriteria;
import com.gfutac.restfilter.model.Author;
import com.gfutac.restfilter.model.Book;
import com.gfutac.restfilter.repositories.AuthorRepository;
import com.gfutac.restfilter.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@Transactional
@Rollback
@ActiveProfiles("local")
@Profile("local")
public class FilterIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void givenName_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchCriteria("name", SearchCriteria.EQ, "J.R.R Tolkien"));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchCriteria("authorId", SearchCriteria.EQ, 1));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenNonexistingId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchCriteria("authorId", SearchCriteria.EQ, 100));
        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void givenNameWithBuilder_whenGettingListOfAuthors_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();
        // where name = JRR Tolkien and authorId = 1
        var specification = builder
                .with("name", SearchCriteria.EQ, "J.R.R Tolkien")
                .with("authorId", SearchCriteria.EQ, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenBookIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", SearchCriteria.EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenAuthorIdGreaterThanWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder
                .with("authorId", SearchCriteria.GT, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(5, result.size());
    }

    @Test
    public void givenBookIdAndAuthorIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", SearchCriteria.EQ, 1L)
                .with("author.authorId", SearchCriteria.EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenBookNameAndPublishingYearWithBuilder_WhenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("name", SearchCriteria.EQ, "The Lord Of The Rings")
                .with("publishingDate", SearchCriteria.GT, LocalDateTime.of(1955, 1, 1, 0, 0))
                .build();

        var result = this.bookRepository.findAll(specification);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("The Lord Of The Rings: The Return of the King", result.get(0).getName());
    }

    @Test
    public void givenDateRanges_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", SearchCriteria.GT, LocalDateTime.of(1996, 1, 1, 0, 0))
                .with("publishingDate", SearchCriteria.LT, LocalDateTime.of(2001, 1, 1, 0, 0))
                .with("author.name", SearchCriteria.EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("A Game of Thrones", "A Clash of Kings", "A Storm of Swords").contains(i.getName())));
    }



}
