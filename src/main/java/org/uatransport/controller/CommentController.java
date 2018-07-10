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

    // @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody Comment comment,
                                                 @RequestParam(value = "transitId") Integer transitId,
                                                 @RequestParam(value = "userId") Integer userId,
                                                 @RequestParam(value = "parentId", required = false) Integer parentId) {
        Comment addedComment = commentService.add(comment, transitId, userId, parentId);
        return new ResponseEntity<>(modelMapper.map(addedComment, CommentDTO.class), HttpStatus.CREATED);
    }

    // test
    // @GetMapping("/{id}")
    // public CommentDTO getComment(@PathVariable Integer id) {
    // return modelMapper.map(commentService.getById(id), CommentDTO.class);
    // }

    @GetMapping("/{transitId}")
    public List<CommentDTO> getTransitComments(@PathVariable Integer transitId) {
        return commentService.getAllTopLevel(transitId).stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList());
    }

    @GetMapping(params = "parentId")
    public List<CommentDTO> getChildComments(@RequestParam(value = "parentId") Integer parentId) {
        return commentService.getAllByParentId(parentId).stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList());
    }

    @GetMapping(params = "userId")
    public List<CommentDTO> getUserComments(@RequestParam(value = "userId") Integer userId) {
        return commentService.getAllByUserId(userId).stream().map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
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
