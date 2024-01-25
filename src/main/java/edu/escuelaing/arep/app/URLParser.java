package edu.escuelaing.arep.app;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class URLParser 
{
    public static void main( String[] args ) throws Exception
    {
        URL mySite = new URL("http://estudiantes.escuelaing.edu.co:80/users?id=103#notas");
        System.out.println( "Protocol " + mySite.getProtocol());
        System.out.println( "Authority " + mySite.getAuthority());
        System.out.println( "Host " + mySite.getHost());
        System.out.println( "Port " + mySite.getPort());
        System.out.println( "Path " + mySite.getPath());
        System.out.println( "Query " + mySite.getQuery());
        System.out.println( "File " + mySite.getFile());
        System.out.println( "Ref " + mySite.getRef());

        URI myURI = new URI("/client?t=67&0=25");
        System.out.println( "Path " + myURI.getPath());
        System.out.println( "Query " + myURI.getQuery());

    }
}
