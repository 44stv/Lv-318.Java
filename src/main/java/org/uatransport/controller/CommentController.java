package org.uatransport.controller;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Comment;
import org.uatransport.entity.dto.CommentDTO;
import org.uatransport.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ModelMapper modelMapper;
    private final CommentService commentService;

//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{transitId}")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment,
                                              @PathVariable Integer transitId,
                                              @RequestParam(value = "userId") Integer userId,
                                              @RequestParam(value = "parentId", defaultValue = "") Integer parentId) {
        return new ResponseEntity<>(commentService.add(comment, transitId, userId, parentId), HttpStatus.CREATED);
    }

    //test
//    @GetMapping("/{id}")
//    public Comment getComment(@PathVariable Integer id) {
//        return commentService.getById(id);
//    }

//    @GetMapping("/{transitId}")
//    public List<Comment> getTransitComments(@PathVariable Integer transitId) {
//        return commentService.getAllTopLevel(transitId);
//    }

    @GetMapping("/{transitId}")
    public List<CommentDTO> getTransitComments(@PathVariable Integer transitId) {
        return commentService.getAllTopLevel(transitId).stream()
            .map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList());
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment, @PathVariable Integer commentId) {
        Comment updatedComment = commentService.update(comment, commentId);

        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.delete(commentId);
    }
}
