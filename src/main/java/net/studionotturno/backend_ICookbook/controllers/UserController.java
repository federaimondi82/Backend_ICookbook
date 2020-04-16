package net.studionotturno.backend_ICookbook.controllers;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.SaltGenerator;
import net.studionotturno.backend_ICookbook.domain.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

@RestController
public class UserController {

    String secret="68!8wd&i?sC";

    @PostMapping(value="user/getData/")
    public Document getData(@RequestBody String json){
        if(json==null) return null;
        Document userDoc=Document.parse(json);
        Map<String,?> map= (Map<String, ?>) userDoc.get("data");
        User user=new User().toObject(map);
        if(user.getEmail()==null | !user.getEmail().contains("@") | user.getPassword()==null | user.getEmail()=="" | user.getPassword()=="") return null;
        else{
            //System.out.println("user1: "+user.toString());
            Bson bson=and(eq("email",user.getEmail()),eq("password",getPass(user.getEmail(),user.getPassword())));
            FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(bson);
            try{
                //se non da errore vuol dire che c'è una risposta
                List<Document> list = f.into(new ArrayList<>());
                user=user.toObject(list.get(0));
                user.setSalt("");
                user.setPassword("");
                return user.toJson();
            }catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
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
        if(user.getEmail()==null | !user.getEmail().contains("@") | user.getPassword()==null | user.getEmail()=="" | user.getPassword()=="") return false;

        if(checkEmail(user.getEmail())){
            String s=SaltGenerator.getInstance().getSalt();
            user.setSalt(s);
            String p=user.getPassword()+""+secret+""+s;
            String pass=cypherPass(p);
            user.setPassword(pass);
            return MongoDBConnection.getInstance().setCollection("users").insertData(user.toJson());
        }
        else   return false;
    }

    @PostMapping(value = "user/login/")
    public boolean userLogin(@RequestBody String json){
        if(json==null) return false;
        Document userDoc=Document.parse(json);
        Map<String,?> map= (Map<String, ?>) userDoc.get("data");
        User user=new User().toObject(map);
        if(user.getEmail()==null | !user.getEmail().contains("@") | user.getPassword()==null | user.getEmail()=="" | user.getPassword()=="") return false;
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


    /**
     * @param email l'email dell'utente
     * @param pass la password dell'utente
     * @return ritorna true se i dati coincidono con quelli del database,false altrimenti
     */
    private boolean checkPass(String email,String pass){
        Bson bson=and(eq("email",email),eq("password",getPass(email,pass)));
        FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(bson);
        try{
            //se non da errore vuol dire che c'è una risposta
            f.first().toString();
            return true;
        }catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
            return false;
        }
    }

    /**
     * @param email l'email dell'utente
     * @param pass la password inviata dal client
     * @return la password cifrata da confrontare
     */
    private String getPass(String email,String pass) {
        //cerca il salt relativo alla email e cifra la pass
        Bson bson=eq("email",email);
        FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(bson);
        String salt="";
        Set<Document> list = f.into(new HashSet<>());
        for (Document doc:list) {
            salt=doc.get("salt").toString();
        }
        String p=pass+""+secret+""+salt;
        return cypherPass(p);

    }

    private String cypherPass(String password){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(password.getBytes());
        return Base64.getEncoder().encodeToString(md.digest());
    }

}
