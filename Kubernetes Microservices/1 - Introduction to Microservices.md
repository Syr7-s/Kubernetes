### Introduction to Microservices

#### Microservices 101

- Microservices are a variant of service-oriented architecture that structures the entire application as a collection of loosely coupled services.

- #### Benefits of Microservices

- By breaking up a monolithic service into many smaller, independent services that work together, developers have more freedom to create, manage, and deploy the individual components without compromising the integrity of the entire application.

- Because the surface area of each individual service is smaller, it's easier to plug in this approach into a continuous integration pipeline.

- Which enables continuous delivery. Microservices allow for a more resilient application over all. If one service fails, the whole application shouldn't fail hard and other components should still continue to work. This allows you to isolate faults in a better manner and make your overall application more fault tolerant.

  ![k8s1](images/k8s1.png)

- Typically, microservices are broken up with the intent to solve a specific business need or enable a business capability.

- The structure makes it easier to build services that can be clearly communicated to the business side of the organization.

- It's easier to justify technical work when it's related directly to specific business need.

- For greenfield opportunities, there's an initial learning curve and many additional facets to set up with microservices that you won't encounter with monoliths. As you continue development you start to build a more complex system. You'll need more organizational knowledge of the inner workings of the monolith. This is different in microservices architecture, where you'll need to know less upfront. Later, when you have a rather large codebase or larger teams, Chances are, you'll end up being more productive using the microservice architecture, rather than trying to add features or debug a large monolith.

  ![k8s2](images/k8s2.png)

- if you started with a microservice architecture up front, you'll end up seeing rewards later on in the lifecycle of the application.

- Building microservice forces you think to about applications in a more modular way.

- Build a microservice - based application using Kubernetes as the microservice platform

#### Benefits of a Microservices Architecture

* Language Independence

* Breaking up the application into multiple subcomponents that interoperate with each other but run independently buys you technical independence. You're allowed to use different languages or even different databases behind the scenes for different microservices in the application.

  ![k8s3](images/k8s3.png)

* #### Avoid Bottlenecks with Scaling

* When it comes to scaling, there's always some part of an application that is invoked by a lot of different modules or is the bottleneck for the rest of the application. 

* Authentication and token management is often called a lot from the rest of the code.

* Using microservices won't help me optimize the out modules, but it'll certainly help me scale them. I can build a login an token management API separately as a single service, and then scale these out independently as an app. Kubernetes also helps with scaling.

  ![k8s4](images/k8s4.png)

* #### Deployment Benefits

* Deploying your individual microservice also becomes easier in this new pattern

* Changes to an out API can be made and deployed whenever they are ready without having to update the rest of the application.

* **There are several advantages to this**

  * You can get your updates to production mode quickly.
  * The risk of rolling out a smaller changeset is much less than having to deploy the entire application. You get to also play with some new ideas like blue-green or A/B deployments.
  * You are able to replace components in a more efficient way. Building services in this manner also open you up to a lot of new technologies

  ![k8s5](images/k8s5.png)

* ### New Opportunities

* Apply microservices approach to containers and serverless paradigms

* Serverless paradigms and containers introduce new tooling and technologies that benefit from a microservices approach.

#### Common Microservices Patterns

* The Twelve-Factor App :  The twelve-factor methodology was initially proposed for building software as a service applications for Heroku.

* However, almost all of the principles covered by that methodology translate to the micro services and container world.

* Understanding how to build applications in a lean and agile way will help you build micro services in a scalable and maintainable way

  ![ks86](images/k8s6.png)

* **Principle 1:  Codebase** 

* The codebase must be tracked in version control and will have many deploys.

* For most folks, this isn't a new thing. Code should be tracked in a single repo backed by a version control, and you'll probably have many deployments of the same application, like dev, staging, production, et cetera.

* **Principle 2: Dependencies**

* Dependencies are explicitly declared and isolated.

* We write a lot of code and love to reuse it. I'm as guilty as anyone else for copy pasting pieces of code, but these days. should I rewrite this as a function? Or two, should I convert this to a library that can be called by anyone? There's an extra step with sharing libraries. You'll need to use a dependency management strategy for this. Like in Java, you might use Maven or in Ruby, a Gemfile.

  ![k8s7](images/k8s7.png)

* **Principle 3: Configuration**

* Store configuration in the environment

* Application configuration should never go into source code, but rather should be part of the environment. This is true whether it's a config file or uses environment variables.(Add configuration via environment variables or config files)

* **Principle 4: Backing Services**

* Treat backing services as an attached resource

* Backing services can be a MySQL database, an SMTP email server, or a third-party service like a Twitter bot, et cetera. All of these are treated the same way and can be attached or detached from deploys at will. 

  ![k8s8](images/k8s8.png)

* **Principle 5: Build, Release, Run**

* Build, deploy and run.

* It promotes having a build and release strategy before getting an application into a running state. The importance here is not the tooling, but rather the ability to have repeatable builds, versioning on your running system, and a way to release or rollback

  ![k8s9](images/k8s9.png)

* **Principle 6: Processes**

* Execute the application as a stateless process

* Of all the factors, I think processes is the one that is the hardest to fully understand. It states that the entire application is executed as one or more stateless processes. For those working in large enterprises, this might be harder to implement because of existing infrastructure that is already laid out in terms of caching or sticky sessions. These ideas may need to be revisited and reimplemented in a different manner. 

  ![k8s10](images/k8s10.png)

* **Principle 7: Port Binding**

* Expose services via port bindings

* Enterprises already map applications in a specific way, and you may need to revisit the way these are designed to account for using port bindings.

* **Principles 8,9 and 10**

* Concurrency: Scale out with the process model.

* Disposability: Qucik application startup and shutdown times

* DEV/PROD parity : Application is treated the same way in dev, staging and production

* **Principles 11 and 12**

* The last two principles come back to concerns that are fundamental once the app is released.

* Log Management :

  * Treated as an event stream

* Admin Tasks: 

  * Treated the same way like the rest of the application
  * Are allowed to run against a released version

* Implement the twelve-factor principles in Kubernetes

