# Uberfire Provisioning

A simple API to provision runtimes to different providers, such as Docker, Kubernetes, Servlet Containers (Wildfly, WAS, WebLogic, Tomcat, etc). This runtimes can be created based on different sources and by using a pipeline to build the binaries that needs to be provisioned. 

There are 5 building blocks
- **Source** (Allow us to get code locally from different sources, list all our sources, add new sources, etc)
- **Build** (API & Maven build provider implementation provided)
- **Runtime** (create new runtime in different providers: AppServers, Docker, Kubernetes, Openshift & control these runtimes)
- **Pipeline** (a way to control and chain the previous elements to we can move from source to runtime in just one service call)
- **Registry** (a way to keep track where our projects, runtimes and pipelines are)

On top of these building blocks you will find the Service Layer that allows you to interact with each step separately or with the pipelines to chain different steps together.


#Source / Workspace 
This module allows us to get code from external repositories so we can build it locally. The main idea behind these services are to provide us with a flexible way to manage our source repositories and enable us to get that code locally so it can be built. 

#Build
This block will be in charge of taking a project path and building the necesarry binaries. The binaries are placed in the target directory and can be picked up by the provisioning layer.

#Runtime
In order to provision runtimes there are 3 main concepts to understand:
 - **Provider Type** : it repesent a registered provider type into the system. This could be Docker, Kubernetes, Any Servlet container or Application Server 
 - **Provider** : it represent an actual instance of a provider. The provider repesent and contains the information to interact with a provider, so we can start provisioning things inside it. For docker, this relates to a specific Docker Deamon that we can contact, or an instance of an application server hosted remotely.
 - **Runtime** : a running container that we have provisioned and we can check the status or execute operations.

The project is divided into an SPI module whcih contains these concepts plus a set of providers that automatically register to the service via classpath resolution.

The current providers implementations are:
- Local / Fat Jar (new JVM) (uberfire-provisioning-fatjar-provider)
- Docker (uberfire-provisioning-docker-provider)
- Kubernetes (uberfire-provisioning-kubernetes-provider)
- Wildfly (uberfire-provisioning-wildfly-provider)

Once we have our new provisioned runtimes we can manage their state and control them remotely. 

By using the provided APIs we will end up creating runtimes, no matter the provider that we choose to use. 
These container instances should provide a way for the end users to execute operations and get information about state:
```
- public void start();
- public void stop();
- public void restart();
- public RuntimeInfo getInfo();
- public RuntimeState getState();
- public Provider getProvider();
- public RuntimeConfiguration getConfig();
```
# Pipeline
An API to define a set of Stages that can be chained to achieve different outputs. So for example you will be able to 
get sources from a remote repository and build those sources. Or get the generated binaries and create new runtimes into different providers. 

# Registry
The registry module provides a place to store (in a distributed way) the information related with Repositories, Projects, Providers, Runtimes, Pipelines and builds. The registry is separated into different registries for different concepts:
- Sources Registry 
- Build Registry
- Runtime Registry
- Pipelines Registry
 

The Registry project should provide a distributed implementation that allows multiple Service layers to access the same data.


# Services

A service layer is provided using JAX-RS services, so remote clients can register their provider instances and remotely provision new runtimes.

Multiple instances of the services can be started in different nodes and by using a distributed Registry module, they should be able to share the information about ProviderTypes, Providers and Runtimes.


Current Methods:
- **Source**
 - GET /api/sources  (Get all external sources repositories)
 - GET /api/sources/{id} (get repository location by id)
 - POST /api/sources  (register and clone external repository for local use)
 

- **Build**
 - GET /api/builds  (Get all builds)
 - POST /api/builds  (new build)

- **Runtime**
 - GET /api/providerstypes (Get All Provider Types)
 - GET /api/providers (Get All Providers)
 - GET /api/runtimes (Get All Runtimes)

 - POST /api/providers (New Provider)
 - POST /api/runtimes (New Runtime)
 - POST /api/runtimes/{id}/start (Start Runtime)
 - POST /api/runtimes/{id}/stop (Stop Runtime)
 - DELETE /api/runtimes/{id} (Remove Runtime)

- **Pipelines**
 - GET /api/pipelines (Get All Pipelines)
 - POST /api/pipelines (New Pipeline)
 - POST /api/pipelines/{id}/run (Run Pipeline)

# Getting Started
You can clone the repository and build all the projects using: mvn clean install (note that you need to have the M2_HOME variable set so maven-invoker can work)

In order to start the services you can do:
```
cd uberfire-provisioning-services/target/
java -jar uberfire-provisioning-services-swarm.jar
```

or alternatively with Docker (this will need to download the docker image first)
```
docker run -p 8080:8082 salaboy/uberfire-provisioning-services 
```
where 8082 is the port where the services are running and 8080 is the port that I will use to access from my environment.

You can also build and start your own docker image with:
```
cd uberfire-provisioning-services/
mvn clean install docker:build docker:start
```

A Postman (Google Chrome add on) project is provided here, where you can execute operations against the services and sample requests are provided for most operations.

https://github.com/Salaboy/uberfire-provisioning/blob/master/uberfire-provisioning.json.postman_collection

You need to download/clone this file and then import it to Postman (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en)

# Example From Sources to Provisioning

The following example show how to use the Uberfire Provision Services provision a new Runtime using as a starting point a Git repository. Different (Runtime) Providers can be configured, but for this example I will be showing Local, Wildfly and Docker. It is trivial to extend this example to provision to Openshift 3 Origin (Kubernetes).

First of all, we need to configure our Providers, this is were we want to provision new Runtimes. As mentioned before we will have 3 providers Local, Wildfly 10, and Docker. In order to do that we can register these 3 providers by executing the following requests to the Provision Services:

0) Check the registered Provider Types 
```
GET http://localhost:8082/api/providertypes
```
This should return:
```
[
  {
    "provider": "org.uberfire.provisioning.kubernetes.runtime.provider.KubernetesProvider",
    "version": "1",
    "providerTypeName": "kubernetes"
  },
  {
    "provider": "org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider",
    "version": "10.0.0",
    "providerTypeName": "wildfly"
  },
  {
    "provider": "org.uberfire.provisioning.local.runtime.provider.LocalProvider",
    "version": "1.0",
    "providerTypeName": "local"
  },
  {
    "provider": "org.uberfire.provisioning.docker.runtime.provider.DockerProvider",
    "version": "1.9.1",
    "providerTypeName": "docker"
  }
]
```

1) Register Providers
- Local (this might be automatically registered in the future)
POST http://localhost:8082/api/providers/
```
{
    "org.uberfire.provisioning.local.runtime.provider.LocalProviderConfiguration": {
        "name":"local fatjar runner"
    }
}
```
- Wildfly 10 (you need to have a Wildfly 10 running on localhost 9990 - mgmt port)
POST http://localhost:8082/api/providers/
```
{
    "org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration": {
        "name":"wildfly at 9990", 
        "host": "localhost",
        "managementPort": "9990",
        "user": "salaboy",
        "password": "salaboy123$"
    }
}
```

- Docker (this might be autoregistered based on the env variables in the future)
POST http://localhost:8082/api/providers/
```
{
    "org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfiguration": {
        "name":"docker local"
    }
}
```

2) Check all the registered providers:
GET http://localhost:8082/api/providers/

```
[
  {
    "org.uberfire.provisioning.local.runtime.provider.LocalProvider": {
      "name": "local fatjar runner",
      "config": {
        "org.uberfire.provisioning.local.runtime.provider.LocalProviderConfiguration": {
          "name": "local fatjar runner",
          "provider": "org.uberfire.provisioning.local.runtime.provider.LocalProvider"
        }
      },
      "providerType": {
        "version": "1.0",
        "provider": "org.uberfire.provisioning.local.runtime.provider.LocalProvider",
        "providerTypeName": "local"
      }
    }
  },
  {
    "org.uberfire.provisioning.docker.runtime.provider.DockerProvider": {
      "name": "docker local",
      "config": {
        "org.uberfire.provisioning.docker.runtime.provider.DockerProviderConfiguration": {
          "name": "docker local",
          "provider": "org.uberfire.provisioning.docker.runtime.provider.DockerProvider"
        }
      },
      "providerType": {
        "version": "1.9.1",
        "provider": "org.uberfire.provisioning.docker.runtime.provider.DockerProvider",
        "providerTypeName": "docker"
      },
      "docker": {
        "host": "192.168.99.100"
      }
    }
  },
  {
    "org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider": {
      "name": "wildfly at 9990",
      "config": {
        "org.uberfire.provisioning.wildfly.runtime.provider.base.WildflyProviderConfiguration": {
          "name": "wildfly at 9990",
          "provider": "org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider",
          "host": "localhost",
          "password": "salaboy123$",
          "user": "salaboy",
          "managementPort": "9990"
        }
      },
      "providerType": {
        "version": "10.0.0",
        "provider": "org.uberfire.provisioning.wildfly.runtime.provider.wildly10.Wildfly10Provider",
        "providerTypeName": "wildfly"
      }
    }
  }
]
```

3) Source & Build
Before being able to provision, we need to get the sources for our project and built it to generate their appropriate binaries. I will be using this repository: https://github.com/Salaboy/livespark-playground -> provisioning-enablement branch which contains a livespark app. 

- First all of all we need to register the repository:
POST http://localhost:8082/api/sources/
```
{
    "org.uberfire.provisioning.source.github.GitHubRepository":{
        "name":"livespark playground",
        "uri":"https://github.com/Salaboy/livespark-playground.git",
        "branch":"provisioning-enablement"
    }
}
```
Which returns the repository ID: for example: "db0e9df7-93f"

- Then you can check where the project was sourced (cloned)
GET http://localhost:8082/api/sources/db0e9df7-93f/location

Returns the temporary directory where the project sources are:
```
/private/var/folders/zl/qhjypfyd5k7bbtpgf276w0ww0000gn/T/uf-source2026886451401276161
```

- Next you need to register the project itself (this might be done automatically later). Now we have the odd case of a repo that is not a project itself (there is no parent pom) so it needs to be manually registered:
POST http://localhost:8082/api/sources/db0e9df7-93f/
```
{
    "org.uberfire.provisioning.build.maven.MavenProject":{
        "name":"users-new",
        "rootPath":"/private/var/folders/zl/qhjypfyd5k7bbtpgf276w0ww0000gn/T/uf-source2026886451401276161",
        "path": "users-new",
        "expectedBinary": "users-new.war"
    }
}
```


- Now that we have the project we can build it
POST http://localhost:8082/api/builds/
```
  {
    "org.uberfire.provisioning.build.maven.MavenProject": {
      "id": "bc24c200-fb6",
      "name": "users-new",
      "type": "Maven",
      "rootPath": "/private/var/folders/zl/qhjypfyd5k7bbtpgf276w0ww0000gn/T/uf-source2026886451401276161",
      "path": "users-new",
      "expectedBinary": "users-new.war"
    }
  }
```
This will trigger the maven build of the specified project leaving the binaries in the /target/ directory.

4) Provisioning
Now that we have our binaries we can provision new runtimes with them. 

