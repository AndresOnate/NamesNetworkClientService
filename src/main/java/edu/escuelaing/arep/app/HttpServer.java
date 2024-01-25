package edu.escuelaing.arep.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class HttpServer 
{
    public static void main( String[] args ) throws Exception
    {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        
        Boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
    
            
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String uriStr = "";
    
            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            if(uriStr.startsWith("/cliente")){
                outputLine = httpClientHtml();
            }else{
                outputLine = httpError();
            }

            out.println(outputLine);

                    
            out.println();
    
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String httpClientHtml(){

        String outputLine = 
        "HTTP/1.1 200 OK\r\n"
        + "Content-Type:text/html\r\n"
        + "\r\n"
        + "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Form Example</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>Form with GET</h1>\n" +
            "        <form action=\"/hello\">\n" +
            "            <label for=\"name\">Name:</label><br>\n" +
            "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
            "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
            "        </form> \n" +
            "        <div id=\"getrespmsg\"></div>\n" +
            "\n" +
            "        <script>\n" +
            "            function loadGetMsg() {\n" +
            "                let nameVar = document.getElementById(\"name\").value;\n" +
            "                const xhttp = new XMLHttpRequest();\n" +
            "                xhttp.onload = function() {\n" +
            "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
            "                    this.responseText;\n" +
            "                }\n" +
            "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
            "                xhttp.send();\n" +
            "            }\n" +
            "        </script>\n" +
            "\n" +
            "        <h1>Form with POST</h1>\n" +
            "        <form action=\"/hellopost\">\n" +
            "            <label for=\"postname\">Name:</label><br>\n" +
            "            <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n" +
            "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
            "        </form>\n" +
            "        \n" +
            "        <div id=\"postrespmsg\"></div>\n" +
            "        \n" +
            "        <script>\n" +
            "            function loadPostMsg(name){\n" +
            "                let url = \"/hellopost?name=\" + name.value;\n" +
            "\n" +
            "                fetch (url, {method: 'POST'})\n" +
            "                    .then(x => x.text())\n" +
            "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
            "            }\n" +
            "        </script>\n" +
            "    </body>\n" +
            "</html>";
        return outputLine;
    }

    public static String httpError(){
        String outputLine = "HTTP/1.1 400 Not Found\r\n"
        + "Content-Type:text/html\r\n"
        + "\r\n"
        + "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Form Example</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    </head>\n" +
            "    <body>\n" +  
                    "<h1>Error</h1>" +         
            "    </body>\n" +
            "</html>";
        return outputLine;
    }
}
