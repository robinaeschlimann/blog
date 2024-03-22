package ch.hftm;

import ch.hftm.control.dto.CommentDto;
import ch.hftm.control.dto.CommentDtoSearchWrapper;
import ch.hftm.model.Comment;
import ch.hftm.service.CommentService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path( "comments" )
public class CommentResource
{
    @Inject
    CommentService service;

    @GET
    @Path( "/{id}" )
    @Produces( "application/json" )
    @Operation( description = "Get all comments for a blog")
    public List<CommentDto> getComments(@PathParam("id") long blogId )
    {
        return service.getComments( blogId );
    }

    @POST
    @Path( "/{id}" )
    @Produces( "application/json" )
    @Operation( description = "Add a new comment to a blog")
    @RequestBody( description = "The comment to add", required = true)
    public Response addComment(@PathParam("id") long blogId, @Valid CommentDto comment ) throws URISyntaxException {
        service.addComment( blogId, comment );

        return Response.created( new URI( "/comments/" )).build();
    }

    @GET
    @Path( "/search" )
    @Produces( "application/json" )
    @Operation( description = "Search for comments")
    public CommentDtoSearchWrapper searchComments(@QueryParam("searchText") String searchText )
    {
        return service.searchComments( searchText );
    }
}
