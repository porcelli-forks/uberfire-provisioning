/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.source.github;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.util.FileUtils;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.exceptions.SourcingException;

/**
 *
 * @author salaboy
 */
public class GitSource implements Source {

    @Override
    public String getSource(Repository repository) throws SourcingException {
        String tmpDir = System.getProperty("java.io.tmpdir");

        try {
            File createdTempDir = FileUtils.createTempDir("uf-source", "", new File(tmpDir));
            Git.cloneRepository()
                    .setURI(repository.getURI())
                    .setDirectory(createdTempDir)
                    .setBranch((repository.getBranch() != null) ? repository.getBranch() : "master")
                    .call();

            return createdTempDir.getCanonicalPath();
        } catch (Exception ex) {
            Logger.getLogger(GitSource.class.getName()).log(Level.SEVERE, null, ex);
            throw new SourcingException("Error Cloning Git Repository", ex);
        }
    }
}
