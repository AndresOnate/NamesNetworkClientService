
package main.java.edu.escuelaing.arep.app;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class URLReader {

    public static void main(String[] args) throws Exception {

        String site = "http://www.google.com/";
        URL siteURL = new URL(site);
        URLConnection urlConnection = siteURL.openConnection();
        // Obtiene los campos del encabezado y los almacena en un estructura Map
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if(headerName !=null){System.out.print(headerName + ":");}
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                System.out.print(value);
            }
            System.out.println("");
        }


        
        System.out.println("-------message-body------");
        URL google = new URL("http://www.google.com/");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
            }
        }
}