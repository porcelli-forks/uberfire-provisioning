/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.pipeline.spi.Stage;

/**
 *
 * @author salaboy
 */
public class MavenBuildStage implements Stage {

    private final String name;

    public MavenBuildStage() {
        this.name = "Maven Build Stage";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void execute(PipelineContext context) {
        int buildResult = new MavenBuild().build((String) context.getData().get("projectPath"));
        if (buildResult != 0) {
            System.out.println(" >>> Build Failed! ");
            return;
        }
        String warLocation = context.getData().get("projectPath") + "/target/" 
                + context.getData().get("projectName") + "-" + context.getData().get("projectVersion") + ".war";
        context.getData().put("warPath", warLocation);

    }

}
