package com.imotions.letsencrypt.api;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/letsencrypt")
@Produces(MediaType.APPLICATION_JSON)
public class LetsEncryptResource {


    @PUT
    @Path("/sslReload")
    public Response sslReload() {
        // sslWorker.forceRun();

        return Response.ok().build();
    }
}
