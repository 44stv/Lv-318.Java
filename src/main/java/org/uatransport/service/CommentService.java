package org.uatransport.service;

import org.uatransport.entity.Comment;
import org.uatransport.exception.TimeExpiredException;

import java.util.List;

public interface CommentService {

    Comment add(Comment comment, Integer transitId, Integer userId, Integer parentId);

    Comment getById(Integer id);

    List<Comment> getAllByTransitId(Integer transitId);

    List<Comment> getAllByUserId(Integer userId);

    Comment update(Comment newData, Integer commentId) throws TimeExpiredException; // add expired exception

    Comment delete(Integer commendId) throws TimeExpiredException; //exception

}
