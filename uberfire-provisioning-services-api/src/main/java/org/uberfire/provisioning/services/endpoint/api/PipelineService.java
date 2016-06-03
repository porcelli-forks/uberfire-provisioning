/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.endpoint.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.uberfire.provisioning.pipeline.spi.Pipeline;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

/**
 *
 * @author salaboy
 */
@Path("pipelines")
public interface PipelineService {

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("")
    List<Pipeline> getAllPipelines() throws BusinessException;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("")
    String newPipeline(@NotNull Pipeline pipeline) throws BusinessException;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("{id}/run")
    void runPipeline(@PathParam("id") String id, @NotNull PipelineContext context) throws BusinessException;

}
