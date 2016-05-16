/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import java.util.UUID;
import org.uberfire.provisioning.source.Repository;

/**
 *
 * @author salaboy
 */
public class GitHubRepository implements Repository {

    private String id;
    private String URI;
    private String name;
    private String type;
    private String branch;
    private boolean bare = false;

    public GitHubRepository() {
        this.type = "GitHub";
        this.id = UUID.randomUUID().toString().substring(0, 12);
    }

    public GitHubRepository(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getURI() {
        return URI;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isBare() {
        return bare;
    }

    @Override
    public void setURI(String URI) {
        this.URI = URI;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setBare(boolean bare) {
        this.bare = bare;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
