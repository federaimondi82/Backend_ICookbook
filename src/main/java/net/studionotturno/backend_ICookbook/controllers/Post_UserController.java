package net.studionotturno.backend_ICookbook.controllers;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

/**
 * Classe con responsabilità di gestire le chiamate post dal client per quanto riguarda gli utenti
 *
 * */
@RestController
public class Post_UserController implements IPassChecker {

    /**
     * Consente di reperire i dati di un utente
     * @param json un oggetto json proveniente dal client
     * @return un oggetto contentente tutti i dati di un utente
     */
    @PostMapping(value="user/getData/")
    public Document getData(@RequestBody String json){
        if(json==null) return null;
        Document userDoc=Document.parse(json);
        if(!JsonChecker.getInstance().checkJson(userDoc))return null;
        Map<String,?> map= (Map<String, ?>) userDoc.get("data");
        User user=new User().toObject(map);
        if(!StringChecker.getInstance().checkEmail(user.getEmail()) || !StringChecker.getInstance().checkString(user.getPassword())) return null;
        else{
            Bson bson=and(eq("email",user.getEmail()),eq("password",getPass(user.getEmail(),user.getPassword())));
            FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(bson);
            try{
                List<Document> list = f.into(new ArrayList<>());
                String oldPass=user.getPassword();
                user=user.toObject(list.get(0));
                user.setSalt("");   user.setPassword(oldPass);
                return user.toJson();
            }catch(Exception e){
                return null;
            }
        }
    }

    /**
     * Vengono effettuti dei controlli sui dati; se passano i dati l'utente viene registrato.
     * @param json l'utente serializzato proveniente dal client
     * @return ritorna true se la registrazione è andata a buon fine,altimenti false
     */
    @PostMapping(value = "user/registration/")
    public boolean userRegisty(@RequestBody String json){
        if(json==null) return false;
        Document userDoc=Document.parse(json);
        Map<String,?> map= (Map<String, ?>) userDoc.get("data");
        User user=new User().toObject(map);
        if(!StringChecker.getInstance().checkEmail(user.getEmail()) || !StringChecker.getInstance().checkString(user.getPassword())) return false;
        if(checkEmail(user.getEmail())){
            String s=SaltGenerator.getInstance().getSalt();
            user.setSalt(s);
            String p=user.getPassword()+""+getSecret()+""+s;
            String pass=cypherPass(p);   user.setPassword(pass);
            return MongoDBConnection.getInstance().setCollection("users").insertData(user.toJson());
        }
        else   return false;
    }

    /**
     * Metodo controller per i dati per l'autenticazione
     * @param json un elemento json con i dati per l'autenticazione proveniente dal client
     * @return un valore boolenao che identifica se l'autenticazione è andata a buon fine o no
     */
    @PostMapping(value = "user/login/")
    public boolean userLogin(@RequestBody String json){
        Document userDoc=Document.parse(json);
        Map<String,?> map= (Map<String, ?>) userDoc.get("data");
        User user=new User().toObject(map);
        if(!StringChecker.getInstance().checkEmail(user.getEmail()) || !StringChecker.getInstance().checkString(user.getPassword())) return false;
        return checkPass(user.getEmail(),user.getPassword());
    }

    /**
     * Viene controllato se sul database vi è una email identica.
     * @param email
     * @return Ritorna true se l'email NON è presente (quindi l'utente deve essere registrato nel DB), altrimenti false
     */
    private boolean checkEmail(String email){
        Bson bson=eq("email",email);
        FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(bson);
        try{
            //se non da errore vuol dire che c'è una risposta
            f.first().toString();
            return false;
        }catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
            return true;
        }
    }
}
