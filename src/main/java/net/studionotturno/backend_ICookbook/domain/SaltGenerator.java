package net.studionotturno.backend_ICookbook.domain;

import java.util.Random;

public class SaltGenerator {
    private String salt;
    private static SaltGenerator instance;

    public static SaltGenerator getInstance(){
        if(instance==null) instance=new SaltGenerator(10);
        return instance;
    }

    private SaltGenerator(int lenght){
        Random random =new Random();
        StringBuffer buf=new StringBuffer();
        for(int i=0;i<lenght;i++){
            int n=random.nextInt(122 - 48)+48;
            if((n >= 58 && n <= 64) || (n >= 91 && n <= 96)){
                i--;
                continue;
            }
            buf.append((char)n);
        }
        this.salt=buf.toString();
    }

    public String getSalt(){
        return this.salt;
    }
}
