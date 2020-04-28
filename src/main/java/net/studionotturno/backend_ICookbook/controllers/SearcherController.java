package net.studionotturno.backend_ICookbook.controllers;

import net.studionotturno.backend_ICookbook.handler.Handler;
import net.studionotturno.backend_ICookbook.handler.SearcherHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Classe con responsabilità di gestire le chiamate dal client relative alle ricerche delle ricette
 *
 * @see net.studionotturno.backend_ICookbook.handler.SearcherHandler
 * @see net.studionotturno.backend_ICookbook.iterators.MyIterator
 * @see net.studionotturno.backend_ICookbook.iterators.IterableCollection
 */
@RestController
public class SearcherController {


    /**
     * Cattura la richiesta del client riguardo la ricerca di ricette in base al nome
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link SearcherHandler#searchByName() searchByName}
     *
     * @see SearcherHandler
     */
    @PostMapping(value="docu/iterator/byName/")
    public ResponseEntity<?> searchByName(HttpServletRequest request){
        Handler handler=new SearcherHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client riguardo la ricerca di ricette in base agli ingredienti
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link SearcherHandler#searchByIngredient() searchByIngredient}
     *
     * @see SearcherHandler
     */
    @PostMapping(value="docu/iterator/byIngredient/")
    public ResponseEntity<?> searchByIngredient(HttpServletRequest request){
        Handler handler=new SearcherHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client riguardo la ricerca di ricette in base al tempo di esecuzione
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link SearcherHandler#searchByExecutionTime() searchByExecutionTime}
     *
     * @see SearcherHandler
     */
    @PostMapping(value="docu/iterator/byExecutionTime/")
    public ResponseEntity<?> searchByExecutionTime(HttpServletRequest request){
        Handler handler=new SearcherHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client riguardo la ricerca di ricette in base alla difficoltà
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link SearcherHandler#searchByDifficult() searchByDifficult}
     *
     * @see SearcherHandler
     */
    @PostMapping(value="docu/iterator/byDifficult/")
    public ResponseEntity<?> searchByDifficult(HttpServletRequest request){
        Handler handler=new SearcherHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client riguardo una ricerca su diversi aspetti di una ricerca
     * può contenere sia nomi che ingredienti che tempo di esecuzione
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link SearcherHandler#searchByDifficult() searchByDifficult}
     *
     * @see SearcherHandler
     */
    @PostMapping(value="docu/iterator/byTotalTags/")
    public ResponseEntity<?> searchByTotalTags(HttpServletRequest request){
        Handler handler=new SearcherHandler(request,null);
        return handler.elaborate(request);
    }




}
