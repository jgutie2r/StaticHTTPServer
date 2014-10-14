StaticHTTPServer
================

This is a lightweight HTTP server for teaching purposes.

This HTTP server responds to GET requests asking for a particular
resource.

The directory where resources are located, the maximum number of
concurrent requests and the port number are specified in the
config.ini file. Also you can configure the prefix of resources
that require user/password and the name of the file in that
path that stores that user/password information.

This is the config.ini file included in the project:

PATH=/var/web/resources  <br />
NUM_THREADS=500 <br />
SERVER_PORT=8080  <br />
AUTH_PREFIX=private  <br />
CREDENTIALS=.creds  <br />

This means that:

1) The resources are located in the path /var/web  <br />
   If the URI is http://server:8080/path/resource  <br />
   then there should exists /var/web/resources/path/resource  <br />
   in order to have a 200 OK response with the resource
   
2) The server can deal with 500 concurrent requests.

3) The server listen in port 8080

4) If the path starts with private then the server
   sends to the client a 401 message asking for 
   user and password.  <br />
   Sample URL that triggers this behaviour:  <br />
   http://server:8080/privateSite1/index.html
   
5) This is the file name that stores the user
   and password. This should be a text file
   that has a single line with the format:  <br />
   user;password
   
   So in the previous URL example it is assumed
   that there exists the following file:  <br />
   /var/web/resources/privateSite1/.creds  <br />
   
   Each path can have its own .creds file storing
   different user/password.
      
---------------------------------------------

This is part of a teaching subject in Web Applications Programming.

This code serves as starting point to add more features as:

- CGI like behaviour 

- Servlet like behaviour using Reflection API

So it is posible to have basic static HTTP server + CGI + Servlet like
behaviour in a very small jar file (around 20 kb).

----------------------------------------------

Juan Gutierrez Aguado (PhD) <br />
Departamento de Informatica - Computer Science Department <br />
Universidad de Valencia     - Valencia University <br />
Spain



