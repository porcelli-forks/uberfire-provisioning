/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.simple.provider;

import javax.xml.bind.annotation.XmlRootElement;
import org.uberfire.provisioning.pipeline.spi.PipelineContext;
import org.uberfire.provisioning.pipeline.spi.Stage;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class PrintOutStage2 implements Stage {

    private String name;

    public PrintOutStage2() {
    }

    public PrintOutStage2(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(PipelineContext context) {
        System.out.println(">>> Message Version 2: " + context.getData().get("message"));
    }

}
