/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uberfire.provisioning.build.maven;

import java.io.File;
import java.util.Collections;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.uberfire.provisioning.build.spi.Build;

/**
 *
 * @author salaboy
 */
public class MavenBuild implements Build {

    @Override
    public int build(String projectPath) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectPath + "/pom.xml"));
        request.setGoals(Collections.singletonList("package"));

        Invoker invoker = new DefaultInvoker();
//        invoker.setMavenHome(new File("/usr/local/Cellar/maven32/3.2.5/libexec"));

        try {
            InvocationResult results = invoker.execute(request);
            return results.getExitCode();
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
