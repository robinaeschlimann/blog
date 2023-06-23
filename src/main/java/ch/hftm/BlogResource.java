package ch.hftm;

import ch.hftm.model.Blog;
import ch.hftm.service.BlogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path( "blogs" )
public class BlogResource
{
    @Inject
    BlogService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> getBlogs() {
        return this.service.getBlogs();
    }

    @POST
    public void addBlog( Blog blog )
    {
        this.service.addBlog( blog );
    }
}
