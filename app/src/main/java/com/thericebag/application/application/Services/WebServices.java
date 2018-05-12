package com.thericebag.application.application.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by abhilash on 29-08-2016.
 */
public class WebServices {
    public static String checkVersion(Context context) {

        /*try {
            HttpGet httppost;
            String searchText = params[0].replaceAll(" ", "%20");
            if (profileDetails != null) {
                httppost = new HttpGet(AppConstants.URL + "Search/?Location=" + areaID + "&SearchText=" + searchText + "&UserId=" + profileDetails.getUSER_ID());
            } else {
                httppost = new HttpGet(AppConstants.URL + "Search/?Location=" + areaID + "&SearchText=" + searchText);
            }
//				HttpGet httppost = new HttpGet("http://ghapi.in/api/v1/Category?TagName="+params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);

//                  data = StringEscapeUtils.unescapeJava(data);
                data = data.replaceAll("\\\\", "");
                data = data.replaceAll("^\"|\"$", "");

                JSONArray jsonarray = new JSONArray(data);
                keywordList.clear();

                ArrayList<String> categoryIDList = new ArrayList<>();

                for (int i = 0; i < jsonarray.length(); ++i) {
                    JSONObject rec = jsonarray.getJSONObject(i);

                    SearchbyKeywordBean categoryBean = new SearchbyKeywordBean();
                    categoryBean.setSHOP_ID(rec.getString("SHOP_ID"));
                    categoryBean.setSHOP_NAME(rec.getString("SHOP_NAME"));
                    categoryBean.setCATEGORY_ID(rec.getString("CATEGORY_ID"));
                    categoryBean.setCATEGORY_NAME(rec.getString("CATEGORY_NAME"));
                    categoryBean.setGH_CODE(rec.getString("GH_CODE"));
                    categoryBean.setWEBSITE_URL_1(rec.getString("WEBSITE_URL_1"));
                    categoryBean.setPHONE_LINE_1(rec.getString("PHONE_LINE_1"));
                    categoryBean.setPHONE_LINE_2(rec.getString("PHONE_LINE_2"));
                    categoryBean.setLATITUDE(rec.getString("LATITUDE"));
                    categoryBean.setLONGITUDE(rec.getString("LONGITUDE"));
                    categoryBean.setADDRESS(rec.getString("ADDRESS"));
                    categoryBean.setIMAGE_PATH(rec.getString("IMAGE_PATH"));
                    categoryBean.setRATING(rec.getString("RATING"));
                    categoryBean.setIS_SHOPNAME(rec.getString("IS_SHOPNAME"));
                    categoryBean.setIS_CATEGORY(rec.getString("IS_CATEGORY"));
                    categoryBean.setIS_FAV(rec.getString("IS_FAV"));

                    String categoryID = rec.getString("CATEGORY_ID");
                    if (!keywordList.contains(categoryBean)) {       // Remove Duplicate Records
                        if (rec.getString("IS_SHOPNAME").equalsIgnoreCase("false")) {
                            if (!categoryIDList.contains(categoryID)){
                                keywordList.add(categoryBean);
                                categoryIDList.add(categoryID);
                            }
                        } else {
                            keywordList.add(categoryBean);
                        }
                    }
                }

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;*/

        return "";
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
