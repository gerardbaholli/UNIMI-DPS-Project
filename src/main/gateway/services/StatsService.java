package services;

import beans.Count;
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


    // TODO: getLastNStats(int n){} FIX!!!
    @Path("lastNStats")
    @POST
    @Produces({"application/json", "application/xml"})
    public Response getLastNStats(Count number){
        int n = number.getN();
        return Response.ok(Stats.getInstance().getLastNStats(n)).build();
    }


    // TODO: getStanDevAvg(int n){} FIX!!!
    @Path("standardDevAvg")
    @POST
    @Produces({"application/json", "application/xml"})
    public Response getStanDevAvg(Count number){
        int n = number.getN();
        return Response.ok(Stats.getInstance().getStanDevAvg(n)).build();
    }

}
