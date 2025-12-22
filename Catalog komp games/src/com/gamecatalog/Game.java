package com.gamecatalog;

import java.io.Serializable;
import java.util.UUID;

/**
 * Класс, представляющий компьютерную игру
 */
public class Game implements Serializable {
    private final String id;
    private String title;
    private Genre genre;
    private int releaseYear;
    private double rating;
    private String developer;
    private String platform;

    public enum Genre {
        ACTION, RPG, ACTION_RPG
    }

    public Game(String title, Genre genre, int releaseYear, double rating, String developer, String platform) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.developer = developer;
        this.platform = platform;
    }

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public Genre getGenre() { return genre; }
    public int getReleaseYear() { return releaseYear; }
    public double getRating() { return rating; }
    public String getDeveloper() { return developer; }
    public String getPlatform() { return platform; }

    // Сеттеры
    public void setTitle(String title) { this.title = title; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setRating(double rating) { this.rating = rating; }
    public void setDeveloper(String developer) { this.developer = developer; }
    public void setPlatform(String platform) { this.platform = platform; }

    @Override
    public String toString() {
        return String.format("%-40s | %-12s | %4d | %4.1f | %-25s | %-15s",
                title.length() > 37 ? title.substring(0, 37) + "..." : title,
                genre.toString().replace("_", "/"),
                releaseYear,
                rating,
                developer.length() > 22 ? developer.substring(0, 22) + "..." : developer,
                platform.length() > 12 ? platform.substring(0, 12) + "..." : platform);
    }
}