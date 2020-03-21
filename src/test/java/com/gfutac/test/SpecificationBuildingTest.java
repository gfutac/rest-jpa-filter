package com.gfutac.test;

import com.gfutac.Application;
import com.gfutac.restfilter.filter.FilterTokenType;
import com.gfutac.restfilter.filter.GenericSpecification;
import com.gfutac.restfilter.filter.GenericSpecificationBuilder;
import com.gfutac.restfilter.filter.SearchOperation;
import com.gfutac.restfilter.model.Author;
import com.gfutac.restfilter.model.Book;
import com.gfutac.restfilter.model.BookChapter;
import com.gfutac.restfilter.repositories.AuthorRepository;
import com.gfutac.restfilter.repositories.BookChapterRepository;
import com.gfutac.restfilter.repositories.BookRepository;
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
@ActiveProfiles("local")
@Profile("local")
public class SpecificationBuildingTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookChapterRepository bookChapterRepository;

    @Test
    public void givenName_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchOperation("name", FilterTokenType.COMPARATOR_EQ.getValue(), "J.R.R Tolkien"));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchOperation("authorId", FilterTokenType.COMPARATOR_EQ.getValue(), 1));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenNonexistingId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchOperation("authorId", FilterTokenType.COMPARATOR_EQ.getValue(), 100));
        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void givenNameWithBuilder_whenGettingListOfAuthors_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder
                .with("name", FilterTokenType.COMPARATOR_EQ.getValue(), "J.R.R Tolkien")
                .with("authorId", FilterTokenType.COMPARATOR_EQ.getValue(), 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenBookIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", FilterTokenType.COMPARATOR_EQ.getValue(), 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenAuthorIdGreaterThanWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder
                .with("authorId", FilterTokenType.COMPARATOR_GT.getValue(), 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(5, result.size());
    }

    @Test
    public void givenBookIdAndAuthorIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", FilterTokenType.COMPARATOR_EQ.getValue(), 1L)
                .with("author.authorId", FilterTokenType.COMPARATOR_EQ.getValue(), 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenBookNameAndPublishingYearWithBuilder_WhenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("name", FilterTokenType.COMPARATOR_LIKE.getValue(), "The Lord Of The Rings")
                .with("publishingDate", FilterTokenType.COMPARATOR_GT.getValue(), LocalDateTime.of(1955, 1, 1, 0, 0))
                .build();

        var result = this.bookRepository.findAll(specification);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("The Lord Of The Rings: The Return of the King", result.get(0).getName());
    }

    @Test
    public void givenDateRanges_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", FilterTokenType.COMPARATOR_GT.getValue(), LocalDateTime.of(1996, 1, 1, 0, 0))
                .with("publishingDate", FilterTokenType.COMPARATOR_LT.getValue(), LocalDateTime.of(2001, 1, 1, 0, 0))
                .with("author.name", FilterTokenType.COMPARATOR_EQ.getValue(), "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("A Game of Thrones", "A Clash of Kings", "A Storm of Swords").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", FilterTokenType.COMPARATOR_EQ.getValue(), null)
                .with("author.name", FilterTokenType.COMPARATOR_EQ.getValue(), "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("The Winds of Winter", "A Dream of Spring").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthorWithPublishDate_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", FilterTokenType.COMPARATOR_NE.getValue(), null)
                .with("author.name", FilterTokenType.COMPARATOR_EQ.getValue(), "George Martin")
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
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", FilterTokenType.COMPARATOR_NE.getValue(), null)
                .with("author.name", FilterTokenType.COMPARATOR_EQ.getValue(), "George Martin")
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
        var builder = new GenericSpecificationBuilder<BookChapter>();

        var specification = builder
                .with("book.name", FilterTokenType.COMPARATOR_EQ.getValue(), "A Game of Thrones")
                .with("book.author.name", FilterTokenType.COMPARATOR_EQ.getValue(), "George Martin")
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
    public void givenStringAsFilter_WhenGettingAuthorById_thenCorrect() throws Exception {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder.build("authorId = 1");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("J.R.R Tolkien", result.get(0).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorByIdOrName_thenCorrect() throws Exception {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder.build("authorId = 1 OR name = \"George Martin\"");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("J.R.R Tolkien", result.get(0).getName());
        Assert.assertEquals("George Martin", result.get(1).getName());
    }

    @Test
    public void givenStringAsFilter_WhenGettingAuthorByNameLike_thenCorrect() throws Exception {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder.build("name ~ \"George\"");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("George Martin", result.get(0).getName());
    }

    @Test // ?search=(name ~ "George" OR name ~ "Edgar") AND authorId < 4
    public void givenStringAsFilter_WhenGettingAuthorsByNameLikeAndIdLesserThan_thenCorrect() throws Exception {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder.build("(name ~ \"George\" OR name ~ \"Edgar\") AND authorId < 4");

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
                "George Martin",
                "Edgar Allan Poe").contains(i.getName())));
    }
}
