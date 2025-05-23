package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment CreateCommentRequestToComment(CreateCommentRequest request);

    CommentResponse.CommentDto commentToCommentDto(Comment comment);

}
