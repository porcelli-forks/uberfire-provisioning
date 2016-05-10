/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import org.uberfire.provisioning.source.Repository;

/**
 *
 * @author salaboy
 */
public class GitHubRepository implements Repository {

    private String URI;
    private String name;
    private String type;
    private boolean bare = false;

    public GitHubRepository() {
        this.type = "GitHub";
    }

    public GitHubRepository(String name) {
        this();
        this.name = name;
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

}
