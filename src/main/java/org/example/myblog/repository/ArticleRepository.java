package org.example.myblog.repository;

import org.example.myblog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a where a.users.id = ?1")
    Iterable<Article> findArticlesByUserId(Long userId);
}
