/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.provisioning.integration.tests;

import java.util.List;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uberfire.provisioning.build.Binary;
import org.uberfire.provisioning.build.Build;
import org.uberfire.provisioning.build.Project;
import org.uberfire.provisioning.build.maven.MavenBinary;
import org.uberfire.provisioning.build.maven.MavenBuild;
import org.uberfire.provisioning.build.maven.MavenProject;
import org.uberfire.provisioning.docker.runtime.provider.DockerBinary;
import org.uberfire.provisioning.exceptions.BuildException;
import org.uberfire.provisioning.exceptions.SourcingException;
import org.uberfire.provisioning.registry.BuildRegistry;
import org.uberfire.provisioning.registry.SourceRegistry;
import org.uberfire.provisioning.registry.local.InMemoryBuildRegistry;
import org.uberfire.provisioning.registry.local.InMemorySourceRegistry;
import org.uberfire.provisioning.source.Repository;
import org.uberfire.provisioning.source.Source;
import org.uberfire.provisioning.source.github.GitHubRepository;
import org.uberfire.provisioning.source.github.GitSource;

/**
 * @author salaboy
 */
@RunWith(Arquillian.class)
public class SimpleSourceAndBuildAPITest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create( JavaArchive.class )
                .addClass( GitSource.class )
                .addClass( MavenBuild.class )
                .addClass( MavenProject.class )
                .addClass( InMemorySourceRegistry.class )
                .addClass( InMemoryBuildRegistry.class )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" );
        System.out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    private SourceRegistry sourceRegistry;

    @Inject
    private BuildRegistry buildRegistry;

    @Inject
    private Source source;

    @Inject
    private Build build;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void helloSourceAndBuildAPIs() throws SourcingException, BuildException {

        Repository repo = new GitHubRepository( "livespark playground" );
        repo.setURI( "https://github.com/salaboy/livespark-playground.git" );
        repo.setBranch( "provisioning-enablement" );
        String location = source.getSource( repo );
        System.out.println( "Location : " + location );
        sourceRegistry.registerRepositorySources( location, repo );

        List<Repository> allRepositories = sourceRegistry.getAllRepositories();
        Assert.assertEquals( 1, allRepositories.size() );

        Project project = new MavenProject( "users-new" );
        project.setRootPath( location );
        project.setPath( "users-new" );
        project.setExpectedBinary( "users-new.war" );

        sourceRegistry.registerProject( repo, project );

        List<Project> projectsAll = sourceRegistry.getAllProjects( repo );
        Assert.assertEquals( 1, projectsAll.size() );

        List<Project> projectsByName = sourceRegistry.getProjectByName( "users-new" );
        Assert.assertEquals( 1, projectsByName.size() );

        int result = build.build( project );

        Assert.assertTrue( result == 0 );
        System.out.println( "Result: " + result );

        boolean binariesReady = build.binariesReady( project );
        Assert.assertTrue( binariesReady );

        System.out.println( "binariesReady " + binariesReady );

        String finalLocation = build.binariesLocation( project );
        System.out.println( "finalLocation " + finalLocation );

        MavenBinary mavenBinary = new MavenBinary( project );
        buildRegistry.registerBinary( mavenBinary );

        List<Binary> allBinaries = buildRegistry.getAllBinaries();
        Assert.assertEquals( 1, allBinaries.size() );

    }

    @Test
    @Ignore
    /*
     * This test requires to have a Docker client running in your environment to 
     * create the docker image. 
    */
    public void helloSourceAndBuildDockerImage() throws SourcingException, BuildException {

        Repository repo = new GitHubRepository( "livespark playground" );
        repo.setURI( "https://github.com/salaboy/livespark-playground.git" );
        repo.setBranch( "provisioning-enablement" );
        String location = source.getSource( repo );
        System.out.println( "Location : " + location );
        sourceRegistry.registerRepositorySources( location, repo );

        List<Repository> allRepositories = sourceRegistry.getAllRepositories();
        Assert.assertEquals( 1, allRepositories.size() );

        Project project = new MavenProject( "users-new" );
        project.setRootPath( location );
        project.setPath( "users-new" );
        project.setExpectedBinary( "users-new.war" );

        sourceRegistry.registerProject( repo, project );

        List<Project> projectsAll = sourceRegistry.getAllProjects( repo );
        Assert.assertEquals( 1, projectsAll.size() );

        List<Project> projectsByName = sourceRegistry.getProjectByName( "users-new" );
        Assert.assertEquals( 1, projectsByName.size() );

        int result = build.createDockerImage( project );

        Assert.assertTrue( result == 0 );
        System.out.println( "Result: " + result );

        boolean binariesReady = build.binariesReady( project );
        Assert.assertTrue( binariesReady );

        System.out.println( "binariesReady " + binariesReady );

        String finalLocation = build.binariesLocation( project );
        System.out.println( "finalLocation " + finalLocation );

        DockerBinary mavenBinary = new DockerBinary( project );
        buildRegistry.registerBinary( mavenBinary );

        List<Binary> allBinaries = buildRegistry.getAllBinaries();
        Assert.assertEquals( 1, allBinaries.size() );

    }

}
