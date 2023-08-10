package ch.hftm.service;

import ch.hftm.control.dto.CommentDto;
import ch.hftm.model.Blog;
import ch.hftm.model.Comment;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.CommentRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Dependent
public class CommentService
{
    @Inject
    BlogRepository blogRepository;

    @Inject
    CommentRepository commentRepository;

    public List<CommentDto> getComments(long blogId ) throws IllegalArgumentException
    {
        var blog = blogRepository.findById(blogId);

        if( blog == null )
        {
            throw new IllegalArgumentException( "Blog with id " + blogId + " not found" );
        }

        return blog.getComments().stream()
                .map( CommentDto::toDto )
                .toList();
    }

    @Transactional
    public void addComment(long blogId, CommentDto commentDto ) throws IllegalArgumentException
    {
        var blog = blogRepository.findById(blogId);

        if (blog == null) {
            throw new IllegalArgumentException("Blog with id " + blogId + " not found");
        }

        var comment = CommentDto.toComment(commentDto);

        blog.getComments().add(comment);

        blogRepository.persist(blog);
    }
}
