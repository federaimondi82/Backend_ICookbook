package net.studionotturno.backend_ICookbook.domain;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Una risorsa leggera da presentare al client dopo le ricerche sul database
 */
public class LazyResource {

    String documentID,recipeName;
    double minutes;

    public LazyResource(){}

    public LazyResource getResource() {
        return this;
    }

    /**Trasformare un elemento Lazy Resoirce in un DOcuemnt per il database o per in viarlo al client
     * Una serializzazione dei dati della ricetta in un formato leggero
     * @return
     */
    public Document toJson(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("documentID",this.documentID);
        map.put("recipeName",this.recipeName);
        map.put("executionTime",this.minutes);
        return new Document(map);
    }

    /**
     * Trasformazione in oggetto di un json provenienente dal client o dal database
     * Deserializzazione del dato
     * */
    public LazyResource toObject(Map<String, ?> data) {
        try{this.documentID=data.get("documentID").toString();}catch(Exception e){}
        try{this.recipeName=data.get("recipeName").toString();}catch(Exception e){}
        try{this.minutes=Double.parseDouble(data.get("executionTime").toString());}catch(Exception e){}
        return this;
    }

    //#region setter

    public LazyResource setDocumentID(String documentID){this.documentID=documentID; return this;}
    public LazyResource setRecipeName(String recipeName){this.recipeName=recipeName; return this;}
    public LazyResource setExecutionTime(double executionTime){this.minutes=executionTime; return this;}

    //#endregion setter

    //#region getter

    public String getDocumentID(){return this.documentID;}
    public String getRecipeName(){return this.recipeName;}
    public double getExecutionTime(){return this.minutes;}

//#endregion getter

    @Override
    public String toString() {
        return "\"documentID\":" + documentID + ",\"recipeName\":" + recipeName + ",\"minutes\":" + minutes;
    }
}
