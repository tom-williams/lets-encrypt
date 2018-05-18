package com.imotions.letsencrypt.api;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.OffsetDateTime;

@Path("/debug")
@Produces(MediaType.APPLICATION_JSON)
public class DebugResource {

    @GET
    @Path("/time")
    public Response debugTime() {
        return Response.ok(ImmutableMap.of("time", OffsetDateTime.now())).build();
    }

}
