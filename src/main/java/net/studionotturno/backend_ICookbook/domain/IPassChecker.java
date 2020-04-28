package net.studionotturno.backend_ICookbook.domain;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Interfaccia per la gestione e controllo delle password
 *
 * */
public interface IPassChecker {

    String SECRET= "68!8wd&i?sC";

    /**
     * Viene controllato se i dati in input sono giusti
     * @param email l'email dell'utente
     * @param pass la password dell'utente
     * @return ritorna true se i dati coincidono con quelli del database,false altrimenti
     */
    default boolean checkPass(String email,String pass){
        Bson bson=and(eq("email",email),eq("password",getPass(email,pass)));
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("users").getDocumentQuery(bson);
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
    default String getPass(String email,String pass) {
        //cerca il salt relativo alla email e cifra la pass
        Bson bson=eq("email",email);
        FindIterable<Document> f= MongoDBConnection.builder().setCollection("users").getDocumentQuery(bson);
        String salt="";
        Set<Document> list = f.into(new HashSet<>());
        for (Document doc:list) salt=doc.get("salt").toString();
        String p=pass+""+SECRET+""+salt;
        return cypherPass(p);
    }

    default String cypherPass(String password){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(password.getBytes());
        return Base64.getEncoder().encodeToString(md.digest());
    }

    default String getSecret(){return SECRET;}
}
