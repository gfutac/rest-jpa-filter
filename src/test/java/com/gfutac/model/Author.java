package com.gfutac.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Author")
@Data
@Accessors(chain = true)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuthorID")
    private long authorId;

    @Column(name = "Name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Book> books;
}
