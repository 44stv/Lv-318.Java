package org.uatransport.service;

import org.uatransport.entity.Comment;
import org.uatransport.exception.TimeExpiredException;

import java.util.List;

public interface CommentService {

    Comment add(Comment comment, Integer transitId, Integer userId, Integer parentId);

    Comment getById(Integer id);

    List<Comment> getAllByTransitId(Integer transitId);

    List<Comment> getAllByUserId(Integer userId);

    List<Comment> getAllByTransitIdAndLevel(Integer transitId, Integer level);

    Comment update(Comment newData, Integer commentId) throws TimeExpiredException;

    void delete(Integer commendId) throws TimeExpiredException;

}
