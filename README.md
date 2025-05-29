# BrAPI Java Test Server

## Server Usage
This server implements all BrAPI calls. It is backed by a custom database with dummy data.

* BrAPI V1 Base URL (v1.0, v1.1, v1.2, v1.3): [test-server.brapi.org/brapi/v1/](https://test-server.brapi.org/brapi/v1/)
* BrAPI V2 Base URL (v2.0, v2.1): [test-server.brapi.org/brapi/v2/](https://test-server.brapi.org/brapi/v2/)

Use [/calls](https://test-server.brapi.org/brapi/v1/call) (V1) or [/serverinfo](https://test-server.brapi.org/brapi/v2/serverinfo) (V2) to check the available endpoints.

## Prerequisites
* Maven 3.9
* Java 21
* Postgres 17.2

## Auth Configuration
BrAPI has provided a [sample central authentication service for the test server](https://brapi.org/oauth).

Here you can create a user and login to be presented with a token which can be used to make requests to your sample server implementation.

Why offer auth? Apart from security concerns, you can utilize authentication with the BrAPI spec to deliver extra functionality
tailored to what data you want your users to see.

To utilize the sample central auth service, set the following properties in your `application.properties` file:
```
security.oidc_discovery_url=https://test-server.brapi.org/.well-known/openid-configuration
security.issuer_urlhttps://auth.brapi.org/realms/brapi
```

If you are not using the sample BrAPI auth system, you must configure these variables properly with the endpoints they expect for your service.

The [local authorization docker set up](#self-contained-authorization-implementation) has some details about how to find these values.

For instructions on how to send the authentication token in your request, see [this section](#authenticating-a-request).

For more information detailing the authentication of the BrAPI Test Server, more documentation with examples and diagrams
can be found [here](https://plant-breeding-api.readthedocs.io/en/latest/docs/best_practices/Authentication.html)
## Run
### Maven
For all run configurations, we will be utilizing **Maven** as our java build and compile tool to build the BrAPI test server.

There are many options to run Maven.  Most are used to running maven in their IDE, however, you can also run it in your command line.

For this project, you will only need to run a maven clean install.

This can be done in the command line with:

`mvn clean install`

You will need to run this operation (either via the command line or your IDE) to recompile the code every time you make changes to the java code.

Now, let's cover some typical run configurations:

### Java IDE
* Checkout the project and open in your favorite Java IDE.
* Run a [maven clean install](#maven)
* Set up an empty database server (Postgres is recommended). Create a new database schema, but do not add any tables. The tables and data will be added on server startup.
* Copy `/src/main/resources/application.properties.template` to `/src/main/resources/properties/application.properties`
* Edit `application.properties`
  * Change `server.port` and `server.servlet.context-path` as needed
  * Change the `spring.datasource.` parameters to match your empty database server and schema
  * If you did not use a Postgres database, change the `spring.datasource.driver-class` to match the database type you have setup (this may require additional dependancies in the POM)
* Run `org.brapi.test.BrAPITestServer.BrapiTestServer.java`

### Docker
To facilitate an understanding of some different BrAPI environment setups, we have provided several different docker
implementations for you to look at.

All of these different container configurations utilize the same `Dockerfile`, which positions the jar and exposes
ports for utilization by the host machine.

#### Development (No Auth) Implementation
To speed up getting started with docker, you can forgo an authentication implementation entirely just to get up and running.

We have provided `docker-compose-dev.yaml`, which is a stripped down container orchestration without any connection to an auth service.

This requires no access to images, and forces you to build the docker image locally and use it.

After building the app with [maven](#maven), simply run

`docker compose -f .\docker-compose-dev.yaml build`

to build the images, and then

`docker compose -f .\docker-compose-dev.yaml up`

and this should bring up the BrAPI test server containers via docker.

With this configuration you will have trouble issuing post requests without authentication.

To get around this, you can use [the dummy tokens](#dummy-user-tokens) provided.
#### External Authorization Implementation
The container implementation provided by `docker-compose.yaml` for standing up a pipeline in something like Jenkins.

It involves pulling images uploaded to Docker Hub.

If you don't know how to do this, docker has a lot of [documentation](https://docs.docker.com/get-started/docker-concepts/the-basics/what-is-an-image/)

* Set up an empty database server (Postgres is recommended). Create a new database schema, but do not add any tables. The tables and data will be added on server startup.
* Download [application.properties.template](/src/main/resources/application.properties.template) and rename the file `application.properties`
* Save this file on the docker host as `/<Local_Path_To_Properties>/application.properties`
* Edit `application.properties`
  * Change `server.port` and `server.servlet.context-path` as needed (port 8080 is exposed in the dockerfile by default)
  * Change the `spring.datasource.` parameters to match your empty database server and schema
  * If you did not use a Postgres database, change the `spring.datasource.driver-class` to match the database type you have set up (this may require additional dependencies in the POM, and a fresh build of the docker image)
* Docker Pull `docker pull docker-username/image-name:image-tag`
* Docker Run `docker run -v /<Local_Path_To_Properties>/:/home/brapi/properties -d docker-username/image-name:image-tag`

#### Self-Contained Authorization Implementation
The `docker-compose-local-auth.yml` has the containers necessary for a local, self-contained authorization implementation.

This implementation allows you to set up a central authentication server via keycloak
and experiment with it locally without introducing an external service.

Why?  User management is a very nifty and important feature of the BrAPI spec.  You can tailor your server implementation to
retrieve specific data based off of the user that asks for it, providing an individualized experience for each user in the system,
or if your authentication service is flexible enough, you can define roles and groups/buckets of users that will fetch data pursuant to that definition. (keycloak offers this out of the box :eyes:)

This setup contains four containers:
* **brapi-java-server-v2**: The BrAPI server.  You can hit the server in your browser with http://localhost:8080/brapi/v2/
* **keycloak-brapi**: The keycloak central auth server for the BrAPI app.  Once hosts are configured, you should be able to hit this with your browser with http://keycloak-brapi:8008 
* **brapi-db**: The DB for the BrAPI server. Contains all relevant data to the BrAPI spec.
* **keycloak-db**: The DB for the keycloak server.  This contains all relevant data to the authentication system as you will define it in keycloak.

Since this is a local self-contained setup, you will need to build the brapi images yourself.

First thing's first: [run a maven clean and install](#maven) to build and compile the BrAPI
test server code.

Next, build the docker image locally with:

`docker compose -f .\docker-compose-local-auth.yml build`

Finally run all the containers in the compose with:

`docker compose -f .\docker-compose-local-auth.yml up`

In this setup, because we've defined the `KC_HOST` as `keycloak-brapi` in the docker-compose file, we will need to modify
your local host machine's `hosts` file must be updated to point this host to your local default IP address.

On most machines, this address is `127.0.0.1`.

Your `hosts` file's location will vary depending on machine and operating system, so to find where that location is to
edit the file, it's recommended you do a quick internet search to find where it is.

Once located, just add the following line:

`127.0.0.1 keycloak-brapi`

This will redirect your browser to hit the url on your local machine you should be able to hit the `keycloak-brapi` endpoint in your web browser
to access the keycloak admin console.

##### Utilizing keycloak with the Test Server
With the containers up, you can now configure your authentication for this keycloak server of the brapi app.

A good resource on how to do this is [the keycloak docs](https://www.keycloak.org/docs/latest/server_admin/).

The basics are login using credentials in the `docker-compose-local-auth.yml` to http://keycloak-brapi:8008.

This will take you to the master realm console, where you can create new realms and configure them any way you find interesting in the docs above.

For the purposes of demonstrating a basic authentication pattern, the things you really need to accomplish are:
1. Create a realm (keycloak docs should cover this).
2. Create a user inside of that realm and give that user some credentials (keycloak docs should cover this).  Be sure to make it so that the user doesn't need to reset their password the first time they log in, unless you are interested in exploring that flow.
3. Create an OpenID Connect client inside of that realm.  Give that client a name and ID, and for the purposes of this demonstration choose `Client Id and Secret` as your Client Authenticator method.  Create or generate a client secret.  Copy both the Client Id and the Client secret somewhere, you will need them to request a token. 
4. Locate the url which contains the open id connect auth information, and assign it to the `security.oidc_discovery_url` property in your application properties file. Per the `application.properties.template`, this typically looks like https://example.com/auth/.well-known/openid-configuration. With realm created in keycloak it would look like: http://keycloak-brapi:8008/realms/realm-name/.well-known/openid-configuration
5. Open the url from step 4 in your browser. Locate `"issuer"` element in the json displayed,  and copy the url and assign it to the `security.issuer_url` property in your `application.properties` file.
6. With the url from 4 still open your browser, locate the `"token_endpoint"` element in the json displayed.  Take note and copy this url somewhere; it will be utilized to get your token.
7. Obtain a way to make requests to the token endpoint.  You can accomplish this with many tools, like `curl` in the command line, or if you prefer a GUI to make requests you can get Postman or Insomnia.

With all of these things in hand, you are now ready to make requests to the brapi test server and utilize authentication.

##### Generating a token from keycloak
To do this, first you need to obtain a token to make requests with.  

If you are using curl, the following command should do the trick:

`curl -d "client_id=your-client-id" -d "client_secret=your-client-secret"   -d "username=username-of-user-created" -d "password=credential-of-user-you-created"   -d "grant_type=password"   "http://your-token-endpoint:8008"`

This will come back with a JSON response containing all the token information.  You will have to search for just the token, which can be found in the json under the element `"access_token"`

You can also utilize another command line tool `jq` to pipe and grab the element so you don't need to search for it every time.  Once you have `jq`, this can be done with:

`curl -d "client_id=your-client-id" -d "client_secret=your-client-secret"   -d "username=username-of-user-created" -d "password=credential-of-user-you-created"   -d "grant_type=password"   "http://your-token-endpoint:8008" | jq .[\"access_token\"]`

If you aren't using curl, simply make sure that the headers depicted in the above curl command are being sent in your request with the right information gathered in the steps above.

Congrats!  You finally have a token, and now you can utilize it with any requests you send to the BrAPI test server.

##### Sending a request with a user token
To test the user authentication functionality, find a POST endpoint in the BrAPI test server that you have an interest in inserting data into.

You can attach the auth token to a request and send it by following the steps in the [Testing the Server](#testing-the-server) section below.

After you've been successful in authenticating requests locally, the next step is setting up your own authentication provider (some single sign on service like Okta for example) to act as the
entrypoint for your users to get into the system and interact with the BrAPI test server in a safe, user-oriented way.

The containerization of all of these pieces should also give you a sense of all the different utilities you will need to support
this kind of architecture.

## Testing the Server
Once running, a good landing page for you to check out is:

http://localhost:8080/brapi/v2

This gives some examples endpoints to hit which should return some values with the dummy data installed by the server the first time it runs.

This works just fine if you want to see all the publicly avialable data, but if you have configured the server to utilize user authentication properly,
you can also send requests with user tokens to only retrieve the data you want your users to see.

### Posting Data to the Server
All POST requests for the BrAPI test server require user authentication to insert the data and relate it to a user.

#### Authenticating a request
The BrAPI test server uses a standard `Bearer Token` authorization header to read and perform authentication for the requests it receives.

The name of the header is `Authorization` and the value of the header looks like `Bearer your-token`.

Most GUI programs for sending requests like Postman and Insomnia have options inside of them which auto-populates these headers,
you just need to provide the token from your auth provider for it to work (They even have integrations to get the tokens from your defined auth service).

But, if you are just using a command line tool like `curl`, now you know what the header looks like.

#### Sending the POST
Once you have this header filled, choose an entity in the UML diagram you want to insert data into.

The more top-level the entity is, the better time you will have in getting your first POST to go through.  `Program` is a good entity to start with.

You will want to find the associated Request POJO for the entity to understand what fields you need to provide to insert a new entity.

Once you do, you will need to send the fields in JSON body.

Here is an example for the program entity:

```
[
	{
		"abbreviation": "JBPM",
		"commonCropName": "Maize",
		"documentationUrl": "http://localhost/jbpm",
		"leadPersonDbId": "list_person_1",
		"leadPersonName": "Bob Robertson",
		"objective": "Determine Kernel Count",
		"programName": "Kernel Count Program",
		"programType": "1",
		"fundingInformation": "Not a lot"
	}
]
```

Finally, with the body and the auth header in hand, you can post to the endpoint of your entity (in this case http://localhost:8080/brapi/v2/programs),
and your entity will be saved and related to the user the token was based with.

You can then view the entity was posted successfully with a GET on the same entity endpoint.

#### Dummy User Tokens

There are several dummy user tokens provided by the test server:

* `XXXX` - A token that grants you access to a dummy user
* `YYYY` - A token that grants you access to a dummy admin
* `ZZZZ` - A token that grants you access to a dummy anonymous user.

You can use any of these tokens to bypass the authentication set up with your BrAPI server implementation.

## Debug

The BrAPI test server can be easily debugged in the two main run configurations we have discussed.

### Java IDE

Following the steps in the [run configuration](#java-ide), if you right-click on the run/play button for the main method in the
`BrapiTestServer` class, you should also have an option to debug.

Once you do that, the application will run in debug mode and you should be able to breakpoint any requests that come through.

### Docker

In this configuration you can attach a remote JVM Debug listener to the exposed JVM debug port, 5005.

Most IDEs offer easy ways to attach these remote listeners.  Simply create a run configuration in your IDE and search for
`Remote JVM Debug`, and specify the port as 5005.

Turn it on while the BrAPI java test server container is running, and you should be able to breakpoint requests.

## DataBase

The database is created automatically at run time by [flyway](https://documentation.red-gate.com/flyway).
SQL-based migrations under `src/main/resources/db/migration` or `src/main/resources/db/sql`, and Java-based migrations under `src/main/java/org/brapi/test/BrAPITestServer/db/migration` will be run.
The `src/main/resources/db/migration` directory is for schema changes, while `src/main/resources/db/sql` is for seed data. Modify `spring.flyway.locations` in `application.properties` to change what flyway runs.

When running in Docker, you can edit or add seed data by adding the volume `-v /<Local_Path_To_SQL>/:/home/brapi/sql` to your docker command. 

Below is a UML diagram of the whole database schema:

![dbSchema](https://raw.githubusercontent.com/plantbreeding/brapi-Java-TestServer/brapi-server-v2/brapi_test_server_data_model_v2.0.svg)

### ID Columns
The ID columns of each of the entities defined in the test server were created to be extremely flexible with whatever implementation
you would like to use.  By default, the ID columns are mapped to String-like fields in the DB to accommodate that flexibility.

If you decide to use this server implementation in a production like environment, **it is highly advised that you change this**.

Most modern applications use UUID type columns, which are supported by most if not all relational databases.

To change this, you would want to take a close look at the `BrAPIBaseEntity` class, which essentially all entities extend from.
