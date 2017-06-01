package com.example.varma.contacts.Objects;


import android.annotation.SuppressLint;

import com.example.varma.contacts.Extra.Utilis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallLogInfo {

    private String callerName, callernumber, callDuration, callType, callTime, calldate, copyCalls, originalNumber;


    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallernumber() {
        return callernumber;
    }

    public void setCallernumber(String callernumber) {
        this.originalNumber = callernumber;
        callernumber = callernumber.replaceAll(" ", "");
        callernumber = callernumber.replaceAll("\\+91", "");
        if (callernumber.charAt(0) == '0') {
            callernumber = callernumber.substring(1);
        }
        this.callernumber = callernumber;
    }

    public String getCopyCalls() {
        return copyCalls;
    }

    public void setCopyCalls(String copyCalls) {

        if (copyCalls.equals("1")) {
            this.copyCalls = "";
        } else {
            this.copyCalls = "(" + copyCalls + ")";
        }

    }

    public String getOriginalNumber() {
        return originalNumber;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = "";
        int duration = Integer.parseInt(callDuration.trim());
        int temp;
        if (duration >= 3600) {
            temp = duration / 3600;
            duration = duration % 3600;
            if (temp == 1) {
                this.callDuration = "1 hour ";
            } else {
                this.callDuration = String.valueOf(temp) + " hours";
            }
        }
        if (duration >= 60) {
            temp = duration / 60;
            duration = duration % 60;
            if (temp == 1) {
                this.callDuration = this.callDuration + "1 min ";
            } else {
                this.callDuration = this.callDuration + String.valueOf(temp) + " mins ";
            }
        }
        if (duration <= 1) {
            this.callDuration = this.callDuration + String.valueOf(duration) + " sec";
        } else {
            this.callDuration = this.callDuration + String.valueOf(duration) + " secs";
        }

    }

    public String getCalldate() {
        return calldate;
    }

    @SuppressLint("SimpleDateFormat")
    public void setCalldate(String calldate) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        callTime = sdf.format(new Date(Long.parseLong(calldate)));

        this.calldate = Utilis.getDateCallLog(calldate);

    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallTime() {
        return callTime;
    }
}
