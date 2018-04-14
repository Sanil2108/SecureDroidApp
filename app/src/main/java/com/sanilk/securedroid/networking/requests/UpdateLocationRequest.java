package com.sanilk.securedroid.networking.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by root on 14/4/18.
 */
public class UpdateLocationRequest extends Request {
    public static final String REQUEST_TYPE="UPDATE_LOCATION_REQUEST";
    public static final String USER_NAME_KEY="user_name";
    public static final String PASSWORD_KEY="password";
    public static final String LOCATIONS_KEY="locations";

    private String userName;
    private String password;
    private Location location;

    public UpdateLocationRequest(){
        requestType=REQUEST_TYPE;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getJSONString(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REQUEST_TYPE_KEY, REQUEST_TYPE);
            jsonObject.put(USER_NAME_KEY, userName);
            jsonObject.put(PASSWORD_KEY, password);

            JSONArray locationsArray=new JSONArray();
            locationsArray.put(location.getJSONObject());

            jsonObject.put(LOCATIONS_KEY, locationsArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static class Location{
        public static final String LAT_KEY="lat";
        public static final String LONG_KEY="long";
        public static final String DATE_KEY="date";

        private double lattitude;
        private double longitude;
        private Date date;

        public Location(){}

        public double getLattitude() {
            return lattitude;
        }

        public void setLattitude(double lattitude) {
            this.lattitude = lattitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public JSONObject getJSONObject() throws Exception{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(LAT_KEY, lattitude);
            jsonObject.put(LONG_KEY, longitude);
            jsonObject.put(DATE_KEY, date);
            return  jsonObject;
        }
    }
}
