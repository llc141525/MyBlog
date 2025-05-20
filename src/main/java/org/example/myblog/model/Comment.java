package org.example.myblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;

    LocalDateTime createTime;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude // 防止重复引用
    @JoinColumn(name = "parent_comment_id", nullable = true)
    Comment parentComment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentComment")
    @OrderBy("createTime asc")
    @JsonManagedReference
    @ToString.Exclude
    List<Comment> childComment;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;


    // comment 的双向关系维护方法
    public void addChildComment(Comment comment) {
        childComment.add(comment);
        comment.setParentComment(this);
    }

    public void removeChildComment(Comment comment) {
        childComment.remove(comment);
        comment.setParentComment(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Comment comment = (Comment) o;
        return getId() != null && Objects.equals(getId(), comment.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
