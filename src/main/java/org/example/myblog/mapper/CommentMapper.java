package org.example.myblog.mapper;

import org.example.myblog.dto.request.CreateCommentRequest;
import org.example.myblog.dto.response.CommentResponse;
import org.example.myblog.model.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment CreateCommentRequestToComment(CreateCommentRequest request);

    List<CommentResponse> commentsToCommentResponse(List<Comment> comments);
}
