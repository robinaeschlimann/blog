package ch.hftm.service;

import ch.hftm.control.dto.*;
import ch.hftm.model.Blog;
import ch.hftm.model.Comment;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.CommentRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;
import java.util.Optional;

@Dependent
public class CommentService
{
    public static final int PAGE_SIZE = 10;
    @Inject
    BlogRepository blogRepository;

    @Inject
    SearchSession searchSession;

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

    @Transactional
    public SearchResultDto<CommentDto> searchComments(String searchText, int page)
    {
        var searchResult = searchSession.search( Comment.class )
                .where( f -> f.simpleQueryString()
                        .field( "text" )
                        .matching( searchText ) )
                .sort( SearchSortFactory::score )
                .fetch( page * PAGE_SIZE, PAGE_SIZE );

        return new SearchResultDto<>( searchResult.hits().stream()
                .map( CommentDto::toDto )
                .toList(),
                searchResult.total().hitCount() );
    }
}
