
CREATE TABLE IF NOT EXISTS Author(
    AuthorID bigint auto_increment primary key,
    Name varchar(64)
);

CREATE TABLE IF NOT EXISTS Book(
    BookID bigint auto_increment primary key,
    Name varchar(128),
    PublishingDate datetime,
    IsFavorite bit,
    AuthorID bigint references Author(AuthorID)
);

CREATE TABLE IF NOT EXISTS BookChapter(
    BookChapterID bigint auto_increment primary key,
    ChapterName varchar(128),
    BookID bigint references Book(BookID)
);

INSERT INTO Author (AuthorID, Name) VALUES (1, N'J.R.R Tolkien');
INSERT INTO Author (AuthorID, Name) VALUES (2, N'George Martin');
INSERT INTO Author (AuthorID, Name) VALUES (3, N'Edgar Allan Poe');
INSERT INTO Author (AuthorID, Name) VALUES (4, N'Arthur Conan Doyle');
INSERT INTO Author (AuthorID, Name) VALUES (5, N'Mark Twain');
INSERT INTO Author (AuthorID, Name) VALUES (6, N'Jane Austen');
INSERT INTO Author (AuthorID, Name) VALUES (7, N'John Doe');

INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (1, N'The Lord Of The Rings: The Fellowship of the Ring', 1, CAST(N'1954-07-29 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (2, N'The Lord Of The Rings: The Two Towers, 2nd Edition', 1, CAST(N'1954-11-11 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (3, N'The Lord Of The Rings: The Return of the King', 1, CAST(N'1955-10-20 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (4, N'A Game of Thrones', 2, CAST(N'1996-08-01 00:00:00.000' AS DateTime), 1);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (5, N'A Clash of Kings', 2, CAST(N'1998-11-16 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (6, N'A Storm of Swords', 2, CAST(N'2000-08-08 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (7, N'A Feast for Crows', 2, CAST(N'2005-10-17 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (8, N'A Dance with Dragons', 2, CAST(N'2011-07-12 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (9, N'The Winds of Winter', 2, NULL, 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (10, N'A Dream of Spring', 2, NULL, 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (11, N'The Black Cat', 3, CAST(N'1843-08-19 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (12, N'The Masque of the Red Death', 3, CAST(N'1842-05-01 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (13, N'The Murders in the Rue Morgue', 3, CAST(N'1841-04-01 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (14, N'The Hound of the Baskervilles', 4, CAST(N'1902-01-01 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (15, N'A Horse''s Tale', 5, CAST(N'1906-01-01 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (16, N'The Adventures of Tom Sawyer', 5, CAST(N'1876-01-01 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (17, N'Pride and Prejudice', 6, CAST(N'1813-01-28 00:00:00.000' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (18, N'A book with double quote: "quoted text"', 7, CAST(N'2020-03-23 23:03:08.420' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (19, N'', 7, CAST(N'2020-03-25 23:15:08.140' AS DateTime), 0);
INSERT INTO Book (BookID, Name, AuthorID, PublishingDate, IsFavorite) VALUES (20, NULL, 7, CAST(N'2020-03-25 23:15:11.310' AS DateTime), 0);

INSERT INTO BookChapter (BookChapterID, ChapterName, BookID) VALUES (1, N'Chapter 1', 1);
INSERT INTO BookChapter (BookChapterID, ChapterName, BookID) VALUES (2, N'Prologue', 4);
INSERT INTO BookChapter (BookChapterID, ChapterName, BookID) VALUES (3, N'Bran I', 4);
INSERT INTO BookChapter (BookChapterID, ChapterName, BookID) VALUES (4, N'Catelyn I', 4);
INSERT INTO BookChapter (BookChapterID, ChapterName, BookID) VALUES (5, N'Daenerys I', 4);