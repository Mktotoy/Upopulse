package com.upo10.miage.upopulse.upowbs;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nelmojahid on 06/05/2015.
 */
public class WebService
{

    private static final int DATARETRIEVAL_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String LOGGER_TAG = "";

    public static JSONObject requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }


    public static String postRequest(String url, String postParameters) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        int statusCode = 0;

        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
            // handle POST parameters
            if (postParameters != null)
            {

                if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
                    Log.i(LOGGER_TAG, "POST parameters: " + postParameters);
                }
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // handle issues
            statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                return "Une erreur s'est produite avec le webservice. Veuillez réessayer ultérieurement. ";
            }

            // read output (only for GET)

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return getResponseText(in);

        } catch (MalformedURLException e) {
            // handle invalid URL
            return "URL exception";
        } catch (SocketTimeoutException e) {
            // hadle timeout
            return "TimeOut";
        } catch (IOException e) {
            // handle I/0
            return "Quelque chose s'est mal passé";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static String putRequest(String url, String postParameters) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        int statusCode = 0;

        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
            // handle POST parameters
            if (postParameters != null)
            {

                if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
                    Log.i(LOGGER_TAG, "PUT parameters: " + postParameters);
                }
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }

            // handle issues
            statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                //return "Une erreur s'est produite avec le webservice. Veuillez réessayer ultérieurement. ";
                return "" + statusCode;
            }

            // read output (only for GET)

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return getResponseText(in);

        } catch (MalformedURLException e) {
            // handle invalid URL
            return "URL exception";
        } catch (SocketTimeoutException e) {
            // hadle timeout
            return "TimeOut";
        } catch (IOException e) {
            // handle I/0
            return "Quelque chose s'est mal passé";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
