package net.studionotturno.backend_ICookbook.controllers;

import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.IPassChecker;
import net.studionotturno.backend_ICookbook.domain.StringChecker;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * COntroller dedicato alle chiamate delete riguardo alle ricette dell'utente
 * */
@RestController
public class Delete_RecipeController  implements IPassChecker {

    /**
     * Consente di cancellare una ricetta dal database, la ricetta deve essere di uno specifico utente
     * @param email email dell'utente
     * @param recipeName una ricetta
     * @return un valore booleano che indica se l'operazione è andata a buon fine o no
     */
    @DeleteMapping(value="docu/delete_documents/{email}/{password}/{recipeName}")
    public boolean deleteSingleDocument(@PathVariable String email,@PathVariable String password,@PathVariable String recipeName) {
        if(!StringChecker.getInstance().checkString(password) || !StringChecker.getInstance().checkEmail(email) || !StringChecker.getInstance().checkString(recipeName)) return false;
        if(checkPass(email,password)){
           // System.out.println(1);
            return MongoDBConnection.getInstance().setCollection("recipes").deleteDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
        }
        else return false;

    }

}
