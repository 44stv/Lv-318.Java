package org.uatransport.service;

import org.uatransport.entity.Comment;
import org.uatransport.entity.CommentRating;
import org.uatransport.entity.User;
import org.uatransport.exception.AlreadyVotedException;
import org.uatransport.exception.ForbiddenException;
import org.uatransport.exception.TimeExpiredException;

import java.util.List;
import java.util.Set;

public interface CommentService {

    Comment add(Comment comment, Integer transitId, Integer userId, Integer parentId);

    Comment getById(Integer id);

    List<Comment> getAllByUserId(Integer userId);

    List<Comment> getAllTopLevel(Integer transitId);

    List<Comment> getAllByParentId(Integer parentId);

    Set<User> getAllByVotedComment(Integer commentId);

    CommentRating vote(Integer commentId, Integer userId, boolean like) throws AlreadyVotedException, ForbiddenException;

    Comment addPics(String images, Integer commentId);

    Comment update(Comment newComment, Integer commentId) throws TimeExpiredException;

    void delete(Integer commendId) throws TimeExpiredException;

}
