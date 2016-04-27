/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.pipeline.simple.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;
import org.uberfire.provisioning.pipeline.spi.Pipeline;
import org.uberfire.provisioning.pipeline.spi.Stage;

/**
 *
 * @author salaboy
 */
@XmlRootElement
public class SimplePipeline implements Pipeline {

    private String id;
    private String name;
    private List<Stage> stages = new ArrayList<Stage>();

    public SimplePipeline() {
        this.id = UUID.randomUUID().toString();
    }

    public SimplePipeline(String name) {
        this();
        this.name = name;

    }

    @Override
    public void addStage(Stage stage) {
        stages.add(stage);
    }

    @Override
    public List<Stage> getStages() {
        return stages;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
