package net.studionotturno.backend_ICookbook.DbConnection;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


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
	
	/**Permette di reperire i dati dal database tramite una query di "select".
	 * Le query in input sono consentite solo se passano il controllo del metodo checkQuery
	 * @param query: una query di select
	 * @return ritorna il resultSet della query;
	 */
	public FindIterable<Document> getDocumentQuery(Bson query) {
		FindIterable<Document> c = null;
		if(checkBson(query)){
			c=this.collection.find(query);
		}
		return c;
	}

	/**
	 * Non sono consentite tutte quelle query che hanno un profilo paricolarmente pericoloso e che
	 * lo sviluppatore non ha implementato.
	 *
	 * */
	private boolean checkBson(Object query) {
		Document d=null;
		if(query instanceof Bson){
			Bson query2=(Bson)query;
			BsonDocument doc=query2.toBsonDocument(
					BsonDocument.class, MongoClient.getDefaultCodecRegistry()
			);
			String json=doc.toJson();
			d=Document.parse(json);
		}
		if(query instanceof Document){
			d=(Document)query;
		}
		if(d.toString().contains("$ne=") || d.toString().contains("null") ||
				d.toString().contains("$nin") || d.toString().contains("$in") ||
				d.toString().contains("$nor") || d.toString().contains("$lte") || d.toString().contains("$lt") ||
				(d.toString().contains("$gt=") && d.toString().contains("password")) ||
				(d.toString().contains("$gte=") && d.toString().contains("password")) ||
				(d.toString().contains("$gt=") && d.toString().contains("userName")) ||
				(d.toString().contains("$gte=") && d.toString().contains("userName"))
		){
			return false;
		}
		else return true;
	}


	/**
	 * Permette di eseguire query di inserimento e update sui dati
	 * @param document: un documento json proveninte dal client mobile
	 * @return ritorna true se la query e' andata a buon fine
	 */
	public boolean insertData(Document document) {
		if(document==null) return false;
		if(checkBson(document)){
			try {
				String s=new ObjectId().toString();
				document.put("_id",s);
				collection.insertOne(document);
				return true;
			}catch(Exception e){
				return false;
			}
		}
		return false;
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
