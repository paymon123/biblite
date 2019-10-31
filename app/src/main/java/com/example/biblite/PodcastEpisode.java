package com.example.biblite;

public class PodcastEpisode {
    static int open = 3;
    static int opens = 0;
    String title;
    String link;
    boolean ready = false;
    public PodcastEpisode(String title, String link, boolean r){


    this.ready = r;
    this.title = title;
    this.link = link;


    }
}
