package org.uatransport.service;

import org.uatransport.entity.Comment;
import org.uatransport.exception.TimeExpiredException;

import java.util.List;

public interface CommentService {

    Comment add(Comment comment, Integer transitId, Integer userId, Integer parentId);

    Comment getById(Integer id);

    List<Comment> getAllByUserId(Integer userId);

    List<Comment> getAllTopLevel(Integer transitId);

    List<Comment> getAllByParentId(Integer parentId);

    Comment update(Comment newData, Integer commentId) throws TimeExpiredException;

    void delete(Integer commendId) throws TimeExpiredException;

}
