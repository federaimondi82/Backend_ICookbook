package net.studionotturno.backend_ICookbook.controllers;

import java.util.*;

import net.studionotturno.backend_ICookbook.domain.MongoDBConnection;
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
public class DocumentController {

	/**
	 * Ritorna tutti i documenti dei un utente sotto forma di una lista di documenti json
	 * @param email : l'email dell'utente
	 * @return un insieme di documenti json
	 */
	@GetMapping(value="docu/get_documents/{email}")
	public List<Document> getDocuments(@PathVariable String email) {
		FindIterable<Document>  f= MongoDBConnection.getInstance().getDocumentQuery(eq("userName",email));
		List<Document> list = f.into(new ArrayList<>());
		return list;
	}

	/**
	 * Ritorna un determinato documento dell'utente indicato
	 * @param email : l'email dell'utente
	 * @return un insieme di documenti json
	 */
	@GetMapping(value="docu/get_documents/{email}/{recipeName}")
	public Map<String,?> getSingleDocument(@PathVariable String email, @PathVariable String recipeName) {
		FindIterable<Document>  f= MongoDBConnection.getInstance().getDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
		return f.first();
	}

	@GetMapping(value="docu/get_documents/recipes/{email}/{recipeName}")
	public String getDocumentID(@PathVariable String email,@PathVariable String recipeName){
		FindIterable<Document>  f= MongoDBConnection.getInstance().getDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
		return f.first().get("_id").toString();
	}

	@DeleteMapping(value="docu/delete_documents/{email}/{recipeName}")
	public boolean deleteSingleDocument(@PathVariable String email, @PathVariable String recipeName) {
		return MongoDBConnection.getInstance().deleteDocumentQuery(and(eq("userName",email),eq("recipeName",recipeName)));
	}

	/**
	 * Consente di ricevere un messaggio post dall'applicazione mobile e salvare una ricetta sul database
	 */
	@PostMapping(value ="docu/post_documents/")
	public boolean postDocument(@RequestBody String document) {
		Document d=Document.parse(document);
		Bson bson=and(eq("userName",d.get("userName")),eq("recipeName",d.get("recipeName")));
		FindIterable<Document> f= MongoDBConnection.getInstance().getDocumentQuery(bson);
		try{
			//se non da errore vuol dire che una ricetta con lo stesso nome è già presente,e va quindi modificata in tronco
			f.first().toString();
			MongoDBConnection.getInstance().deleteDocumentQuery(bson);
			return MongoDBConnection.getInstance().insertData(d);
		}catch(Exception e){//se scatta l'eccezione,quindi la query non ha dato risultati, viene inserito il nuovo documento
			return MongoDBConnection.getInstance().insertData(d);
		}
	}

}
