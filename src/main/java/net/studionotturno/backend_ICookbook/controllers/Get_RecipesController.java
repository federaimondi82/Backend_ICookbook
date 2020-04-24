package net.studionotturno.backend_ICookbook.controllers;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.IPassChecker;
import net.studionotturno.backend_ICookbook.domain.StringChecker;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Classe responsabile di gestire le chiamate get per le ricette dal client
 */
@RestController
public class Get_RecipesController implements IPassChecker {


    /**
     * Ritorna tutti i documenti dei un utente sotto forma di una lista di documenti json
     * @param email : l'email dell'utente
     * @return un insieme di documenti json
     */
    @GetMapping(value="docu/get_documents/{email}/{password}")
    public List<Document> getDocuments(@PathVariable String email,@PathVariable String password) {
        if(!StringChecker.getInstance().checkEmail(email) || !StringChecker.getInstance().checkString(password)) return null;
        if(checkPass(email,password)){
            FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("recipes").getDocumentQuery(eq("userName",email));
            List<Document> list = f.into(new ArrayList<>());
            //list.forEach((el)->System.out.println(el.toString()));
            return list;
        }else{
            return null;
        }

    }

    /**
     * Ritorna un determinato documento dell'utente indicato
     * @param email : l'email dell'utente
     * @return un insieme di documenti json
     */
    @GetMapping(value="docu/get_documents/{email}/{password}/{recipeName}")
    public Map<String,?> getSingleDocument(@PathVariable String email, @PathVariable String password,@PathVariable String recipeName) {
        if(!StringChecker.getInstance().checkEmail(email) || !StringChecker.getInstance().checkString(password) || !StringChecker.getInstance().checkString(recipeName)) return null;
        if(checkPass(email,password)){
            FindIterable<Document>  f= MongoDBConnection.getInstance().setCollection("recipes").getDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
            return f.first();
        }
        else return null;

    }

    /**
     * Consente di conoscere il documenID del documento memorizzato sul DB<br>
     * di una specifica ricetta passata come parametro
     * @param email l'email dell'utente
     * @param recipeName il nome della ricetta
     * @return una stringa contenente il documenID del documento memorizzato sul DB di una specifica ricetta passata come parametro
     */
    @GetMapping(value="docu/get_documents/recipes/{email}/{password}/{recipeName}")
    public String getDocumentID(@PathVariable String email,@PathVariable String password,@PathVariable String recipeName){
        if(!StringChecker.getInstance().checkEmail(email) && ! StringChecker.getInstance().checkString(password)) return null;
        if(checkPass(email,password)){
            FindIterable<Document>  f= MongoDBConnection.getInstance().setCollection("recipes").getDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
            return f.first().get("_id").toString();
        }else return null;

    }

}
