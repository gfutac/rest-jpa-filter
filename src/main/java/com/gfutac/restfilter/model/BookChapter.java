package com.gfutac.restfilter.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "BookChapter")

@Data
@Accessors(chain = true)
@ToString
public class BookChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookChapterID")
    private long bookChapterId;

    @Column(name = "ChapterName")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    @ToString.Exclude
    private Book book;
}
