package services;

import beans.Stat;
import beans.Stats;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("stats")
public class StatsService {

    // permette di aggiungere una statistica
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response add(Stat s){
        String result = Stats.getInstance().addStats(s);
        if (result!=null){
            if (result.equals("Success"))
                return Response.ok(Stats.getInstance()).build();
            else if (result.equals("Fail"))
                return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }

    // restituisce tutte le statistiche
    @GET
    @Produces({"application/json", "application/xml"})
    public Response get(){
        return Response.ok(Stats.getInstance()).build();
    }


    @Path("lastStats/{number}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLastNStats(@PathParam("number") int n){
        return Response.ok(Stats.getInstance().getLastNStats(n)).build();
    }


    @Path("standardDevAvg/{number}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getStanDevAvg(@PathParam("number") int n){
        return Response.ok(Stats.getInstance().getStanDevAvg(n)).build();
    }

}
