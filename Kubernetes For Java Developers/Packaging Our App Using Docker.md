### Packaging Our App Using Docker

#### Docker workflow

* A client host, a server host, and a registry. 

* Registry is a stateless highly scalable server side application that stores and lets you distribute Docker images. This is typically hosted on the internet, such as Docker Hub or Amazon Elastic Container Registry. It could be hosted inside the firewall as well. Such as using JFrog's Artifactory or other similar products. This is where all your Docker images, including application images are published. 

* Docker client host is from where the commands to download an image or run a container are issued. This could be your Dell machine during development or CI/CD pipeline that is giving the commands to run the containers. The command is typically issued using Docker CLI which is also installed on the client host.

* There is server host where the images are downloaded for your use and containers are run. There are multiple server hosts to make the application highly available. Each server host has Docker Engine installed that is listening for commands. 

* App Developer creates a Docker image and publishes it on the registry, my app in our case. The developers gives the command from the client host like, Docker container run, using Docker CLI. CLI than translates this command to arrest request and sends it to the Docker Engine on the server host. Docker Engine checks if the image is already available on the server host.

* If the image already exists, then it runs the container. If the image does not exist, then it downloads the image from the pre-configured registry and runs the container. 

* The image is stored on the server for later reuse. If multiple instances of the container for the same image needs to run, then the image is only downloaded once, unless a newer version is specified. I also have a course on Docker for Java Developers that provides much more details about how you can get started with Docker. 

  ![k8s7](images/k8s7.png)

#### Docker Image and Container

* #### Create a Docker Image

* Docker image can be built from a text-based file, usually called a Dockerfile. Dockerfile is a text document that contains all the commands a user could call on the command line to assemble the image. Docker runs instructions in a Docker file in the specified order. A Dockerfile must start with a FROM instruction. The FROM instruction specifies a base image from which you are building. The base imagine could be an operating system or another general purpose image built by somebody and already has some of the tools that you need for the application.

  * Text-based file
  * Default name is Dockerfile
  * Instructions : FROM, ADD, CMD

* Dockerfile syntax allows multiple instructions. ADD is one such instruction that copies new files, directories, or remote file URLs from a source, and adds them to file system of the image at the destination path. Another important instruction is CMD, that provides defaults for executing container. If there are multiple CMD instructions then only the last CMD will take effect. This is where typically you start or deploy your application.

* Sample Dockerfile : 

  ```shell
  FROM debian
  CMD echo "hello world!"
  ```

  ```shell
  FROM openjdk
  CMD java -version
  ```

* As a developer, you can start with an operating system base image, and add your specific version of the JDK. But if an image with the JDK version that you need already exists, then you're more likely to use that. 

* #### Multi-Stage Dockerfile

* Docker enables multi-stage builds using Dockerfile. 

* This is enabled by having multiple FROM statements. Each FROM statement can use a different base and each of them begins a new stage of the build. 

  ![k8s8](images/k8s8.png)

* You can selectively copy artifacts from one stage to another, leaving behind everything you don't want in the final image. This allows build and run of the application from the same Dockerfile, and yet have a lean Docker image for production.

  ![k8s9](images/k8s9.png)

* If you're looking for a no-cost, long term support downstream distribution of OpenJDK then Amazon Corretto is one of your choice. An official Docker image for Amazon Corretto is also available at hub.docker.com So you can easily replace OpenJDK with Amazon Corretto as shown in this slide. 

* Amazon Corretto is meant to be a drop-in replacement for OpenJDK compatible JDKs. You can of course use Amazon Corretto on desktop, on Prime and on the Cloud, as a standalone download. 

* #### Build Context

* It is important to understand what is build context when building a Docker image. Docker image is built using Docker image build command. 

  ```shell
  # Set of files in the PATH or URL of docker image build command
  $ docker image build -t ex:v1 -f Dockerfile .
  ```

* This command takes a path or a URL. By default Dockerfile exists at the root of this path or URL. An instruction in the Dockerfile such as ADD will resolve files in the local file system using this build context. 

* The build context is sent to the Docker engine so it is recommended to create a new directory, copy the Dockerfile and other necessary artifacts in that directory, and then issue the build command.

* Empty directory with relevant artifacts

* Alternatively you can also use .dockerignore, very much like .gitignore to exclude files and directories from the build context.

* #### Name an Image

* Each image is uniquely identified with a name and a tag. When building an image you need to assign a name to the image and optionally tag it. If no tag is provided then the latest tag is used. 

  ```shell
  $ docker image build -t name:tag
  ```

* #### Run a Container

* Running an image launches a container. Docker container run command is used to run the container, you just need to specify the image name. As explained in the Docker workflow, the CLI will give this command to the Docker engine, which will check if the image is already available on server host. Download it if needed and then run the container. 

  ```shell
  $ docker container run <image name>
  ```

* By default the container runs in foreground. - d option allows to run the container in detach or background mode. 

* Background : -d

* Multiple options to the command can be combined such as -it

* Interactive : -it 

* Here -i keep STDIN open and -t allocates a pseudo-tty.

* #### Port Forward in Container

* By default no ports of the containers are published to the outside world, however a port can be explicitly made available using port forwarding.

* Allows application in container accessible on the host.

  ```shell
  docker container run -p <host port>:<container port>
  ```

* Multiple ports using additional -p

#### Build a Docker image using a Dockerfile

* We're going to use Dockerfile to build our application. Here, this is a Dockerfile, part of our maven repo. This is the second file that we are explaining here now. As you can see, this is a multi stage Dockerfile. Here is my first from statement, and here is my second from statement. The first from statement is using maven as a base image. And I'm specifying a tag that means I want maven version three point five and jdk eight. You can look at Docker Hub and see where these images are available and what other tags are available if you need a different version of maven. I'm giving this stage a name, call it Build. The first step here is taking a repository.tar.gz which will generate in a second and copying it to a specific directory in the maven image itself.

* This directory is existing in the maven base image. Then we take our application code and copy it to the base image as well in the user source app directory. We set that as a work directory and then give the command call as maven, where we are referring to the settings for maven and package the application. Now this really leverages the maven repository that we will generate locally and repurpose that for creating your application package. At this point, yes, we are using Dockerfile, but it's using basic maven commands to package your Java application. 

  ```Dockerfile
  FROM maven:3.5-jdk-8 as BUILD
  
  ADD repository.tar.gz /usr/share/maven/ref/
  
  COPY . /usr/src/app
  WORKDIR /usr/src/app
  RUN mvn -s /usr/share/maven/ref/settings-docker.xml package
  
  FROM openjdk:8-jre
  EXPOSE 8080 5005
  COPY --from=BUILD /usr/src/app/target /opt/target
  WORKDIR /opt/target
  ENV _JAVA_OPTIONS '-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'
  
  CMD ["java", "-jar", "greeting.war"]
  ```

* In this case, we are using openjdk as the base image and we're using 8-jre as the tag. We don't need the entire JDK or maven or class files, none of that for our production image. So we take open openjdk-8-jre as a base image, we expose a port from the application. 8080 is where our application is running. 5005 is our debug port. And we'll look at that a little bit later. Then the first command is, from the build stage, I'm just copying the cargo directory into a specific directory here, and that OPT target directory is now set up as a work directory. Now remember everything that is generated in my first stage is left behind and only the relevant artifacts are copied from first stage, my development stage, to my production stage. I set up certain Java options here, this will be used when we are debugging our application later. And finally I fire up my application using Java -jar and greeting.war because this greeting.war exists in OPT target directory.

  ```bash
  # Build Docker Image using Dockerfile
  #Code 
  # Change to app directory
  # Show and explain multi stage Dockerfile
  #Create local repo:  . This command is going to download the maven repository, all the dependencies, and package them locally into one file.
  mvn -Dmaven.repo.local=./repository clean package
  tar cf repository.tar.gz ./repository
  # Create Docker Image
  docker image build -t syr7s7greeting
  # Show docker image
  docker image ls
  ```

* In my JavaExInKubernetes directory, I have a repository folder. This is my entire maven repository that needs to be packaged into the base image itself.

  ![k8s10](images/k8s10.png)

*  all the build and the production images are done using a single Dockerfile, using a multi stage Dockerfile

