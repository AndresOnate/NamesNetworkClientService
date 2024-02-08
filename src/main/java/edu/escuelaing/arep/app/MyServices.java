package edu.escuelaing.arep.app;


import static edu.escuelaing.arep.app.MySpark.get;
public class MyServices {

    public static void main(String[] args) throws Exception {
        get("/hi", (req) -> {return "El query es:" + req;});
        MySpark.getInstance().runServer(args);
    }
}
