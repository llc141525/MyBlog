package org.example.myblog.repository;

import org.example.myblog.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "select a from Article a left join fetch a.comments where a.users.id = ?1",
            countQuery = "select count(a) from Article a")
    List<Article> findArticlesByUserId(Long userId, Pageable pageable);
}
