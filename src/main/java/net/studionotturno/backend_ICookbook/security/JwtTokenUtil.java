package net.studionotturno.backend_ICookbook.security;

import java.util.*;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe responsabile della creazione dei token.<br/>
 * Un Oggetto JWT è formato da tre parti;
 * <ul>
 *<li>un Header ( racchiude informazioni relative a tipo di token e all'algoritmo )</li>
 *<li>un payload ( personalizzabile che contiene il corpo e le informazioni che si vogliono passare usando il jwt)</li>
 *<li>e una signature ( una firma formata da una chiave e un algoritmo di cifratura per cifrae i dati del token)</li>
 *<ul/>
 *
 * tag utili sul sito ufficiale https://tools.ietf.org/html/rfc7519#section-4.1
 */
public class JwtTokenUtil {

    /**
     * Gli elementi di un Json sono elementi di tipo json, queindi del tipo chiava,valore<br/>
     * Tradotto in Java un Map<K,V>
     *
     * */
    private Map<String,Object> header,payload,signature;
    private long now;
    private String userEmail;
    private String SECRET= "68!8wd&i?sCjsuatjsc5oalelo2avisck5ak3eoakc";//valore letto nell properties

    public JwtTokenUtil(String userEmail){
        this.header=new HashMap<>();
        this.payload=new HashMap<>();
        this.signature=new HashMap<>();
        this.now= Calendar.getInstance().getTimeInMillis()/1000;
        this.userEmail=userEmail;
    }

    /**
     * Costruisce la parte dell'header per il
     * @return la struttura dati da usare come oggetto Json per l'Header
     */
    private Map<String, Object> getHeader() {
        this.header.put("alg","HS256");
        this.header.put("typ","JWT");
        return this.header;
    }

    /**
     * Costruisce la parte del payload per il
     * @return la struttura dati da usare come oggetto Json per il Payload
     */
    private Map<String, Object> getPayload() {
        this.payload.put("jti", UUID.randomUUID().toString());//id univoco per prevenire la generazione di token accidentale
        this.payload.put("iss","net.studionotturno.ICookbook");
        this.payload.put("sub",this.userEmail);
        this.payload.put("iot",this.now);//Epochtime della creazione del token in secondi
        this.payload.put("exp",this.now+ TimeUnit.DAYS.toSeconds(356));//Scadenza del  token un anno
        return this.payload;
    }

    /**
     * Generazione di un JWT per l'autenticazione
     * @return
     */
    public String getJwt(){
        return Jwts.builder().setHeader(getHeader())
                .setClaims(getPayload())
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

}
