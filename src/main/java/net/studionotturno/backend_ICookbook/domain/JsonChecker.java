package net.studionotturno.backend_ICookbook.domain;

import com.mongodb.MongoClient;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Classe ocn responsabilità di controllare gli oggetti json in input da un client
 *
 * */
public class JsonChecker {

    private static JsonChecker instance;

    JsonChecker(){}

    public static JsonChecker getInstance(){
        if(instance==null) instance=new JsonChecker();
        return instance;
    }

    /**
     * Controlla che il json non abbia del codice malevolo con query NoSQL non permesse
     * */
    public boolean checkJson(Document d){
        if(d.isEmpty()) return false;
        if(d.toString().contains("$ne=") || d.toString().contains("null") ||
                d.toString().contains("$nin") || d.toString().contains("$in") ||
                d.toString().contains("$nor") ||
                (d.toString().contains("$gt=") && d.toString().contains("password")) ||
                (d.toString().contains("$gte=") && d.toString().contains("password")) ||
                (d.toString().contains("$gt=") && d.toString().contains("userName")) ||
                (d.toString().contains("$gte=") && d.toString().contains("userName")) ||
                (d.toString().contains("$gte=") && d.toString().contains("name")) ||
                (d.toString().contains("$gt=") && d.toString().contains("name"))
        ){
            return false;
        }
        else{
            return true;
        }
    }
}
