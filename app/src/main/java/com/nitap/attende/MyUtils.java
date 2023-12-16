package com.nitap.attende;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nitap.attende.models.MyConfiguration;



import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class MyUtils {


    public static void saveString(Context context,String key,String value) {
        SharedPreferences prefs = context.getSharedPreferences("application",Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getString(Context context,String key) {
        SharedPreferences prefs = context.getSharedPreferences("application",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String value = prefs.getString(key, "EMPTY");
        editor.commit();
        return value;
    }
    public static void removeString(Context context,String key) {
        SharedPreferences prefs = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        prefs.edit().remove(key).commit();
    }


    public static Object getObjectFromString(String jsonString, Class class1) {
        ObjectMapper mapper = new ObjectMapper();
        Object resultObject = null;
        try {
            resultObject = mapper.readValue(jsonString, class1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultObject == null) {
            return null;
        } else {
           return resultObject;
        }

    }

    public static String getStringFromObject(Object object) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        String jsonString = gson.toJson(object);
        return jsonString;
    }

    public static void clearAppData(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.clearApplicationUserData();

    }

    public static String getSalt(Context context) {
        String salt = MyUtils.getString(context,"SALT");
        if (Objects.equals(salt, "EMPTY")){
            return null;
        } else {
            return salt;
        }
    }

    public static void setSalt(Context context,String salt) {
        MyUtils.saveString(context,"SALT",salt);
    }
/*
    public static FaceFeatureInfo getFaceFeatureInfo(Context applicationContext, String faceinfoString) {
        ObjectMapper mapper = new ObjectMapper();
        FaceFeatureInfo faceFeatureInfo = new FaceFeatureInfo();
        try {
            faceFeatureInfo = mapper.readValue(faceinfoString, FaceFeatureInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (faceFeatureInfo == null){ return null; }
        else  { return faceFeatureInfo; }
    }
*/
    public static MyConfiguration getConfiguration(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        MyConfiguration myConfiguration = new MyConfiguration();
        try {
            String jsonString = MyUtils.getString(context,"MYCONFIG");
            if (Objects.equals(jsonString, "EMPTY"))
                return null;
            myConfiguration = mapper.readValue(jsonString, MyConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (myConfiguration == null){ return null; }
        else  { return myConfiguration; }
    }

    public static MyConfiguration getConfigurationBuilder(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        MyConfiguration myConfigurationBuilder = new MyConfiguration();
        try {
            String jsonString = MyUtils.getString(context,"MYCONFIGBUILDER");
            if (Objects.equals(jsonString, "EMPTY"))
                return null;
            myConfigurationBuilder = mapper.readValue(jsonString, MyConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (myConfigurationBuilder == null){ return null; }
        else  { return myConfigurationBuilder; }
    }

    public static void saveConfiguration(Context context,MyConfiguration myConfiguration) {
        String updatedString = MyUtils.getStringFromObject(myConfiguration);
        MyUtils.saveString(context,"MYCONFIG",updatedString);
    }

    public static void saveConfigurationBuilder(Context context,MyConfiguration myConfiguration) {
        String updatedString = MyUtils.getStringFromObject(myConfiguration);
        MyUtils.saveString(context,"MYCONFIGBUILDER",updatedString);
    }

    public static void removeConfiguration(Context context) {
        MyUtils.removeString(context,"MYCONFIG");
    }

    public static void removeConfigurationBuilder(Context context) {
        MyUtils.removeString(context,"MYCONFIGBUILDER");
    }


    public static String getCodeFromBinaryString(String binaryString) {
        //int i = Integer.parseInt(s);
        switch (binaryString) {
            case "000000":return "0";
            case "000001":return "1";
            case "000010":return "2";
            case "000011":return "3";
            case "000100":return "4";
            case "000101":return "5";
            case "000110":return "6";
            case "000111":return "7";

            case "001000":return "8";
            case "001001":return "9";
            case "001010":return "a";
            case "001011":return "b";
            case "001100":return "c";
            case "001101":return "d";
            case "001110":return "e";
            case "001111":return "f";

            case "010000":return "g";
            case "010001":return "h";
            case "010010":return "i";
            case "010011":return "j";
            case "010100":return "k";
            case "010101":return "l";
            case "010110":return "m";
            case "010111":return "n";

            case "011000":return "o";
            case "011001":return "p";
            case "011010":return "q";
            case "011011":return "r";
            case "011100":return "s";
            case "011101":return "t";
            case "011110":return "u";
            case "011111":return "v";

            case "100000":return "w";
            case "100001":return "x";
            case "100010":return "y";
            case "100011":return "z";
            case "100100":return "A";
            case "100101":return "B";
            case "100110":return "C";
            case "100111":return "D";

            case "101000":return "E";
            case "101001":return "F";
            case "101010":return "G";
            case "101011":return "H";
            case "101100":return "I";
            case "101101":return "J";
            case "101110":return "K";
            case "101111":return "L";

            case "110000":return "M";
            case "110001":return "N";
            case "110010":return "O";
            case "110011":return "P";
            case "110100":return "Q";
            case "110101":return "R";
            case "110110":return "S";
            case "110111":return "T";

            case "111000":return "U";
            case "111001":return "V";
            case "111010":return "W";
            case "111011":return "X";
            case "111100":return "Y";
            case "111101":return "Z";
            case "111110":return "!";
            case "111111":return "@";


        }

        return null;
    }

    public static String getBinaryStringFromCode(String code) {

        switch (code) {

            case "0":return "000000";
            case "1":return "000001";
            case "2":return "000010";
            case "3":return "000011";
            case "4":return "000100";
            case "5":return "000101";
            case "6":return "000110";
            case "7":return "000111";

            case "8":return "001000";
            case "9":return "001001";
            case "a":return "001010";
            case "b":return "001011";
            case "c":return "001100";
            case "d":return "001101";
            case "e":return "001110";
            case "f":return "001111";

            case "g":return "010000";
            case "h":return "010001";
            case "i":return "010010";
            case "j":return "010011";
            case "k":return "010100";
            case "l":return "010101";
            case "m":return "010110";
            case "n":return "010111";

            case "o":return "011000";
            case "p":return "011001";
            case "q":return "011010";
            case "r":return "011011";
            case "s":return "011100";
            case "t":return "011101";
            case "u":return "011110";
            case "v":return "011111";

            case "w":return "100000";
            case "x":return "100001";
            case "y":return "100010";
            case "z":return "100011";
            case "A":return "100100";
            case "B":return "100101";
            case "C":return "100110";
            case "D":return "100111";

            case "E":return "101000";
            case "F":return "101001";
            case "G":return "101010";
            case "H":return "101011";
            case "I":return "101100";
            case "J":return "101101";
            case "K":return "101110";
            case "L":return "101111";

            case "M":return "110000";
            case "N":return "110001";
            case "O":return "110010";
            case "P":return "110011";
            case "Q":return "110100";
            case "R":return "110101";
            case "S":return "110110";
            case "T":return "110111";

            case "U":return "111000";
            case "V":return "111001";
            case "W":return "111010";
            case "X":return "111011";
            case "Y":return "111100";
            case "Z":return "111101";
            case "!":return "111110";
            case "@":return "111111";

        }


        return null;
    }






}

