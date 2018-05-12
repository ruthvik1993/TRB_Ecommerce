package com.thericebag.application.application.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abhilash on 01-09-2016.
 */

public final class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static String getRequestBodyOfJson(HashMap map) {
        JSONObject jsonObject = new JSONObject();

        if (map != null && map.size() != 0) {
            Iterator myVeryOwnIterator = map.keySet().iterator();

            try {


                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();

                    Object obj = map.get(key);
                    System.out.print(obj.getClass());
                    if (obj.getClass().equals(String.class)) {
                        jsonObject.put(key, (String) obj);
                    } else if (obj.getClass().equals(Integer.class)) {
                        jsonObject.put(key, (Integer) obj);
                    } else if (obj.getClass().equals(Float.class)) {
                        jsonObject.put(key, (Float) obj);
                    } else if (obj.getClass().equals(boolean.class)) {
                        jsonObject.put(key, (boolean) obj);
                    } else if (obj.getClass().equals(Double.class)) {
                        jsonObject.put(key, (Double) obj);
                    }
                }
            } catch (JSONException e) {

            }
        }

        return jsonObject.toString();

    }
}