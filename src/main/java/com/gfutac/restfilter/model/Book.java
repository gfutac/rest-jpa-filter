package com.gfutac.restfilter.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

@Table(name = "Book")

@Data
@Accessors(chain = true)
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID")
    private long bookId;

    @Column(name = "Name")
    private String name;

    @Column(name = "PublishingDate")
    private LocalDateTime publishingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    @ToString.Exclude
    private Author author;
}
