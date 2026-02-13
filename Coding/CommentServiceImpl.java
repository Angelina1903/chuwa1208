package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post buildPost(long id) {
        Post p = new Post();
        p.setId(id);
        return p;
    }

    private Comment buildComment(long id, Post post, String name, String email, String body) {
        Comment c = new Comment();
        c.setId(id);
        c.setPost(post);
        c.setName(name);
        c.setEmail(email);
        c.setBody(body);
        return c;
    }

    private CommentDto buildDto(long id, String name, String email, String body) {
        CommentDto dto = new CommentDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setBody(body);
        return dto;
    }

    @Test
    void createComment_success_shouldSetPostAndSave() {
        long postId = 1L;

        Post post = buildPost(postId);
        CommentDto requestDto = buildDto(0L, "n1", "e1@a.com", "body1");

        Comment mappedEntity = buildComment(0L, null, "n1", "e1@a.com", "body1");
        Comment savedEntity = buildComment(10L, post, "n1", "e1@a.com", "body1");
        CommentDto responseDto = buildDto(10L, "n1", "e1@a.com", "body1");

        when(modelMapper.map(requestDto, Comment.class)).thenReturn(mappedEntity);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, CommentDto.class)).thenReturn(responseDto);

        CommentDto actual = commentService.createComment(postId, requestDto);

        assertEquals(responseDto, actual);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(captor.capture());
        assertNotNull(captor.getValue().getPost());
        assertEquals(postId, captor.getValue().getPost().getId());
    }

    @Test
    void createComment_postNotFound_shouldThrow() {
        long postId = 99L;
        CommentDto requestDto = buildDto(0L, "n1", "e1@a.com", "body1");

        when(modelMapper.map(requestDto, Comment.class)).thenReturn(new Comment());
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.createComment(postId, requestDto));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentsByPostId_success_shouldMapList() {
        long postId = 1L;

        Comment c1 = buildComment(1L, buildPost(postId), "a", "a@a.com", "bodyA");
        Comment c2 = buildComment(2L, buildPost(postId), "b", "b@a.com", "bodyB");

        CommentDto d1 = buildDto(1L, "a", "a@a.com", "bodyA");
        CommentDto d2 = buildDto(2L, "b", "b@a.com", "bodyB");

        when(commentRepository.findByPostId(postId)).thenReturn(Arrays.asList(c1, c2));
        when(modelMapper.map(c1, CommentDto.class)).thenReturn(d1);
        when(modelMapper.map(c2, CommentDto.class)).thenReturn(d2);

        List<?> result = commentService.getCommentsByPostId(postId);

        assertEquals(2, result.size());
        assertEquals(d1, result.get(0));
        assertEquals(d2, result.get(1));
    }

    @Test
    void getCommentsByPostId_empty_shouldReturnEmptyList() {
        long postId = 1L;

        when(commentRepository.findByPostId(postId)).thenReturn(Collections.emptyList());

        List<?> result = commentService.getCommentsByPostId(postId);

        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), eq(CommentDto.class));
    }

    @Test
    void getCommentById_success_commentBelongsToPost() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Comment comment = buildComment(commentId, post, "n", "e@a.com", "body");
        CommentDto expectedDto = buildDto(commentId, "n", "e@a.com", "body");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(expectedDto);

        CommentDto actual = commentService.getCommentById(postId, commentId);

        assertEquals(expectedDto, actual);
    }

    @Test
    void getCommentById_postNotFound_shouldThrow() {
        long postId = 1L;
        long commentId = 10L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(postId, commentId));

        verify(commentRepository, never()).findById(anyLong());
    }

    @Test
    void getCommentById_commentNotFound_shouldThrow() {
        long postId = 1L;
        long commentId = 10L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(buildPost(postId)));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(postId, commentId));
    }

    @Test
    void getCommentById_commentNotBelongToPost_shouldThrowBlogApiException() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Post anotherPost = buildPost(2L);
        Comment comment = buildComment(commentId, anotherPost, "n", "e@a.com", "body");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(postId, commentId));

        verify(modelMapper, never()).map(any(Comment.class), eq(CommentDto.class));
    }

    @Test
    void updateComment_success_shouldUpdateFieldsAndSave() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Comment existing = buildComment(commentId, post, "old", "old@a.com", "oldBody");

        CommentDto req = buildDto(0L, "new", "new@a.com", "newBody");
        Comment updated = buildComment(commentId, post, "new", "new@a.com", "newBody");
        CommentDto expectedDto = buildDto(commentId, "new", "new@a.com", "newBody");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existing));
        when(commentRepository.save(any(Comment.class))).thenReturn(updated);
        when(modelMapper.map(updated, CommentDto.class)).thenReturn(expectedDto);

        CommentDto actual = commentService.updateComment(postId, commentId, req);

        assertEquals(expectedDto, actual);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(captor.capture());
        assertEquals("new", captor.getValue().getName());
        assertEquals("new@a.com", captor.getValue().getEmail());
        assertEquals("newBody", captor.getValue().getBody());
    }

    @Test
    void updateComment_commentNotBelongToPost_shouldThrowAndNotSave() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Post anotherPost = buildPost(2L);
        Comment existing = buildComment(commentId, anotherPost, "old", "old@a.com", "oldBody");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existing));

        assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(postId, commentId, buildDto(0L, "n", "e", "b")));

        verify(commentRepository, never()).save(any(Comment.class));
        verify(modelMapper, never()).map(any(Comment.class), eq(CommentDto.class));
    }

    @Test
    void deleteComment_success_shouldDelete() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Comment existing = buildComment(commentId, post, "n", "e", "b");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existing));

        commentService.deleteComment(postId, commentId);

        verify(commentRepository, times(1)).delete(existing);
    }

    @Test
    void deleteComment_commentNotBelongToPost_shouldThrowAndNotDelete() {
        long postId = 1L;
        long commentId = 10L;

        Post post = buildPost(postId);
        Post anotherPost = buildPost(2L);
        Comment existing = buildComment(commentId, anotherPost, "n", "e", "b");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existing));

        assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(postId, commentId));

        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
