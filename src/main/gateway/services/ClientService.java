package services;

import beans.Node;
import beans.Nodes;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("client")
public class ClientService {

    //restituisce numero nodi presenti nella rete
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getNodesNumber(){
        int result = Nodes.getInstance().getNodesNumber();
        return Response.ok(result).build();
    }

    //restituisce ultime n statistiche (con timestamp) di quartiere





    //deviazione standard e media delle ultime n statistiche prodotte dal quartiere
    

}
