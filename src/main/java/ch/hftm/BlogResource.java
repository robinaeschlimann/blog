package ch.hftm;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.service.BlogService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

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
    public List<BlogDto> getBlogs(@RestQuery Integer limit)
    {
        var blogs = this.service.getBlogs();

        if (limit != null && limit > 0) {
            return blogs
                    .stream()
                    .limit(limit)
                    .toList();
        }
        return blogs;
    }

    @POST
    public Response addBlog( @Valid BlogDto blog ) throws URISyntaxException
    {
        this.service.addBlog( blog );

        return Response.created( new URI( "/blogs/" + blog.getId() )).build();
    }
    @PUT
    public Response updateBlog( @Valid BlogDto blog )
    {
        this.service.updateBlog( blog );
        return Response.ok().build();
    }
}
