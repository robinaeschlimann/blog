package ch.hftm;

import ch.hftm.model.Blog;
import ch.hftm.service.BlogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path( "blogs" )
public class BlogResource
{
    @Inject
    BlogService service;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<Blog> getBlogs() {
        return this.service.getBlogs();
    }

    @POST
    public Response addBlog( Blog blog ) throws URISyntaxException
    {
        if (isBodyInvalid(blog))
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        this.service.addBlog( blog );

        return Response.created( new URI( "/blogs/" + blog.getId() )).build();
    }
    @PUT
    public Response updateBlog( Blog blog )
    {
        if (isBodyInvalid(blog))
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        this.service.updateBlog( blog );
        return Response.ok().build();
    }

    private static boolean isBodyInvalid(Blog blog)
    {
        return blog == null || blog.getTitle() == null || blog.getDescription() == null;
    }

}
