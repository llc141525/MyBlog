package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "article", ignore = true)
    @Mapping(target = "childComment", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    Comment CreateCommentRequestToComment(CreateCommentRequest request);

    @Mapping(target = "username", source = "users.username")
    @Mapping(target = "avatar", source = "users.avatarUrl")
    CommentResponse.CommentDto commentToCommentDto(Comment comment);

}
