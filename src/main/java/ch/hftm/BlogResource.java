package ch.hftm;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.model.BlogMessage;
import ch.hftm.repository.BlogRepository;
import ch.hftm.service.BlogService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path( "blogs" )
public class BlogResource
{
    @Inject
    BlogService service;

    @Inject
    BlogRepository blogRepository;

    @Inject
    @Channel("blog")
    Emitter<BlogMessage> emitter;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Operation( description = "Get all blogs. With the query parameter limit you can limit the number of returned blogs.")
    @APIResponse( responseCode = "200", description = "Blogs returned")
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
    @Produces( MediaType.APPLICATION_JSON )
    @Operation( description = "Add a new blog")
    @RequestBody( description = "The blog to add", required = true )
    @APIResponse( responseCode = "201", description = "Blog created")
//    @RolesAllowed("create-blog")
    public Response addBlog( @Valid BlogDto blog ) throws URISyntaxException
    {
        var blogId = this.service.addBlog( blog );

        var blogDb = blogRepository.findById( blogId );

        emitter.send(BlogMessage.builder()
                .id(blogDb.getId())
                .title(blogDb.getTitle())
                .description(blogDb.getDescription())
                .build());

        return Response.created( new URI( "/blogs/" + blog.getId() )).build();
    }
    @PUT
    @Operation( description = "Update an existing blog")
    @RequestBody( description = "The blog to update", required = true )
//    @RolesAllowed("create-blog")
    public Response updateBlog( @Valid BlogDto blog )
    {
        this.service.updateBlog( blog );
        return Response.ok().build();
    }
}
