package services;

import beans.Word;
import beans.Words;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("words")
public class WordsService {

    @GET
    @Produces({"application/json", "application/xml"})
    public Response getWordsList(){
        return Response.ok(Words.getInstance()).build();
    }

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addWord(Word word){
        Words.getInstance().add(word);
        return Response.ok().build();
    }

    @Path("get/{word}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getDefinition(@PathParam("word") String word){
        String def = Words.getInstance().getDefinition(word);
        if (def!=null)
            return Response.ok(def).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("change")
    @PUT
    @Consumes({"application/json", "application/xml"})
    public Response changeDefinition(Word word){
        String resp = Words.getInstance().changeDefinition(word);
        if (resp.equals("Success"))
            return Response.ok().build();
        else if (resp.equals("notFound"))
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response deleteWord(Word word){
        String resp = Words.getInstance().deleteWord(word);
        if (resp.equals("Success"))
            return Response.ok().build();
        else if (resp.equals("notFound"))
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }


}
