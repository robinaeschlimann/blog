package ch.hftm.service;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.control.dto.BlogDtoSearchWrapper;
import ch.hftm.model.Blog;
import ch.hftm.model.IBlog;
import ch.hftm.repository.BlogRepository;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Dependent
public class BlogService {
    @Inject
    BlogRepository blogRepository;

    @Inject
    SearchSession searchSession;

//    @Inject
//    @Channel("blog")
//    Emitter<BlogMessage> emitter;

    @Inject
    Logger logger;

    public List<BlogDto> getBlogs() {
        var blogs = blogRepository.listAll();
        logger.info("Returning " + blogs.size() + " blogs");
        return blogs.stream()
                .map(BlogDto::toDto)
                .toList();
    }

    @Transactional
    @Blocking
    public long addBlog(BlogDto blogDto) {
        logger.info("Adding blog " + blogDto.getTitle());

        var blog = BlogDto.toBlog( blogDto );

        blogRepository.persist(blog);

        return blog.getId();
    }

    @Transactional
    public void addBlogs( List<BlogDto> blogDtos ) {

        var blogs = blogDtos.stream()
                .map( BlogDto::toBlog )
                .toList();

        logger.info( "Adding blogs: " + blogs.stream()
                .map( Blog::getTitle )
                .collect(Collectors.joining(", ") ) );

        blogRepository.persist( blogs.stream() );
    }

    @Transactional
    public void updateBlog( IBlog blog )
    {
        logger.info( "Updating blog with id: " + blog.getId() );
        var dbBlog = blogRepository.findById( blog.getId() );

        dbBlog.setTitle( blog.getTitle() );
        dbBlog.setDescription( blog.getDescription() );
        blogRepository.persist( dbBlog );
    }

    @Transactional
    public BlogDtoSearchWrapper searchBlogs(String searchText )
    {
        var searchResult = searchSession.search( Blog.class )
                .where( f -> f.simpleQueryString().fields( "title" ).matching( searchText ) )
                .fetch( 10 );

        return BlogDtoSearchWrapper.builder().resultCount( searchResult.total().hitCount() )
                .searchText( searchText )
                .blogs( searchResult.hits().stream()
                        .map( BlogDto::toDto )
                        .toList() )
                .build();
    }
}