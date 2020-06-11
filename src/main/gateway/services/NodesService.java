package services;

import beans.Node;
import beans.Nodes;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("nodes")
public class NodesService {

    //restituisce la lista di nodi
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getNodesList(){
        return Response.ok(Nodes.getInstance()).build();
    }

    //permette di inserire un nodo (id, ipAddress, port)
    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addNode(Node n){
        String result = Nodes.getInstance().add(n);
        if (result!=null){
            if (result.equals("Success"))
                return Response.ok(Nodes.getInstance()).build();
            else if (result.equals("Warning"))
                return Response.status(Response.Status.CONFLICT).build();
            else if (result.equals("Fail"))
                return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response deleteNode(Node n){
        String result = Nodes.getInstance().delete(n);
        if (result.equals("Success"))
            return Response.ok().build();
        else if (result.equals("notFound"))
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }


}
