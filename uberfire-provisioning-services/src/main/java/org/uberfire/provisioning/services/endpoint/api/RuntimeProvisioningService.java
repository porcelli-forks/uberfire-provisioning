/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.endpoint.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.uberfire.provisioning.runtime.spi.RuntimeConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.Provider;
import org.uberfire.provisioning.runtime.spi.providers.ProviderConfiguration;
import org.uberfire.provisioning.runtime.spi.providers.ProviderType;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.runtime.spi.Runtime;

/**
 *
 * @author salaboy
 */
@Path("")
public interface RuntimeProvisioningService {

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("providertypes")
    List<ProviderType> getAllProviderTypes() throws BusinessException;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("providers")
    List<Provider> getAllProviders() throws BusinessException;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("providers")
    void registerProvider(@NotNull ProviderConfiguration conf) throws BusinessException;

    @DELETE
    @Path("providers")
    void unregisterProvider(@FormParam(value = "name") String name) throws BusinessException;

    @POST
    @Path("runtimes/")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public String newRuntime(@NotNull RuntimeConfiguration conf) throws BusinessException;

    @GET
    @Produces("application/json")
    @Path("runtimes/")
    public List<Runtime> getAllRuntimes() throws BusinessException;

    @DELETE
    @Path("runtimes/{id}")
    public void unregisterRuntime(@PathParam(value = "id") String id) throws BusinessException;

//    @POST
//    @Path("instances/{id}/start")
//    public void startContainerInstance(@PathParam(value = "id") String id) throws BusinessException;
//
//    @POST
//    @Path("instances/{id}/stop")
//    public void stopContainerInstance(@PathParam(value = "id") String id) throws BusinessException;
//
//    @POST
//    @Path("instances/{id}/restart")
//    public void restartContainerInstance(@PathParam(value = "id") String id) throws BusinessException;

}
