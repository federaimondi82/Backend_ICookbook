package net.studionotturno.backend_ICookbook.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.*;

/**
 * Vengono estrapolati i claims dal token; è possibile effettuare dei controlli sul token jwt
 *
 * @see JwtTokenUtil
 * @see Jwts
 * @see Claims
 */
public class JwtUserChecker {

    private String jwt;

    private String secret= "68!8wd&i?sCjsuatjsc5oalelo2avisck5ak3eoakc";//valore letto nell properties
    private Claims payload;

    public JwtUserChecker(String jwt){
        this.jwt=jwt;
        this.payload=Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
    }


    /**
     * Viene controllato se il token non è scaduto
     * @return true se il token è valido, false altrimenti
     */
    public boolean isExpired(){
        return this.payload.getExpiration().after(new Date());
    }

    /**
     * Ritorna l'email dell'utente presente nel payload
     * */
    public String getUserEmail(){
        return this.payload.getSubject();
    }

}
