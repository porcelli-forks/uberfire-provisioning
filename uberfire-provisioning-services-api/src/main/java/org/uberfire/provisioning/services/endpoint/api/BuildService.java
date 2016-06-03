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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.uberfire.provisioning.build.spi.Binary;
import org.uberfire.provisioning.build.spi.Project;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;

/**
 *
 * @author salaboy
 */
@Path("builds")
public interface BuildService {

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("")
    List<Binary> getAllBinaries() throws BusinessException;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("")
    String newBuild(@NotNull Project project) throws BusinessException;
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("/docker")
    String createDockerImage(Project project) throws BusinessException;

}
