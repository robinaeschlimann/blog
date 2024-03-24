package ch.hftm.service;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.control.dto.BlogDtoHighlight;
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
    public static final float BOOST_TITLE = 6.0f;
    public static final float BOOST_DESCRIPTION = 1.0f;
    @Inject
    BlogRepository blogRepository;

    @Inject
    SearchSession searchSession;

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

        var blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());

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
    public SearchResultDto<BlogDto> searchBlogs(String searchText, int page)
    {
        var searchResult = searchSession.search( Blog.class )
                .where( f -> f.simpleQueryString()
                        .field( "title" ).boost( BOOST_TITLE )
                        .field( "description" ).boost( BOOST_DESCRIPTION )
                        .matching( searchText ) )
                .sort( f -> f.score().then().field( "title_sort" ) )
                .fetch( page * PAGE_SIZE, PAGE_SIZE);

        return new SearchResultDto<>( searchResult.hits().stream()
                .map( BlogDto::toDto )
                .toList(), searchResult.total().hitCount() );
    }

    @Transactional
    public SearchResultDto<BlogDtoHighlight> searchBlogsWithHighlighting( String searchText, int page )
    {
        var searchResult = searchSession.search( Blog.class )
                .select( BlogDtoHighlight.class )
                .where( f -> f.simpleQueryString()
                        .field( "title" ).boost(BOOST_TITLE)
                        .field( "description" ).boost(BOOST_DESCRIPTION)
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