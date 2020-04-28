package net.studionotturno.backend_ICookbook.domain;

/**
 * Classe ocn responsabilità di controllare gli oggetti json in input da un client
 *
 * */
public class StringChecker {

    private static StringChecker instance;

    StringChecker(){}

    public static StringChecker getInstance(){
        if(instance==null) instance=new StringChecker();
        return instance;
    }

    public boolean checkString(String input){
        if(input==null || input.contains(">") || input.contains("<") || input.contains("&") || input.contains("\"") ||
        input.contains("$") || input.contains("\\") || input.contains("/") || input.contains("'") ||
         input.contains("%") || input.contains("*") || input.contains("+") ||
        input.contains("-") || input.contains(",") || input.contains(";") || input.contains("=") ||
        input.contains("^") || input.contains("|") || input.contains("[") || input.contains("]") ||
        input.contains("{") || input.contains("}") || input.contains("(") || input.contains(")") ||
        input.contains("#") || input.contains("§") || input.contains("°") || input.contains("ç") ){
            return false;
        }
        else return true;
    }

    public boolean checkEmail(String email){
        if(checkString(email)){
            if(email.contains("@")) return true;
            else return false;
        }
        else return false;
    }
}
