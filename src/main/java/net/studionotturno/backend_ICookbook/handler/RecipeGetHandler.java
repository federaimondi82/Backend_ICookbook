package net.studionotturno.backend_ICookbook.handler;


import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.LazyResource;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Fa parte del desing patter Chain of responsability per le chiamate dal client;<br/>
 * questo concrateHandler realizza le elaborazioni da eseguire in base alle richieste get
 *
 * @see Handler
 * @see net.studionotturno.backend_ICookbook.controllers.RecipeController
 * */
public class RecipeGetHandler implements Handler {

    private HttpServletRequest request;
    private Document body;
    private String email;

    /**
     * Costruttore; vengono estrapolati alcuni dati dalla request per essere usati nei metodi;
     * per i metodi nel costruttore viene effettuata una ricerca dinamica e vengono usati i metodi
     * del'interfaccia Handler con metodi default(con corpo)
     *
     * @see Handler
     * */
    public RecipeGetHandler(HttpServletRequest request, Handler next)  {
        this.request=request;
        this.body=getBody(request);
        this.email=getEmail(request.getHeader("header"));
    }

    @Override
    public ResponseEntity<?> elab(){
        String opt=this.request.getRequestURI();
        if(opt.contains("docu/get_documents/")) return getDocuments();
        else if(opt.contains("docu/get_documents/lazy")) return getLazyDocuments();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Si effettua una query sul DB per reperire tutte le ricette di un utente
     * @return Una lista oggetti Document pieni di ricette
     *
     * @see MongoDBConnection
     */
    public ResponseEntity<?> getDocuments(){
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("recipes").getDocumentQuery(eq("userName",this.email));
        return ResponseEntity.ok(f.into(new ArrayList<>()));
    }

    /**
     * Si effettua una query sul DB per reperire tutte le ricette di un utente; avviene la
     *  trasformazione in formato Lazy delle ricette
     * @return Una lista oggetti Document pieni di ricette Lazy
     *
     * @see MongoDBConnection
     */
    public ResponseEntity<?> getLazyDocuments(){
        FindIterable<Document> f= MongoDBConnection.builder()
                .setCollection("recipes").getDocumentQuery(eq("userName",this.email));

        List<LazyResource> list = new ArrayList<>();
        for(Document el : f.into(new ArrayList<>())){
            list.add(new LazyResource().setDocumentID(el.get("_id").toString())
                    .setRecipeName(el.get("recipeName").toString())
                    .setExecutionTime(Double.parseDouble(el.get("executionTime").toString()))
            );
        }
        return ResponseEntity.ok(list);
    }

}
