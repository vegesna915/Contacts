package com.example.varma.contacts;

import android.content.Context;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Varma on 3/28/2017.
 */

public class Utilis {


    static boolean isEmailValid(String email) {

        return (email.contains("@") && email.contains("."));

    }

    static boolean isPasswordValid(String pass) {
        return ((pass.length() > 5) && (pass.length() < 20));
    }

    static String getFirstLetter(String name) {
        String c = Character.toString(name.charAt(0));
        if (Pattern.matches("[a-zA-Z]", c)) {
            return c;
        } else {
            return " ";
        }

    }

    static int getContactColor(Context context, int oldColor) {

        int color;

        Random random = new Random();
        int i = random.nextInt(9);

        switch (i) {
            case 0: {
                color = context.getResources().getColor(R.color.yellowA400);
                break;
            }
            case 1: {
                color = context.getResources().getColor(R.color.redA400);
                break;
            }
            case 2: {
                color = context.getResources().getColor(R.color.tealA400);
                break;
            }
            case 3: {
                color = context.getResources().getColor(R.color.purpleA200);
                break;
            }
            case 4: {
                color = context.getResources().getColor(R.color.pink400);
                break;
            }
            case 5: {
                color = context.getResources().getColor(R.color.orangeA400);
                break;
            }
            case 6: {
                color = context.getResources().getColor(R.color.limeA400);
                break;
            }
            case 7: {
                color = context.getResources().getColor(R.color.lightBlueA400);
                break;
            }
            case 8: {
                color = context.getResources().getColor(R.color.cyanA400);
                break;
            }

            default: {
                color = context.getResources().getColor(R.color.colorAccent);
                break;
            }
        }

        if (oldColor == color) {
            return getContactColor(context, oldColor);
        } else {
            return color;
        }


    }

}
