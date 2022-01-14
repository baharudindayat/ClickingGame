package com.example.taa;

public class Player {
    private String name;
    private String difficulty;
    private int level;
    private int score;

    public Player(String name, String difficulty, int level, int score) {
        this.name = name;
        this.difficulty = difficulty;
        this.level = level;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
}
