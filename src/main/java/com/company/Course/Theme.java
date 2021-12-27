package com.company.Course;

import java.util.List;

public class Theme {
    private final String themeName;
    private final List<Homeworks> listHomeworks;
    private final int studentMaxScore;
    private final int maxPointTheme;

    public Theme(String name, List<Homeworks> tasks, int studentMaxPoint, int maxPoint) {
        this.themeName = name;
        this.listHomeworks = tasks;
        this.studentMaxScore = studentMaxPoint;
        this.maxPointTheme = maxPoint;
    }

    public List<Homeworks> getListHomeworks() { return listHomeworks; }

    public String getName() { return themeName; }

    public int getStudentMaxScore() { return studentMaxScore; }

    public int getMaxPoint() { return maxPointTheme; }
}
