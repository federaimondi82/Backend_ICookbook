package net.studionotturno.backend_ICookbook;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import org.bson.BsonDocument;
import org.bson.Document;

import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@SpringBootTest
public class NOSQL_Injection_Test {

    @Test
    public final void test_query_ne(){

        /*//Bson query=and(ne("password",null),gt("password",null));
        //Bson query=gt("\"ne(\"password\",null)\"","");
        //Bson query=eq("\"ne(\"password\",null)\"",null);
        //Bson query=eq("",null);
        Bson query=gt("password","");
        //System.out.println(query.toString().split("operation",20)[0].contains("ne"));//return true
        //BsonDocument doc=query.toBsonDocument(BsonDocument.class)
        FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(query);
        List<Document> list = f.into(new ArrayList<>());
        list.forEach((el)-> System.out.println(el.toString()));*/

        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(or(ne("password",null),gt("password",null))),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(nin("","")),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(nin("")),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(nin("","aaa","bbb","ccc")),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(ne("password","1")),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(gt("password","")),null);
        assertEquals(MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(gte("name","")),null);
        /**/

    }

    @Test
    public final void test_request_param(){

        FindIterable<Document>  f= MongoDBConnection.getInstance().setCollection("users").getDocumentQuery(nin("pass","aaa","bbb","ccc"));
        List<Document> list = f.into(new ArrayList<>());
        list.forEach((el)-> System.out.println(el.toString()));
    }

    @Test
    public final void test_prevent_insert(){

        Bson query=gt("password","");
        BsonDocument bdoc=query.toBsonDocument(BsonDocument.class, CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider()));
        String json=bdoc.toJson();
        Document doc=Document.parse(json);
        MongoDBConnection.getInstance().setCollection("users").insertData(doc);

    }

}
