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



This is part of a teaching subject in Web Applications Programming.

This code serves as starting point to add more features as:

- CGI like behaviour 

- Servlet like behaviour using Reflection API

So it is posible to have basic static HTTP server + CGI + Servlet like
behaviour in a very small jar file (around 20 kb).




