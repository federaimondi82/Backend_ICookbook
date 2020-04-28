package net.studionotturno.backend_ICookbook.handler;

import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Fa parte del desing patter Chain of responsability per le chiamate dal client;<br/>
 * questo concrateHandler realizza le elaborazioni da eseguire in base alle richieste
 * sulla cancellazione di ricette
 *
 * @see Handler
 * @see net.studionotturno.backend_ICookbook.controllers.RecipeController
 *
 * */
public class RecipeDeleteHandler implements Handler {

    private HttpServletRequest request;
    private Document body;
    private String email;

    /**
     * Costruttore; vengono estrapolati alcuni dati dalla request per essere usati nei metodi
     * per i metodi nel costruttore viene effettuata una ricerca dinamica e vengono usati i metodi
     * del'interfaccia Handler con metodi default(con corpo)
     * */
    public RecipeDeleteHandler(HttpServletRequest request, Handler next)  {
        this.request=request;
        this.body=getBody(request);
        this.email=getEmail(request.getHeader("header"));
    }

    @Override
    public ResponseEntity<?> elab(){
        String opt=this.request.getRequestURI();
        if(opt.contains("docu/delete_documents/")) return deleteSingleDocument();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    /**
     * Consente di esaudire una richiesta di cancellazione di una ricetta di uno specifico utente
     * @return La risposta a questa chiamata, ritorna true se la cancellazione è avvenuta con successo,false altrimenti
     *
     * @see MongoDBConnection
     */
    public ResponseEntity<?> deleteSingleDocument() {
        return ResponseEntity.ok(MongoDBConnection.builder().setCollection("recipes")
                .deleteDocumentQuery(and(eq("userName",this.email),eq("recipeName",this.body.get("recipeName")))));
    }

}
