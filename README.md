# Uberfire Provisioning

A simple API to provision runtimes to different providers, such as Docker, Kubernetes, Servlet Containers (Wildfly, WAS, WebLogic, Tomcat, etc). This runtimes can be created based on different sources and by using a pipeline to build the binaries that needs to be provisioned. 

There are 5 building blocks
- Source (not in the repo yet, but it should allow us to get code locally from different sources)
- Build (API & Maven build provider implementation provided)
- Runtime (create new runtime in different providers: AppServers, Docker, Kubernetes, Openshift & control these runtimes)
- Pipeline (a way to control and chain the previous elements to we can move from source to runtime in just one service call)
- Registry (a way to keep track where our projects, runtimes and pipelines are)

On top of these building blocks you will find the Service Layer that allows you to interact with each step separately.


#Source

#Build
This block will be in charge of taking a project path and building the necesarry binaries. The binaries are placed in the target directory and can be picked up by the provisioning layer.

#Runtime
In order to provision runtimes there are 3 main concepts to understand:
 - **Provider Type** : it repesent a registered provider type into the system. This could be Docker, Kubernetes, Any Servlet container or Application Server 
 - **Provider** : it represent an actual instance of a provider. The provider repesent and contains the information to interact with a provider, so we can start provisioning things inside it. For docker, this relates to a specific Docker Deamon that we can contact, or an instance of an application server hosted remotely.
 - **Runtime** : a running container that we have provisioned and we can check the status or execute operations.

The project is divided into an SPI module whcih contains these concepts plus a set of providers that automatically register to the service via classpath resolution.

Once we have our new provisioned runtimeswe can manage their state and control them remotely. 

By using the provided APIs we will end up creating runtimes, no matter the provider that we choose to use. 
These container instances should provide a way for the end users to execute operations and get information about state:
```
- public void start();
- public void stop();
- public void restart();
- public RuntimeInfo getInfo();
- public RuntimeState getState();
```


# Services

A service layer is provided using JAX-RS services, so remote clients can register their provider instances and remotely provision new runtimes.

Multiple instances of the services can be started in different nodes and by using a distributed Registry module, they should be able to share the information about ProviderTypes, Providers and Runtimes.


Current Methods:
- Build
 - GET /api/builds  (Get all builds)
 - POST /api/builds  (new build)

- Runtime
 - GET /api/runtime/providers (Get All Providers)
 - GET /api/runtime/providers/instances (Get All Provider Instances)
 - GET /api/runtime/instances (Get All Container Instances)

 - POST /api/runtime/providers/instances (New Provider Instance)
 - POST /api/runtime/instances (New Container Instance)
 - POST /api/runtime/instances/<id>/start (Start Container Instance)
 - POST /api/runtime/instances/<id>/stop (Stop Container Instance)
 - DELETE /api/runtime/instances/<id> (Remove Container Instance)

- Pipelines
 - GET /api/pipelines (Get All Pipelines)
 - POST /api/pipelines (New Pipeline)
 - POST /api/<id>/run (Run Pipeline)

 




