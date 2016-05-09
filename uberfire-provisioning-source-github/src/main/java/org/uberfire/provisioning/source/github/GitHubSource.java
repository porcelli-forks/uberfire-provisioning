/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.uberfire.provisioning.source.Source;

/**
 *
 * @author salaboy
 */
public class GitHubSource implements Source{

    @Override
    public String getSource(String repository, String pathTo) throws GitAPIException {
        Git.cloneRepository()
                .setURI(repository)
                .setDirectory(new File(pathTo))
                .call();
        return pathTo;
    }
}
