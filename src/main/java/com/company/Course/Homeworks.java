package com.company.Course;

public class Homeworks {
    private final String taskName;
    private final int taskScore;
    private final int maxTaskScore;

    public Homeworks(String name, int score, int maxScore) {
        this.taskName = name;
        this.taskScore = score;
        this.maxTaskScore = maxScore;
    }

    public String getTaskName() { return taskName; }

    public int getTaskScore() { return taskScore; }

    public int getMaxTaskScore() { return maxTaskScore; }
}
