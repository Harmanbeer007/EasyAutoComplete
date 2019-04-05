package harmanbeer007.easylibrary.easyautocompleteview;

import android.net.Uri;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import harmanbeer007.easylibrary.easyautocompleteview.utils.CustomLog;

class HttpConnector {

    static String getResponse(String urlString, String searchParam, String query) {
        CustomLog.d("HttpConnector", "URL ==> " + appendInput(urlString + "/?" + searchParam + "=", query) + " searchParam of Method GET" + searchParam + " query=" + query);


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            URL url = new URL(appendInput(urlString + "/&" + searchParam + "=", query));
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[512];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }


        } catch (MalformedURLException e) {
            CustomLog.e("AppUtil", "Error processing Autocomplete API URL", e);
        } catch (IOException e) {
            CustomLog.e("AppUtil", "Error connecting to Autocomplete API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        CustomLog.d("Result", jsonResults.toString());
        return jsonResults.toString();
    }

    static String getPostResponse(String urlString, String searchParam, String searchTerm) {
        CustomLog.d("HttpConnector", "URL ==> " + urlString + " searchParam of Method POST" + searchParam + " query=" + searchTerm);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new NameValuePair(searchParam, searchTerm));
//            params.add(new NameValuePair("secondParam", paramValue2));
//            params.add(new NameValuePair("thirdParam", paramValue3));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[512];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }


        } catch (MalformedURLException e) {
            CustomLog.e("AppUtil", "Error processing Autocomplete API URL", e);
        } catch (IOException e) {
            CustomLog.e("AppUtil", "Error connecting to Autocomplete API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        CustomLog.d("Result", jsonResults.toString());
        return jsonResults.toString();
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static class NameValuePair {
        String name;
        String value;

        public String getName() {
            return name;
        }

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static String appendInput(String mAutocompleteUrl, String query) {
        return mAutocompleteUrl + Uri.encode(query);
    }
}
