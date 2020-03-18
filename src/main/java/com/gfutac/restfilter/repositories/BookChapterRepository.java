package com.gfutac.restfilter.repositories;

import com.gfutac.restfilter.model.BookChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookChapterRepository extends JpaRepository<BookChapter, Long>, JpaSpecificationExecutor<BookChapter> {
}
