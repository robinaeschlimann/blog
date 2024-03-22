package ch.hftm.service;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.control.dto.BlogDtoHighlight;
import ch.hftm.control.dto.BlogDtoSearchWrapper;
import ch.hftm.control.dto.SearchResultDto;
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
    public static final int PAGE_SIZE = 10;
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
    public BlogDtoSearchWrapper searchBlogs(String searchText, int page)
    {
        var searchResult = searchSession.search( Blog.class )
                .where( f -> f.simpleQueryString()
                        .field( "title" ).boost( 6.0f )
                        .field( "description" ).boost( 1.0f )
                        .matching( searchText ) )
                .sort( f -> f.score().then().field( "title_sort" ) )
                .fetch( page * PAGE_SIZE, PAGE_SIZE);

        return BlogDtoSearchWrapper.builder().resultCount( searchResult.total().hitCount() )
                .searchText( searchText )
                .blogs( searchResult.hits().stream()
                        .map( BlogDto::toDto )
                        .toList() )
                .build();
    }

    @Transactional
    public SearchResultDto<BlogDtoHighlight> searchBlogsWithHighlighting( String searchText, int page )
    {
        var searchResult = searchSession.search( Blog.class )
                .select( BlogDtoHighlight.class )
                .where( f -> f.simpleQueryString()
                        .field( "title" ).boost( 6.0f )
                        .field( "description" ).boost( 1.0f )
                        .matching( searchText ) )
                .highlighter( f -> f.unified()
                        .tag( "<strong>", "</strong>")
                        .numberOfFragments( 1 )
                        .fragmentSize(256)
                        .noMatchSize(256) )
                .sort( f -> f.score().then().field( "title_sort" ) )
                .fetch( page * PAGE_SIZE, PAGE_SIZE);

        return new SearchResultDto<>( searchResult.hits(), searchResult.total().hitCount() );
    }
}