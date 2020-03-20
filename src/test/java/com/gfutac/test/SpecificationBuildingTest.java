package com.gfutac.test;

import com.gfutac.Application;
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
        var specification = new GenericSpecification<Author>(new SearchOperation("name", SearchOperation.EQ, "J.R.R Tolkien"));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchOperation("authorId", SearchOperation.EQ, 1));
        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenNonexistingId_whenGettingListOfAuthors_thenCorrect() {
        var specification = new GenericSpecification<Author>(new SearchOperation("authorId", SearchOperation.EQ, 100));
        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void givenNameWithBuilder_whenGettingListOfAuthors_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder
                .with("name", SearchOperation.EQ, "J.R.R Tolkien")
                .with("authorId", SearchOperation.EQ, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("J.R.R Tolkien")));
    }

    @Test
    public void givenBookIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", SearchOperation.EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenAuthorIdGreaterThanWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Author>();

        var specification = builder
                .with("authorId", SearchOperation.GT, 1L)
                .build();

        var result = this.authorRepository.findAll(specification);

        Assert.assertEquals(5, result.size());
    }

    @Test
    public void givenBookIdAndAuthorIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", SearchOperation.EQ, 1L)
                .with("author.authorId", SearchOperation.EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings: The Fellowship of the Ring")));
    }

    @Test
    public void givenBookNameAndPublishingYearWithBuilder_WhenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("name", SearchOperation.EQ, "The Lord Of The Rings")
                .with("publishingDate", SearchOperation.GT, LocalDateTime.of(1955, 1, 1, 0, 0))
                .build();

        var result = this.bookRepository.findAll(specification);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("The Lord Of The Rings: The Return of the King", result.get(0).getName());
    }

    @Test
    public void givenDateRanges_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", SearchOperation.GT, LocalDateTime.of(1996, 1, 1, 0, 0))
                .with("publishingDate", SearchOperation.LT, LocalDateTime.of(2001, 1, 1, 0, 0))
                .with("author.name", SearchOperation.EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("A Game of Thrones", "A Clash of Kings", "A Storm of Swords").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthor_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", SearchOperation.EQ, null)
                .with("author.name", SearchOperation.EQ, "George Martin")
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of("The Winds of Winter", "A Dream of Spring").contains(i.getName())));
    }

    @Test
    public void givenNullDates_WhenGettingListOfBooksAndAuthorWithPublishDate_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("publishingDate", SearchOperation.NE, null)
                .with("author.name", SearchOperation.EQ, "George Martin")
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
                .with("publishingDate", SearchOperation.NE, null)
                .with("author.name", SearchOperation.EQ, "George Martin")
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
                .with("book.name", SearchOperation.EQ, "A Game of Thrones")
                .with("book.author.name", SearchOperation.EQ, "George Martin")
                .build();

        var result = this.bookChapterRepository.findAll(specification);

        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.stream().allMatch(i -> List.of(
                "Prologue",
                "Bran I",
                "Catelyn I",
                "Daenerys I").contains(i.getName())));

    }
}
