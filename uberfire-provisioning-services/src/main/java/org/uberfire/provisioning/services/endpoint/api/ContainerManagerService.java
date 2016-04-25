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
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.services.info.ContainerInstanceProviderInfo;
import org.uberfire.provisioning.spi.ContainerInstanceInfo;
import org.uberfire.provisioning.spi.base.BaseContainerInstanceConfiguration;
import org.uberfire.provisioning.spi.providers.base.BaseContainerProviderConfiguration;
import org.uberfire.provisioning.spi.providers.info.ContainerProviderInfo;

/**
 *
 * @author salaboy
 */
@Path("containers")
public interface ContainerManagerService {

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("providers")
    List<ContainerProviderInfo> getAllContainerProviders() throws BusinessException;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("providers/instances")
    List<ContainerInstanceProviderInfo> getAllContainerProvidersInstances() throws BusinessException;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Path("providers/instances")
    void registerContainerProviderInstance(@NotNull BaseContainerProviderConfiguration conf) throws BusinessException;

    @DELETE
    @Path("providers/instances")
    void unregisterContainerProviderInstance(@FormParam(value = "name") String name) throws BusinessException;

    @POST
    @Path("instances/")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public String newContainerInstance(@NotNull BaseContainerInstanceConfiguration conf) throws BusinessException;

    @GET
    @Produces("application/json")
    @Path("instances/")
    public List<ContainerInstanceInfo> getAllContainerInstances() throws BusinessException;

    @DELETE
    @Path("instances/{id}")
    public void removeContainerInstance(@PathParam(value = "id") String id) throws BusinessException;

    @POST
    @Path("instances/{id}/start")
    public void startContainerInstance(@PathParam(value = "id") String id) throws BusinessException;

    @POST
    @Path("instances/{id}/stop")
    public void stopContainerInstance(@PathParam(value = "id") String id) throws BusinessException;

    @POST
    @Path("instances/{id}/restart")
    public void restartContainerInstance(@PathParam(value = "id") String id) throws BusinessException;

}
