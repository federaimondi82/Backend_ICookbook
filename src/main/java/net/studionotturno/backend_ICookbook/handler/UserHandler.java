package net.studionotturno.backend_ICookbook.handler;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.*;
import net.studionotturno.backend_ICookbook.security.JwtTokenUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Fa parte del desing patter Chain of responsability per le chiamate dal client;<br/>
 * questo concrateHandler realizza le elaborazioni da eseguire in base alle richieste
 * riguardanti l'user quali: registrazione, autenticazione e invio propri dati
 *
 * @see Handler
 * @see net.studionotturno.backend_ICookbook.controllers.UserController
 * */
public class UserHandler implements Handler,IPassChecker {

    private HttpServletRequest request;
    private Document body;

    public UserHandler(HttpServletRequest request, Handler next) {
        this.request=request;
        this.body=getBody(request);
    }

    @Override
    public ResponseEntity<?> elab(){
        String opt=this.request.getRequestURI();
        if(opt.contains("user/public/login/")) return auth();
        else if(opt.contains("user/public/registration/")) return registration();
        else if(opt.contains("user/getData/")) return getData();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Consente di esaudire una richiesta di autenticazione.<br/>
     * Se l'utente viene riconosciuto come registrato allora viene creato un token JWT univoco con scadenza un anno
     * che verrà utilizzato per ogni successiva richiesta del client.<br>
     * Il token, lato client, verrà salvato in locale per essere richiamato ed usato ad ogni chiamata verso il backend;
     * se il token non fosse presente nessuna chiamata arriverà al backend.
     * @return Ritorna un token JWT
     */
    public ResponseEntity<?> auth(){
        Map<String,?> map= (Map<String, ?>) this.body.get("data");
        User user=new User().toObject(map);
        if(!StringChecker.getInstance().checkEmail(user.getEmail()) || !StringChecker.getInstance().checkString(user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(checkPass(user.getEmail(),user.getPassword())){
            return ResponseEntity.ok()
                    .header("token",new JwtTokenUtil(user.getEmail()).getJwt())
                    .body("Hello token");
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Vengono effettuti dei controlli sui dati; se passano i dati l'utente viene registrato.<br/>
     * Le chiamate a questo endoit non necessitano del controllo del json web token, a differenza di tutte le altre
     * @return ritorna una badRequest se qualcosa è andato storto,
     * altimimenti un valore booleano:true se la registrazione è andata a buon fine,altimenti false
     */
    public ResponseEntity<?> registration(){
        if(this.body==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Map<String,?> map= (Map<String, ?>) this.body.get("data");
        User user=new User().toObject(map);
        if(!StringChecker.getInstance().checkEmail(user.getEmail()) || !StringChecker.getInstance().checkString(user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(checkEmail(user.getEmail())){
            String s= SaltGenerator.getInstance().getSalt();
            user.setSalt(s);
            String p=user.getPassword()+""+getSecret()+""+s;
            String pass=cypherPass(p);   user.setPassword(pass);
            return ResponseEntity.ok(MongoDBConnection.builder().setCollection("users").insertData(user.toJson()));
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Consente di reperire i dati di un utente
     */
    public ResponseEntity<?> getData(){
        if(this.body==null) return null;
        Map<String,?> map= (Map<String, ?>) this.body.get("data");
        User user=new User().toObject(map);
        Bson bson=and(eq("email",user.getEmail()));
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("users").getDocumentQuery(bson);
        try{
            List<Document> list = f.into(new ArrayList<>());
            String oldPass=user.getPassword();
            user=user.toObject(list.get(0));
            user.setSalt("");   user.setPassword(oldPass);
            return ResponseEntity.ok(user.toJson());
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Viene controllato se sul database vi è una email identica.
     * @param email
     * @return Ritorna true se l'email NON è presente (quindi l'utente deve essere registrato nel DB), altrimenti false
     */
    private boolean checkEmail(String email){
        Bson bson=eq("email",email);
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("users").getDocumentQuery(bson);
        try{
            //se non da errore vuol dire che c'è una risposta
            f.first().toString();
            return false;
        }catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
            return true;
        }
    }

}
