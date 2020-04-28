package net.studionotturno.backend_ICookbook.handler;


import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;

/**
 * Fa parte del desing patter Chain of responsability per le chiamate dal client;<br/>
 * questo concrateHandler realizza le elaborazioni da eseguire in base alle richieste
 * post delle ricette
 *
 * @see Handler
 * @see net.studionotturno.backend_ICookbook.controllers.RecipeController
 *
 * */
public class RecipePostHandler implements Handler {

    private HttpServletRequest request;
    private Document body;
    private String email;

    /**
     * Costruttore; vengono estrapolati alcuni dati dalla request per essere usati nei metodi;
     * per i metodi nel costruttore viene effettuata una ricerca dinamica e vengono usati i metodi
     * del'interfaccia Handler con metodi default(con corpo)
     *
     * @see MongoDBConnection
     * */
    public RecipePostHandler(HttpServletRequest request, Handler next)  {
        this.request=request;
        this.body=getBody(request);
        this.email=getEmail(request.getHeader("header"));
    }

    @Override
    public ResponseEntity<?> elab(){
        String opt=this.request.getRequestURI();
        if(opt.contains("docu/get_single_documents/")) return getSingleDocument();
        else if(opt.contains("docu/get_documents/recipes/")) return getDocumentID();
        else if(opt.contains("docu/post_documents/")) return postDocument();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Si effettua una query sul DB per reperire una specifica ricetta di un utente;
     * un utente autenticato può reperire una propria ricetta memorizzata sul cloud
     * @return Una ricetta in formato intero
     * @see MongoDBConnection
     */
    public ResponseEntity<?> getSingleDocument(){
        FindIterable<Document>  f= MongoDBConnection.builder().setCollection("recipes")
                .getDocumentQuery(and(eq("userName",this.email),eq("recipeName",this.body.get("recipeName"))));
        return ResponseEntity.ok(f.first());
    }

    /**
     * Si effettua una query sul DB per reperire una specifica ricetta di un utente;
     * un utente autenticato può reperire una propria ricetta memorizzata sul cloud
     * @return Una ricetta in formato intero
     * @see MongoDBConnection
     */
    public ResponseEntity<?> getDocumentID(){
        FindIterable<Document>  f= MongoDBConnection.builder().setCollection("recipes").
                getDocumentQuery(and(eq("userName",this.email),eq("recipeName",this.body.get("recipeName"))));
        return ResponseEntity.ok(f.first().get("_id").toString());
    }


    /**
     * Memorizzazione di una ricetta proveniente dal cliet<br/>
     * Si effettua una query sul DB per controllare se la ricetta è già presente
     * Se lo è la ricetta viene rimpiazzata,altriemei viene salvata per la prima volta
     *
     * @return ritorna un valore booleano che indica se l'inserimento è avvenuto con successo,false altrimenti
     * @see MongoDBConnection
     */
    public ResponseEntity<?> postDocument(){
        Bson bson=and(eq("userName",this.body.get("userName")),eq("recipeName",this.body.get("recipeName")));
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("recipes").getDocumentQuery(bson);
        try{//se non da errore vuol dire che una ricetta con lo stesso nome è già presente,e va quindi modificata in tronco
            f.first().toString();
            MongoDBConnection.builder().setCollection("recipes").deleteDocumentQuery(bson);
            return ResponseEntity.ok().body(MongoDBConnection.builder().setCollection("recipes").insertData(this.body));
        }catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
            return ResponseEntity.ok().body(MongoDBConnection.builder().setCollection("recipes").insertData(this.body));
        }
    }

}
