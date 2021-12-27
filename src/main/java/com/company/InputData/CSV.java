package com.company.InputData;

import com.company.Course.MainData;
import com.company.People.Person;
import com.company.People.Student;
import com.company.Course.Homeworks;
import com.company.Course.Theme;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSV {

    private static final String csvRegex = ";";
    private static final String courseName = "Java";
    private static final List<Integer> listOfAllTasksInCurrentTheme = Arrays.asList(1, 7, 9, 9, 11, 8, 13, 16, 7, 10, 11, 3, 2, 1, 1);

    private static List<List<String>> Read(String filename) {
        List<List<String>> allLinesFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            var currentLine = "";
            while ((currentLine = reader.readLine()) != null) {
                var values = currentLine.split(csvRegex);
                allLinesFromFile.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLinesFromFile;
    }

    public static List<Student> ConvertToListStudents(String filename) {
        List<List<String>> parsedFile = CSV.Read(filename);
        List<Student> studentsListFromCsv = new ArrayList<>();
        List<String> allThemesFromCsv = ConvertToListThemes(parsedFile);
        List<String> allTitlesFromCsv = new ArrayList<>(parsedFile.get(1));

        for (var currentLine : parsedFile.subList(3, parsedFile.size())) {
            var studentData = currentLine.get(0).split(" ");
            var person = new Person(studentData[1], studentData[0]);
            var allCourses = new ArrayList<MainData>();
            var allStudentThemes = new ArrayList<Theme>();

            var startIndex = 2;
            for (var currentTheme = 0; currentTheme < allThemesFromCsv.size(); currentTheme++) {
                var allTasks = new ArrayList<Homeworks>();
                var tasksOfTheme = allTitlesFromCsv.subList(startIndex, startIndex + listOfAllTasksInCurrentTheme.get(currentTheme));
                var taskValuePosition = startIndex;
                var taskMaxScorePosition = 0;

                for (var task : tasksOfTheme) {
                    allTasks.add(new Homeworks(
                            task,
                            Integer.parseInt(currentLine.get(taskValuePosition)),
                            Integer.parseInt(parsedFile.get(2).get(startIndex + taskMaxScorePosition))));
                    taskValuePosition++;
                    taskMaxScorePosition++;
                }

                allStudentThemes.add(new Theme(
                        allThemesFromCsv.get(currentTheme),
                        allTasks,
                        allTasks.get(0).getTaskScore(),
                        Integer.parseInt(parsedFile.get(2).get(startIndex))));
                startIndex += listOfAllTasksInCurrentTheme.get(currentTheme);
            }

            allCourses.add(new MainData(
                    courseName,
                    allStudentThemes,
                    Integer.parseInt(parsedFile.get(2).get(2)),
                    currentLine.get(1)));
            studentsListFromCsv.add(new Student(person, allCourses));
        }
        return studentsListFromCsv;
    }

    private static List<String> ConvertToListThemes(List<List<String>> parsedCSV) {
        var themes = new ArrayList<String>();
        for (String theme : parsedCSV.get(0)) {
            if (theme.length() > 1)
                themes.add(theme);
        }
        return themes;
    }
}
