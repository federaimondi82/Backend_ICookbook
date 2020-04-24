package net.studionotturno.backend_ICookbook.controllers;

import java.util.*;

import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.JsonChecker;
import org.bson.Document;

import com.mongodb.client.FindIterable;

import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Controller per le chiamate di get post e delete relative alle ricette e ai document
 */
@RestController
public class Post_RecipesController {

	/**
	 * Consente di ricevere un messaggio post dall'applicazione mobile e salvare una ricetta sul database
	 */
	@PostMapping(value ="docu/post_documents/")
	public boolean postDocument(@RequestBody String document) {
		if(document==null) return false;
		Document d=Document.parse(document);
		if(!JsonChecker.getInstance().checkJson(d)) return false;
		Bson bson=and(eq("userName",d.get("userName")),eq("recipeName",d.get("recipeName")));
		FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("recipes").getDocumentQuery(bson);
		try{
			//se non da errore vuol dire che una ricetta con lo stesso nome è già presente,e va quindi modificata in tronco
			f.first().toString();
			MongoDBConnection.getInstance().setCollection("recipes").deleteDocumentQuery(bson);
			return MongoDBConnection.getInstance().setCollection("recipes").insertData(d);
		}catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
			return MongoDBConnection.getInstance().setCollection("recipes").insertData(d);
		}
	}

}
