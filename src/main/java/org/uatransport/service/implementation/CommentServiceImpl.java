package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Comment;
import org.uatransport.exception.TimeExpiredException;
import org.uatransport.repository.CommentRepository;
import org.uatransport.repository.TransitRepository;
import org.uatransport.repository.UserRepository;
import org.uatransport.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TransitRepository transitRepository;
    private final UserRepository userRepository;

    private static final int MAX_COMMENT_LEVEL = 5;

    @Override
    @Transactional
    public Comment add(Comment comment, Integer transitId, Integer userId, Integer parentId) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment object should not be null");
        }

        if (parentId != null) {
            Comment parentComment = getById(parentId);

            int level = parentComment.commentLevel();

            comment.setParentComment(level < MAX_COMMENT_LEVEL ? parentComment : parentComment.getParentComment());
        }

        comment.setCreatedDate(LocalDateTime.now());
        comment.setTransit(transitRepository.getOne(transitId));
        comment.setUser(userRepository.getOne(userId));

        commentRepository.save(comment);

        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getById(Integer id) {
        return commentRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllByTransitId(Integer transitId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllByUserId(Integer userId) {
        return null;
    }

    @Override
    @Transactional
    public Comment update(Comment newData, Integer commentId) {




        throw new TimeExpiredException(String.format("Time for updating comment with id '%s' expired", commentId));
//        return ;
    }

    @Override
    @Transactional
    public Comment delete(Integer commentId) {

        throw new TimeExpiredException(String.format("Time for updating comment with id '%s' expired", commentId));
//        return ;
    }
}
