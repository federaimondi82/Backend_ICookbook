package net.studionotturno.backend_ICookbook.domain;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Fornisce una interfaccia unificata per inviare e ricevere informazioni al/dal database
 *
 */
public class MongoDBConnection {
	
	private static MongoDBConnection instance;
	private String user;
	private String pass;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	
	public MongoDBConnection() {
		this.user="root";
		this.pass="";
		this.mongoClient = new MongoClient("localhost", 27017);
		this.database = mongoClient.getDatabase("mydb");
	}
	
	public static MongoDBConnection getInstance(){
		if(instance==null) instance=new MongoDBConnection();
		return instance;
	}

	public MongoDBConnection setCollection(String collection){
		this.collection=this.database.getCollection(collection);
		return this;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void close() {
		//TODO
	}
	
	/**Permette di reperire i dati dal database tramite una query di "select"
	 * @param query: una query di select
	 * @return ritorna il resultSet della query;
	 */
	public FindIterable<Document> getDocumentQuery(Bson query) {
		FindIterable<Document> c=this.collection.find(query);
		return c;
	}
	

	/**
	 * Permette di eseguire query di inserimento e update sui dati
	 * @param document: un documento json proveninte dal client mobile
	 * @return ritorna true se la query e' andata a buon fine
	 */
	public boolean insertData(Document document) {
		try {
			String s=new ObjectId().toString();
 			document.put("_id",s);
			collection.insertOne(document);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}


	/**
	 * Cancellazione di dati sul database
	 * @param query una query di cancellazione
	 * @return un boolenano per deternimare se la query è andta a buon fine
	 */
	public boolean deleteDocumentQuery(Bson query) {
		try {
			collection.deleteOne(query);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public void updateData(String document) {

	}

}
