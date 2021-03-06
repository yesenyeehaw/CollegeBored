/*
* Extends Parse to keep track of schools that are favorited and the user that favorited it
 */
package com.codepath.collegebored.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Favorite")
public class Favorite extends ParseObject {

    public Favorite(){}

    public static final String KEY_USER = "User";
    public static final String KEY_SCHOOL = "School";
    public static final String KEY_SCHOOL_NAME = "SchoolName";

    //public static toSchool()

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseObject getSchool(){
        return getParseObject(KEY_SCHOOL);
    }

    public void setSchool(ParseObject school){
        put(KEY_SCHOOL, school);
    }

    public String getSchoolName() { return getString(KEY_SCHOOL_NAME); }

    public void setSchoolName(String name) { put(KEY_SCHOOL_NAME, name); }


}
