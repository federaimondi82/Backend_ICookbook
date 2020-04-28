package net.studionotturno.backend_ICookbook.controllers;

import net.studionotturno.backend_ICookbook.domain.IPassChecker;
import net.studionotturno.backend_ICookbook.handler.Handler;
import net.studionotturno.backend_ICookbook.handler.RecipeDeleteHandler;
import net.studionotturno.backend_ICookbook.handler.RecipeGetHandler;
import net.studionotturno.backend_ICookbook.handler.RecipePostHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Controller dedicato a tutte le chiamate riguardo alle ricette dell'utente
 * Fa parte del desing patter Chain of responsability per le chiamate dal client per le ricette
 * Per ogni endpoint vi è un metodo specifico per soddisfare la richiesta
 *
 * @see Handler
 * @see RecipeGetHandler
 * @see RecipeDeleteHandler
 * @see RecipePostHandler
 *
 * */
@RestController
public class RecipeController {

    //#region metodi delete

    /**
     * Cattura la richiesta del client per cancellare una ricetta dal database, la ricetta deve essere di uno specifico utente
     *
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipeDeleteHandler#deleteSingleDocument() deleteSingleDocument}
     *
     * @see RecipeDeleteHandler
     */
    @PostMapping(value="docu/delete_documents/")
    public ResponseEntity<?> deleteSingleDocument(HttpServletRequest request) {
        Handler handler= new RecipeDeleteHandler(request,null);
        return handler.elaborate(request);
    }

    //#endregion metodi delete

    //#region metodi get

    /**
     * Cattura la richiesta del client per avere la lista delle proprie ricette, nel formato intero,
     * memorizzate in cloud.
     * Ritorna tutti i documenti dei un utente sotto forma di una lista di documenti json
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipeGetHandler#getDocuments() getDocuments}
     *
     * @see RecipeGetHandler
     */
    @GetMapping(value="docu/get_documents/")
    public ResponseEntity<?> getDocuments(HttpServletRequest request) {
        Handler handler=new RecipeGetHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client per avere la lista delle proprie ricette, nel formato lazy,
     * memorizzate in cloud.
     * Ritorna tutti i documenti dei un utente sotto forma di una lista di documenti json
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipeGetHandler#getLazyDocuments() getLazyDocuments}
     *
     * @see RecipeGetHandler
     */
    @GetMapping(value="docu/get_documents/lazy")
    public ResponseEntity<?> getLazyDocuments(HttpServletRequest request) {
        Handler handler=new RecipeGetHandler(request,null);
        return handler.elaborate(request);
    }

    //#endregion metodi get

    //#region metodi post

    /**
     * Cattura la richiesta del client per avere l'ID del documento di una propria ricetta memorizzata sul DB.
     *
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipePostHandler#getDocumentID() getDocumentID}
     *
     * @see RecipePostHandler
     */
    @PostMapping(value="docu/get_documents/recipes/")
    public ResponseEntity<?> getDocumentID(HttpServletRequest request){
        Handler handler=new RecipePostHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client per avere la specifica ricetta, in formato intero,
     * memorizzate in cloud.
     *
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipePostHandler#getSingleDocument() getSingleDocument}
     *
     * @see RecipePostHandler
     */
    @PostMapping(value="docu/get_single_documents/")
    public ResponseEntity<?> getSingleDocument(HttpServletRequest request) {
        Handler handler=new RecipePostHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client per avere salvare una ricetta in cloud.
     *
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link RecipePostHandler#postDocument() postDocument}
     *
     * @see RecipePostHandler
     */
    @PostMapping(value ="docu/post_documents/")
    public ResponseEntity<?> postDocument(HttpServletRequest request) {
        Handler handler=new RecipePostHandler(request,null);
        return handler.elaborate(request);
    }


    //#endregion metodi post

}
