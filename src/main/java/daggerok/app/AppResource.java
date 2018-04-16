package daggerok.app;

import daggerok.app.message.Message;
import daggerok.app.message.MessageRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("")
@Stateless
@Produces(APPLICATION_JSON)
public class AppResource {

  @Context
  UriInfo uriInfo;
  @Inject
  MessageRepository messages;

  @POST
  @Path("")
  public Response storeNotSimilarDataOnly(final JsonObject request) {

    final String data = request.getString("data", "");

    if (null == data || data.trim().isEmpty())
      throw new RuntimeException("invalid payload");

    final Message message = messages.maybeSave(new Message().setData(data));

    return Response.accepted(Json.createObjectBuilder()
                                 .add("_self", uriInfo.getBaseUriBuilder()
                                                      .path(AppResource.class)
                                                      .path(AppResource.class, "all")
                                                      .build()
                                                      .toString())
                                 .add("message", Json.createObjectBuilder()
                                                     .add("id", message.getId())
                                                     .add("data", message.getData()))
                                 .build())
                   .build();
  }

  @GET
  @Path("")
  public Response all() {
    return Response.ok(messages.findAll())
                   .build();
  }

  @GET
  @Path("health")
  public Response health() {
    return Response.ok(Json.createObjectBuilder()
                           .add("status", "UP")
                           .add("_self", uriInfo.getBaseUriBuilder()
                                                .path(AppResource.class)
                                                .path(AppResource.class, "health")
                                                .build()
                                                .toString())
                           .build())
                   .build();
  }
}
