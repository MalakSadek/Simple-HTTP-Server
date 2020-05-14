# Simple-HTTP-Server

A simple HTTP server in Java 🌐 📂 (2019)

This is an HTTP server implemented in Java. It supports HTTP GET, HEAD and DELETE requests from multiple clients through threading, and responses appropriately to connecting clients using the 200 OK, 204 No Content, 404 Not Found and 501 Not Implemented codes. It also supports text files such as text/html and binary images such as `images/jpg`, `images/jpeg`, `images/png` and `images/gif`. The server logs every request to a .txt file.

A short report of the advanded requirements can be read here.

### Installation

Clone the project: 
            `git clone https://github.com/MalakSadek/Simple-HTTP-Server`

Cd into the directory and compile the files, then run the source code:

            cd Java-Web-Server-HTTP-Requests
            javac src/*.java src/exception/*.java

## Usage

### Server

Run the server:

            cd src/
            java WebServerMain <document_root> <port_number> <max_number_of_active_threads>

where:

* document_root is the root directory from which the server will serve documents.
* port_number is the port on which the server will listen.
* <max_number_of_active_threads> is the maximum number of clients that can connect simultaneously to the server.

Example: `java WebServerMain ../www 12345 100`

A testing web root directory with html and binary image files is provided in the www directory.

    Terminal
        GET request method
            headers: curl -s -I -X GET localhost:12345/index.html
            text content: curl -s -X GET localhost:12345/index.html
        DELETE request method
            headers: curl -s -I -X DELETE localhost:12345/delete_me.txt
            content: curl -s -X DELETE localhost:12345/delete_me.txt
        404 Error: curl -s -X GET localhost:12345/test.html (File not found)
        501 Error: curl -X SLURP localhost:12345/index.html (POST not implemented)
    Web browser
        Default page (GET): open http://127.0.0.1:12345/
        Binary image content: open http://127.0.0.1:12345/stars.gif

# Javadocs

    1. Generate the Javadocs: javadoc -d javadoc src/*.java src/exception/*.java

    2. Open javadoc/index.html in your web browser.
    
    
# Contact

* email: mfzs1@st-andrews.ac.uk
* LinkedIn: www.linkedin.com/in/malak-sadek-17aa65164/
* website: https://malaksadek.wordpress.com/

