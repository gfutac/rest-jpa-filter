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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@Transactional
@Rollback
@ActiveProfiles("local")
@Profile("local")
public class filterIntegrationTest {

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
        var specification = new GenericSpecification<Author>(new SearchCriteria("authorId", SearchCriteria.EQ, 2));
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

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings 2, 3rd Edition")));
    }

    @Test
    public void givenBookIdAndAuthorIdWithBuilder_whenGettingListOfBooks_thenCorrect() {
        var builder = new GenericSpecificationBuilder<Book>();

        var specification = builder
                .with("bookId", SearchCriteria.EQ, 1L)
                .with("author.authorId", SearchCriteria.EQ, 1L)
                .build();

        var result = this.bookRepository.findAll(specification);

        Assert.assertTrue(result.stream().anyMatch(i -> i.getName().equals("The Lord Of The Rings 2, 3rd Edition")));
    }
}
