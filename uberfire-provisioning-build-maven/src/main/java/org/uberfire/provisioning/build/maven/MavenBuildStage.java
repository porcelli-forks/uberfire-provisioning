/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uberfire.provisioning.build.spi.exceptions.BuildException;
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
        try {
            int buildResult = 0;
            MavenBuild build = new MavenBuild();
            MavenProject mavenProject = new MavenProject((String) context.getData().get("projectName"));
            mavenProject.setRootPath((String) context.getData().get("projectPath"));
            mavenProject.setExpectedBinary((String) context.getData().get("expectedBinary"));
            try {
                
                buildResult = build.build(mavenProject);
            } catch (BuildException ex) {
                Logger.getLogger(MavenBuildStage.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
            if (buildResult != 0) {
                System.out.println(" >>> Build Failed! ");
                return;
            }
            String binaryLocation = build.binariesLocation(mavenProject);
            context.getData().put("warPath", binaryLocation);
            
        } catch (BuildException ex) {
            Logger.getLogger(MavenBuildStage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
