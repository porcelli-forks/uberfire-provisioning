/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.services.pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.pipeline.spi.Stage;
import org.uberfire.provisioning.services.endpoint.api.ContainerProvisioningService;
import org.uberfire.provisioning.services.endpoint.exceptions.BusinessException;
import org.uberfire.provisioning.spi.base.BaseContainerInstanceConfiguration;

/**
 *
 * @author salaboy
 */
public class ProvisionContainerStage implements Stage {

    private final String name = "provision stage";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(PipelineContext context) {
        ContainerProvisioningService provisioningService = (ContainerProvisioningService) context.getServices().get("provisioningService");
        BaseContainerInstanceConfiguration conf = new BaseContainerInstanceConfiguration();
        Map<String, String> props = new HashMap<String, String>();

        for (String key : context.getData().keySet()) {
            props.put(key, (String) context.getData().get(key));
        }
        conf.setProperties(props);
        try {
            provisioningService.newContainerInstance(conf);
        } catch (BusinessException ex) {
            Logger.getLogger(ProvisionContainerStage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
