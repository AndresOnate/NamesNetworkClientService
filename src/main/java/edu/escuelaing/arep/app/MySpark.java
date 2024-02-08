package edu.escuelaing.arep.app;


import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class MySpark
{

    private static String serviceURI = "";
    private static Function service = null;
    private static Boolean running = false;
    private static MySpark _instance = new MySpark();

    private MySpark(){}

    public static MySpark getInstance(){
        return _instance;
    }


    public void runServer( String[] args ) throws Exception
    {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        
        running = true;
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
            URI requestURI = new URI(uriStr);
            try {
                if(requestURI.getPath().startsWith("/action")){
                    outputLine = callService(requestURI);
                }else{
                    outputLine = httpResponse(requestURI);
                }
            }catch (Exception e){
                e.printStackTrace();
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

    private String callService(URI requestURI) {
        String serviceURI = requestURI.getPath().substring(7);
        String output = "Error, No encuentra la función";
        System.out.println(serviceURI);
        System.out.println(this.serviceURI);
        if(this.serviceURI.equals(serviceURI)){
            output = service.handle(requestURI.getQuery());
        }
        return  "HTTP/1.1 200 OK\r\n"
                + "Content-Type:text/html\r\n"
                + "\r\n" + output;

    }



    public static void  get(String path, Function svc) throws Exception {
        String[] args = {};
        serviceURI = path;
        service = svc;

    }


    public static String httpResponse(URI requestedURI) throws IOException{
        Path file = Paths.get("target/classes/public" + requestedURI.getPath());
        String outputLine =
                "HTTP/1.1 200 OK\r\n"
                        + "Content-Type:text/html\r\n"
                        + "\r\n";
        Charset charset = Charset.forName("UTF-8");
        BufferedReader reader = Files.newBufferedReader(file, charset);
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            outputLine = outputLine + line;
        }
        return  outputLine;
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
