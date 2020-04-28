package net.studionotturno.backend_ICookbook.controllers;

import net.studionotturno.backend_ICookbook.handler.Handler;
import net.studionotturno.backend_ICookbook.handler.UserHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe con responsabilità di gestire le chiamate post dal client per quanto riguarda gli utenti.<br/>
 * Fa parte del desing patter Chain of responsability per le chiamate dal client.
 *
 * Per ogni endpoint vi è un metodo specifico per soddisfare la richiesta
 *
 * @see Handler
 * @see UserHandler
 *
 * */
@RestController
public class UserController implements Handler {

    /**
     * Cattura la richiesta del client riguardo ai propri dati;la chiamata viene passata al sottostante handler
     * @param request la richiesta del client
      * @return la risposta ritornata dall'handler sottostante
     *
     * @see UserHandler
     */
    @PostMapping(value="user/getData/")
    public ResponseEntity<?> getData(HttpServletRequest request){
        UserHandler handler =new UserHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client per la registrazione;la chiamata viene passata al sottostante handler
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link UserHandler#registration() registration}
     *
     * @see UserHandler
     */
    @PostMapping(value = "user/public/registration/")
    public ResponseEntity<?> registration(HttpServletRequest request){
        UserHandler handler =new UserHandler(request,null);
        return handler.elaborate(request);
    }

    /**
     * Cattura la richiesta del client riguardo l'autenticazione;la chiamata viene passata al sottostante handler
     * @param request la richiesta del client
     * @return la risposta ritornata dall'handler sottostante {@link UserHandler#auth() auth}
     *
     * @see UserHandler
     */
    @PostMapping(value = "user/public/login/")
    public ResponseEntity<?> auth(HttpServletRequest request){
        UserHandler handler =new UserHandler(request,null);
        return handler.elaborate(request);
    }


}
