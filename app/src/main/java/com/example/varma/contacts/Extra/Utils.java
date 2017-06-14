package com.example.varma.contacts.Extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.varma.contacts.Objects.CallLogInfo;
import com.example.varma.contacts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;


public class Utils {


    public static boolean isEmailValid(String email) {

        return (email.contains("@") && email.contains("."));

    }

    public static boolean isPasswordValid(String pass) {
        return ((pass.length() > 5) && (pass.length() < 20));
    }

    public static String getFirstLetter(String name) {

        name = name.trim();
        if (name.length() <= 0) {
            return " ";
        }
        String c = Character.toString(name.charAt(0));
        if (Pattern.matches("[a-zA-Z]", c)) {
            return c;
        } else {
            return " ";
        }
    }

    public static boolean isUserNameValid(String name) {
        return Pattern.matches("^[a-zA-Z](?:[_ -]?[a-zA-Z0-9])*$", name);
    }

    public static String isUserIdValid(String userId) {

        if (userId.length() < 4) {
            return "Minimum length is 4";

        }

        if (userId.length() > 16) {
            return "Maximum length is 15";
        }

        if (userId.contains(" ")) {
            return "No Spaces in User Id";
        }

        if (Pattern.matches("^[a-zA-Z](?:[_]?[a-zA-Z0-9])*$", userId)) {
            return "";
        } else {
            return "User Id Should Contain Only Letters and Numbers";
        }
    }

    public static boolean internetConnectionStatus(Context context) {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));


        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();


    }

    public static void closeKeyboard(Context context, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

        }
    }

    public static String addCharToString(String string, String letter, int startPosition, int endPosition) {
        return (string.substring(0, startPosition) + letter + string.substring(endPosition, string.length()));
    }

    public static String removeCharFromString(String string, String letter, int startPosition, int endPosition) {

        if (startPosition == 0) {
            return string;
        } else {
            return (string.substring(0, startPosition - 1) + letter + string.substring(endPosition, string.length()));
        }

    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateCallLog(String date) {

        int d, cd, m, cm;

        //-------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date currentDate = Calendar.getInstance().getTime();
        Date date1 = new Date(Long.parseLong(date));

        //-------
        cd = Integer.parseInt(sdf.format(currentDate));
        d = Integer.parseInt(sdf.format(date1));
        //
        sdf = new SimpleDateFormat("MM");
        cm = Integer.parseInt(sdf.format(currentDate));
        m = Integer.parseInt(sdf.format(date1));
        //-------------
        if (m == cm) {
            if (cd == d) {
                return "Today";
            } else if (d == cd - 1) {
                return "Yesterday";
            }
        }
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date1);
    }

    public static ArrayList<CallLogInfo> callLogMaintainer(ArrayList<CallLogInfo> callLogInfos) {
        ArrayList<CallLogInfo> callLogs = new ArrayList<>();
        callLogs.addAll(callLogInfos);

        boolean temp;
        int i = 0;
        int j;
        int copyCalls = 1;
        while (i < callLogs.size() - 1) {
            j = i;
            do {
                if (callLogs.get(i).getCallernumber().equals(callLogs.get(i + 1).getCallernumber())
                        &&
                        callLogs.get(i).getCallType().equals(callLogs.get(i + 1).getCallType())) {
                    copyCalls = copyCalls + 1;

                    callLogs.remove(i + 1);
                    temp = true;

                } else {
                    temp = false;
                }


            } while (temp && i < callLogs.size() - 1);
            callLogs.get(j).setCopyCalls(String.valueOf(copyCalls));
            copyCalls = 1;
            i++;
        }


        return callLogs;
    }


    public static int getContactColor(Context context, int oldColor) {

        int color;

        Random random = new Random();
        int i = random.nextInt(8);

        switch (i) {
            case 0: {
                color = ContextCompat.getColor(context, R.color.yellowA400);
                break;
            }
            case 1: {
                color = ContextCompat.getColor(context, R.color.cyanA400);
                break;
            }
            case 2: {
                color = ContextCompat.getColor(context, R.color.tealA400);
                break;
            }
            case 3: {
                color = ContextCompat.getColor(context, R.color.purpleA200);
                break;
            }
            case 4: {
                color = ContextCompat.getColor(context, R.color.pink400);
                break;
            }
            case 5: {
                color = ContextCompat.getColor(context, R.color.orangeA400);
                break;
            }
            case 6: {
                color = ContextCompat.getColor(context, R.color.limeA400);
                break;
            }
            case 7: {
                color = ContextCompat.getColor(context, R.color.lightBlueA400);
                break;
            }

            default: {
                color = ContextCompat.getColor(context, R.color.colorAccent);
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
