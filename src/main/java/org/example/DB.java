package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public interface DB {
    List<TgUser> TG_USERS = new ArrayList<>();
    List<User> USERS = new ArrayList<>();
    List<Post> POSTS = new ArrayList<>();
    List<Comment> COMMENTS = new ArrayList<>();


    static void load(){

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String str = response.body();
            List<User> users = loadUser(str);
            USERS.addAll(users);
            writeToFileUsers(users);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String str = response.body();
            List<Post> posts = loadPosts(str);
            POSTS.addAll(posts);
            writeToFilePosts(posts);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/comments"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String str = response.body();
            List<Comment> comments = loadComments(str);
            COMMENTS.addAll(comments);
            writeToFileComments(comments);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static void upload(){
        try {
            String str = Files.readString(Path.of("users.json"));
            Gson gson = new Gson();
            List<User> users = gson.fromJson(str,new TypeToken<List<User>>(){}.getType());
            USERS.addAll(users);

            String str1 = Files.readString(Path.of("posts.json"));
            Gson gson1 = new Gson();
            List<Post> posts = gson1.fromJson(str1,new TypeToken<List<Post>>(){}.getType());
            POSTS.addAll(posts);

            String str2 = Files.readString(Path.of("comments.json"));
            Gson gson2 = new Gson();
            List<Comment> comments = gson2.fromJson(str2,new TypeToken<List<Comment>>(){}.getType());
            COMMENTS.addAll(comments);

        } catch (Exception e) {
            load();
        }
    }

    static void writeToFileUsers(List<User> users) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(users);
        Files.writeString(Path.of("users.json"),json,StandardOpenOption.TRUNCATE_EXISTING);
    }

    static void writeToFilePosts(List<Post> posts) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(posts);
        Files.writeString(Path.of("posts.json"),json,StandardOpenOption.TRUNCATE_EXISTING);
    }

    static void writeToFileComments(List<Comment> comments) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(comments);
        Files.writeString(Path.of("comments.json"),json,StandardOpenOption.TRUNCATE_EXISTING);
    }

    static List<Comment> loadComments(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str,new TypeToken<List<Comment>>(){}.getType());
    }

    static List<Post> loadPosts(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str,new TypeToken<List<Post>>(){}.getType());
    }

    static List<User> loadUser(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str,new TypeToken<List<User>>(){}.getType());
    }
}
