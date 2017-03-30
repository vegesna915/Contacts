package com.example.varma.contacts;

import java.util.regex.Pattern;

/**
 * Created by Varma on 3/28/2017.
 */

public class Utilis {


    static boolean isEmailValid(String email){

        return (email.contains("@") && email.contains("."));

    }
    static  boolean isPasswordValid(String pass){
        return ((pass.length() > 5 )&&(pass.length() < 20) );
    }

    static String getFirstLetter(String name){
        String c = Character.toString(name.charAt(0));
        if(Pattern.matches("[a-zA-Z]",c)){
            return c;
        }else{
            return " ";
        }

    }

}
