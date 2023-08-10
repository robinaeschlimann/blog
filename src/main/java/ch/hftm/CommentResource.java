package ch.hftm;

import ch.hftm.control.dto.CommentDto;
import ch.hftm.model.Comment;
import ch.hftm.service.CommentService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

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
    public List<CommentDto> getComments(@PathParam("id") long blogId )
    {
        return service.getComments( blogId );
    }

    @POST
    @Path( "/{id}" )
    @Produces( "application/json" )
    public Response addComment(@PathParam("id") long blogId, @Valid CommentDto comment ) throws URISyntaxException {
        service.addComment( blogId, comment );

        return Response.created( new URI( "/comments/" )).build();
    }
}