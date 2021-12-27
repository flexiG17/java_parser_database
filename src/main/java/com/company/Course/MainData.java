package com.company.Course;

import java.util.List;

public class MainData {
    private final String courseName;
    private final List<Theme> listOfThemes;
    private final int maxScoreInTheme;
    private final String learningGroup;

    public MainData(String name, List<Theme> themes, int maxScore, String group) {
        this.courseName = name;
        this.listOfThemes = themes;
        this.maxScoreInTheme = maxScore;
        this.learningGroup = group;
    }

    public int getMaxScoreInTheme() {
        return maxScoreInTheme;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<Theme> getListOfThemes() {
        return listOfThemes;
    }

    public String getLearningGroup() {
        return learningGroup;
    }
}
