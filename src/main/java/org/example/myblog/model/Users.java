package org.example.myblog.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Builder
//@Table(name = "[user]")
public class Users {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @ToString.Exclude
    private List<Article> articles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String avatarUrl;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void addArticle(Article article) {
        articles.add(article);
        article.setUsers(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setUsers(null);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUsers(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUsers(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Users users = (Users) o;
        return getId() != null && Objects.equals(getId(), users.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
