package net.studionotturno.backend_ICookbook.domain;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresentazione dei dati dell'utente per il backend
 *
 * */
public class User {


    String name, surname,gender, email,password,salt,birthday;

    public User setName(String name){ this.name=name;return this; }
    public User setSurname(String surname){ this.surname=surname;return this; }

    public User setGender(String gender){ this.gender=gender;return this; }
    public User setEmail(String email){ this.email=email;return this; }
    public User setPassword(String pass){this.password=pass; return this; }
    public User setBirthday(String birthday){this.birthday=birthday;return this; }
    public User setSalt(String salt) { this.salt = salt; return this;}

    public String getName(){
        return this.name;
    }
    public String getSurname(){
        return this.surname;
    }
    public String getSalt() { return salt; }
    public String getGender(){
        return this.gender;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public String getBirthday(){
        return this.birthday;
    }

    /**
     * Serializzaizone di dati in formato json da object User
     * */
    public Document toJson(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name",this.name);
        map.put("surname",this.surname);
        map.put("gender",this.gender);
        map.put("email",this.email);
        map.put("password",this.password);
        map.put("birthday",this.birthday);
        map.put("salt",this.salt);
        return new Document(map);
    }

    /**
     * Deserializzaizone dei dati da Json a oggetto User
     * */
    public User toObject(Map<String, ?> data) {
        try{this.name=data.get("name").toString();}catch(Exception e){}
        try{this.surname=data.get("surname").toString();}catch(Exception e){}
        try{this.gender=data.get("gender").toString();}catch(Exception e){}
        try{this.email=data.get("email").toString();}catch(Exception e){}
        try{this.password=data.get("password").toString();}catch(Exception e){}
        try{this.birthday=data.get("birthday").toString();}catch(Exception e){}
        try{this.salt=data.get("salt").toString();}catch(Exception e){}
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
