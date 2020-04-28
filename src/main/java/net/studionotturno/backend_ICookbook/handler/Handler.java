package net.studionotturno.backend_ICookbook.handler;

import net.studionotturno.backend_ICookbook.security.JwtUserChecker;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * Classe handler del desing patter gof chain of responsability
 * Mette a disposizione un metodo generale per il controllo del token JWT in input dal client
 *
 * La richiesta viene passata prima ad uno specifico controller che istanzia il proprio oggetto per elaborare la richiesta in modo effettivo,<br/>
 * poi a runtime viene effettuata una ricerca dinamica per eseguire {@link Handler#eleborate(HttpServletRequest) elaborate}
 * il quale non deve essere mai sovrascritto, esso funge da filtro per il token JWT.<br/>
 * Viene filtrata la richiesta e passata ad elab() che in questo caso è sovrascritto in ogni classe che implementa
 * l'interfaccia Handler che gestisce le richiestra dei controller
 *
 * @see JwtUserChecker
 * */
public interface Handler {

    /**
     * Viene controllato il token sull'header della chiamata
     * @return un valore booleano che ritorna true se il token è presente e se è valido,altrimenti false
     */
    default boolean checkToken(String header){
        String token=header;
        if(token!=null){
            //se il token non è vuoto vine controllato, se i dati sono adeguati allora viene inoltrato al controller
            JwtUserChecker checker=new JwtUserChecker(token);
            if(checker.isExpired())return true;
            else return false;
        }else return false;
    }


    /**
     * Viene controllato se la richiesta è indirizzata verso quegli endpoit per l'autenticazione;<br/>
     * se si, la chiamata passa tranquillamente verso l'userHandler, altrimenti<br/>
     * viene controllato se esiste il token e se è valido.<br/>
     * il reindirizzamento viene eseguito tramite il metodo elab() diverso per ogni endpoint
     * (si deve effettuare un override del metodo nella classe che eredita handler)
     * @return la risposta da inviare al client
     */
    default ResponseEntity<?>  elaborate(HttpServletRequest request){
        if(request.getRequestURI().contains("user/public/")) return elab();
        else{
            if(checkToken(request.getHeader("header")))return elab();
            else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     *Metodo vuoto da dover sovrascrivere nelle classi che implementazno Handler
     * */
    default ResponseEntity<?> elab() {
        //TODO viene sovrascritto per ogni concrete handler
        System.out.println("elab Handler");
        return null;
    }

    /**
     * Viene letta la request in input dalla richiesta ed estrapolato il body;
     * Il body per la richiesta è un json e quindi deve essere parsato divenendo un oggetto {@code Document}
     * @see Document
     * */
    default Document  getBody(HttpServletRequest request){
        final StringBuilder body = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line;
            if (reader == null) return null;
            while ((line = reader.readLine()) != null) body.append(line);
            return body.toString().isEmpty()?null:Document.parse(body.toString());
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Viene parsato il token ed estrapolata l'email
     * @return L'email dell'utente estrapolata dal token
     */
    default String getEmail(String token){
        JwtUserChecker c=new JwtUserChecker(token);
        return c.getUserEmail();
    }



}
