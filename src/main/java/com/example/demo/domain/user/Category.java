package com.example.demo.domain.user;

public enum Category {
    TECHNOLOGY("Tech"),
    SCIENCE("Science"),
    ART("Art"),
    MUSIC("Music"),
    SPORTS("Sports");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}