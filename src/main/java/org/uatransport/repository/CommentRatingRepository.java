package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uatransport.entity.CommentRating;

import java.util.List;

public interface CommentRatingRepository extends JpaRepository<CommentRating, Integer> {

    List<CommentRating> findCommentRatingByCommentIdAndUserId(Integer commentId, Integer userId);

    List<CommentRating> findAllByCommentId(Integer commentId);
}
