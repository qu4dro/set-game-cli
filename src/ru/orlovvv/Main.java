package ru.orlovvv;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String set_server_url = "http://194.176.114.21:8058"; 
    public static int token = -1;
    public static ArrayList<Card> cards = new ArrayList<>();
    public static Scanner s = new Scanner(System.in);
    public static String name;

    public static void main(String[] args) throws IOException {
        System.out.println("Введи имя, путник интернета: ");
        name = s.nextLine();
        registration(name);
        Request fetchCardsRequest = new Request("fetch_cards", token);
        Response fetchCardsResponse = serverRequest(fetchCardsRequest);
        assert fetchCardsResponse != null;
        cards = fetchCardsResponse.cards;
        System.out.println(cards.toString());

    }

    public static Response serverRequest(Request req) {
        Gson gson = new Gson();
        try {
            URL url = new URL(set_server_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStream out = urlConnection.getOutputStream();
            out.write(gson.toJson(req).getBytes());
            InputStream stream = urlConnection.getInputStream();
            return gson.fromJson(new InputStreamReader(stream), Response.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void registration(String name) {
        Request register = new Request("register", name, -1);
        Response resp = serverRequest(register);
        assert resp != null;
        token = resp.token;
        if (resp.status.equals("ok")) {
            System.out.println("Ваше имя " + name + " и ваш токен - " + token);
        } else {
            System.out.println("Занято. Необходимо другое имя: ");
            name = s.nextLine();
            registration(name);
        }
    }

}
