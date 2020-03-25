package com.gfutac.test;

import com.gfutac.Application;
import com.gfutac.model.Author;
import com.gfutac.model.Book;
import com.gfutac.model.BookChapter;
import com.gfutac.query.filter.parser.FilterExpression;
import com.gfutac.query.filter.parser.FilterTokenType;
import com.gfutac.query.filter.specification.FilterSpecification;
import com.gfutac.query.filter.specification.FilterSpecificationBuilder;
import com.gfutac.query.filter.specification.SpecificationBuildingException;
import com.gfutac.repositories.AuthorRepository;
import com.gfutac.repositories.BookChapterRepository;
import com.gfutac.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@Transactional
@Rollback
@ActiveProfiles("integration")
@Profile("integration")
public class SpecificationBuildingTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookChapterRepository bookChapterRepository;

    @Test
    public void givenName_whenGettingListOfAuthors_thenCorrect() {
        var specification = new FilterSpecification<Author>(new FilterExpression("name", FilterTokenType.COMPARATOR_EQ, "J.R.R Tolkien"));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new FilterSpecification<Author>(new FilterExpression("authorId", FilterTokenType.COMPARATOR_EQ, 1));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenNonexistingId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new FilterSpecification<Author>(new FilterExpression("authorId", FilterTokenType.COMPARATOR_EQ, 100));
        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void givenNameWithBuilder_whenGettingListOfAuthors_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder
                .and("name", FilterTokenType.COMPARATOR_EQ, "J.R.R Tolkien")
                .and("authorId", FilterTokenType.COMPARATOR_EQ, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenBookIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("bookId", FilterTokenType.COMPARATOR_EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenAuthorIdGreaterThanWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder
                .and("authorId", FilterTokenType.COMPARATOR_GT, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(6, result.size());
    }

    @Test
    public void givenBookIdAndAuthorIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("bookId", FilterTokenType.COMPARATOR_EQ, 1L)
                .and("author.authorId", FilterTokenType.COMPARATOR_EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenBookNameAndPublishingYearWithBuilder_WhenGettingListOfBooks_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("name", FilterTokenType.COMPARATOR_LIKE, "The Lord Of The Rings")
                .and("publishingDate", FilterTokenType.COMPARATOR_GT, LocalDateTime.of(1955, 1, 1, 0, 0))
                .build();

        var result = this.bookRepository.findAll(specification);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("The Lord Of The Rings: The Return of the King", result.get(0).getName());
    }

    @Test
    public void givenDateRanges_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("publishingDate", FilterTokenType.COMPARATOR_GT, LocalDateTime.of(1996, 1, 1, 0, 0))
                .and("publishingDate", FilterTokenType.COMPARATOR_LT, LocalDateTime.of(2001, 1, 1, 0, 0))
                .and("author.name", FilterTokenType.COMPARATOR_EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("A Game of Thrones", "A Clash of Kings", "A Storm of Swords").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("publishingDate", FilterTokenType.COMPARATOR_EQ, null)
                .and("author.name", FilterTokenType.COMPARATOR_EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("The Winds of Winter", "A Dream of Spring").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthorWithPublishDate_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("publishingDate", FilterTokenType.COMPARATOR_NE, null)
                .and("author.name", FilterTokenType.COMPARATOR_EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(5, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
                "A Game of Thrones",
                "A Clash of Kings",
                "A Storm of Swords",
                "A Feast for Crows",
                "A Dance with Dragons").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthorWithPublishDateSecondPage_thenCorrect() {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder
                .and("publishingDate", FilterTokenType.COMPARATOR_NE, null)
                .and("author.name", FilterTokenType.COMPARATOR_EQ, "George Martin")
                .build();

        var page = 2;
        var pageable = PageRequest.of(page - 1, 3);

        var result = this.bookRepository.findAll(specification, pageable).toList();

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
//                "A Game of Thrones",
//                "A Clash of Kings",
//                "A Storm of Swords",
                "A Feast for Crows",
                "A Dance with Dragons").contains(i.getName())));
    }

    @Test
    public void givenAuthorName_WhenGettingListOfBookChapters_thenCorrect() {
        var builder = new FilterSpecificationBuilder<BookChapter>();

        var specification = builder
                .and("book.name", FilterTokenType.COMPARATOR_EQ, "A Game of Thrones")
                .and("book.author.name", FilterTokenType.COMPARATOR_EQ, "George Martin")
                .build();

        var result = this.bookChapterRepository.findAll(specification);

        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
                "Prologue",
                "Bran I",
                "Catelyn I",
                "Daenerys I").contains(i.getName())));

    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorById_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder.build("authorId = 1");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("J.R.R Tolkien", result.get(0).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorByIdOrName_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder.build("authorId = 1 OR name = \"George Martin\"");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("J.R.R Tolkien", result.get(0).getName());
        Assert.assertEquals("George Martin", result.get(1).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorByNameLike_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder.build("name ~ \"George\"");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("George Martin", result.get(0).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorsByNameLikeAndIdLesserThan_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();

        var specification = builder.build("(name ~ \"George\" OR name ~ \"Edgar\") AND authorId < 4");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
                "George Martin",
                "Edgar Allan Poe").contains(i.getName())));
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorsByNameLikeWithSingleQuote_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder.build("name ~ \"Horse's\"");

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("A Horse's Tale", result.get(0).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorsByNameLikeWithDuobleQuote_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder.build("name ~ \"\\\"quote\"");

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("A book with double quote: \"quoted text\"", result.get(0).getName());
    }

    @Test(expected = SpecificationBuildingException.class)
    public void givenMalformedStringAsFilter_WhenGettingAuthorsByNameLike_thenException() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();
        builder.build("name ~~ \"George\"");
    }

    @Test(expected = SpecificationBuildingException.class)
    public void givenMalformedStringAsFilter_WhenGettingAuthorsByNameLikeAndId_thenException() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Author>();
        builder.build("name ~~ \"George\" AND authorId = 2");
    }

    @Test
    public void givenNullDatesInString_WhenGettingListOfBooksAndAuthor_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder.build("publishingDate = NULL and author.name = \"George Martin\"");

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("The Winds of Winter", "A Dream of Spring").contains(i.getName())));
    }

    @Test
    public void givenEmptyString_WhenGettingListOfBookChapters_thenCorrect() throws SpecificationBuildingException {
        var builder = new FilterSpecificationBuilder<Book>();

        var specification = builder.build("name = NULL or name = \"\"");

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(19L, 20L).contains(i.getBookId())));
    }
}
