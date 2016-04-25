# Uberfire Provisioning
A simple API to provision runtimes to different providers, such as Docker, Kubernetes, Servlet Containers (Wildfly, WAS, WebLogic, Tomcat, etc)

There are 4 main concepts to understand (I accept suggestion on the names):
 - **Container Provider** (Box Provider): it repesent a registered container provider into the system. This could be Docker, Kubernetes, Any Servlet container or Application Server 
 - **Container Provider Instance** (Box Provider Instance): it represent an actual instance of a provider. The provider instance repesent and contains the information to interact with an instance of a provider, so we can start provisioning things inside it. For docker, this relates to a specific Docker Deamon that we can contact, or an instance of an application server hosted remotely.
 - **Container** (Box): the container definition and description. This repesent what do we want to provision.
 - **Container Instance** (Box Instance): a running container that we have provisioned and we can check the status or execute operations.
 

The project is divided into an SPI module whcih contains these concepts plus a set of providers that automatically register to the service via classpath resolution.

The Cluster SPI is also provided to solve registration and lookup for different nodes using the APIs to keep in sync. A local implementation is provided for testing, but a distributed registry will be needed for real life scenarios.


# Container Instance
By using the provided APIs we will end up creating container instances, no matter the provider that we choose to use. 
These container instances should provide a way for the end users to execute operations and get information about state:
```
- public void start();
- public void stop();
- public void restart();
- public ContainerInstanceInfo getInfo();
- public ContainerInstanceState getState();
```

# Container Provisioning Services

A service layer is provided using JAX-RS services, so remote clients can register their provider instances and remotely provision new container instances.

Multiple instances of the services can be started in different nodes and by using the Cluster module, they should be able to share the information about ContainerProvidersInstances, Containers and ContainerInstances.



