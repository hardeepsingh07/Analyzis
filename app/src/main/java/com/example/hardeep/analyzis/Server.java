package com.example.hardeep.analyzis;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Server {

    private static Random rand;
    private static int range;
    private static ArrayList<HashMap<String, String>> sessionList, eventList;
    private static HttpURLConnection conn;
    private static URL urlAddSession, urlAddEvent, urlGetEvents, urlGetSessions, urlDeleteSessions, UrlDeleteEvents;

    public Server() throws Exception {
        urlAddSession = new URL("http://razp1.ddns.net:5000/addSession");
        urlAddEvent = new URL("http://razp1.ddns.net:5000/addEvent");
        urlGetEvents = new URL("http://razp1.ddns.net:5000/getAllEvents");
        urlGetSessions = new URL("http://razp1.ddns.net:5000/getAllSessions");
        urlDeleteSessions = new URL("http://razp1.ddns.net:5000/dropSessions");
        UrlDeleteEvents = new URL("http://razp1.ddns.net:5000/dropEvents");
    }

    void fillSession() throws Exception {
        // Sessions
        System.out.println("Session List: ");
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Name", "Screen" + i);
            map.put("Count", rand.nextInt(range) + "");
            sessionList.add(map);
        }
        printList(sessionList);
    }

    void fillEvent() throws Exception {
        // Events
        System.out.println("\nEvent List: ");
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Name", "Event" + i);
            map.put("Count", rand.nextInt(range) + "");
            eventList.add(map);
        }
        printList(eventList);
    }

    String deleteSession() throws Exception {
        // Delete Sessions Table
        System.out.println("\nDeleting Session Database...");
        conn = (HttpURLConnection) urlDeleteSessions.openConnection();
        conn.setRequestMethod("DELETE");
        System.out.print("Session Delete Response: ");
        return readData();
    }

    String deleteEvent() throws Exception {
        // Delete Events Table
        System.out.println("\nDeleting Event Database...");
        conn = (HttpURLConnection) UrlDeleteEvents.openConnection();
        conn.setRequestMethod("DELETE");
        System.out.print("Event Delete Response: ");
        return readData();
    }

    String getSessions() throws Exception {
        // Print Session Database
        System.out.println("\nSession Database...");
        conn = (HttpURLConnection) urlGetSessions.openConnection();
        conn.setRequestMethod("GET");
        System.out.println("Session Table: ");
        return readData();
    }

    String getEvents() throws Exception {
        // Print Event Database
        System.out.println("\nEvent Database...");
        conn = (HttpURLConnection) urlGetEvents.openConnection();
        conn.setRequestMethod("GET");
        System.out.println("Event Table: ");
        return readData();
    }

    public String postSession(ArrayList<HashMap<String, String>> data) throws Exception {
        // Post in Session Database
        System.out.println("\nPosting in Session Database...");
        for (HashMap<String, String> h : data) {
            conn = (HttpURLConnection) urlAddSession.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            String json = new JSONObject(h).toString();
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

        }
        return readData();
    }

    public String postEvent(ArrayList<HashMap<String, String>> data) throws Exception {
        // Post in Event Database
        System.out.println("\nPosting in Event Database...");
        for (HashMap<String, String> h : data) {
            conn = (HttpURLConnection) urlAddEvent.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            String json = new JSONObject(h).toString();
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

        }
        return readData();
    }

    public void printList(ArrayList<HashMap<String, String>> list) {
        // Print ArrayList of HashMap
        int i = 0;
        for (HashMap<String, String> h : list) {
            i++;
            for (Map.Entry<String, String> entry : h.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(i + "." + " " + key + " : " + value);
            }
        }
    }

    public String readData() throws Exception {
        // read the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String print = "";
        while ((line = rd.readLine()) != null) {
            print += line;
        }
        rd.close();
        return print;
    }
}
