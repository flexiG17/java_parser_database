package com.company.SqLite;

import com.company.Course.MainData;
import com.company.People.Student;
import java.sql.*;
import java.util.*;

public class Database {
    private static Connection connection;
    private static Statement statement;

    public static void ConnectToProject() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:vk_base.s3db");
        statement = connection.createStatement();
    }

    public static void Create() throws SQLException {

        statement.execute(
                "CREATE TABLE IF NOT EXISTS data_about_course_from_csv\n" +
                    "(\n" +
                    "id INTEGER NOT NULL\n" +
                    "PRIMARY KEY AUTOINCREMENT,\n" +
                    "courseName         TEXT    NOT NULL,\n" +
                    "maxScore           INTEGER NOT NULL,\n" +
                    "courseGroup        TEXT    NOT NULL,\n" +
                    "student_id          INTEGER NOT NULL\n);"
        );

        statement.execute(
                "CREATE TABLE IF NOT EXISTS people_data_from_vk(\n" +
                    "personId           INTEGER NOT NULL\n" +
                    "PRIMARY KEY        AUTOINCREMENT,\n" +
                    "name               TEXT    NOT NULL,\n" +
                    "surname            TEXT    NOT NULL,\n" +
                    "city               TEXT    NOT NULL,\n" +
                    "birthdate          DATE,            \n" +
                    "image              TEXT    NOT NULL,\n" +
                    "vkId               INTEGER NOT NULL,\n" +
                    "gender             INTEGER DEFAULT 0 NOT NULL\n);"
        );

        statement.execute(
                "CREATE TABLE IF NOT EXISTS student_data_from_csv(\n" +
                    "student_id         INTEGER NOT NULL\n" +
                    "REFERENCES         people_data_from_vk,\n" +
                    "course_id          INTEGER NOT NULL\n" +
                    "REFERENCES         data_about_course_from_csv\n);"
        );

        statement.execute(
                "CREATE TABLE IF NOT EXISTS task_in_course(\n" +
                    "id                 INTEGER NOT NULL\n" +
                    "PRIMARY KEY        AUTOINCREMENT,\n" +
                    "task_name          TEXT    NOT NULL,\n" +
                    "score              INTEGER DEFAULT 0 NOT NULL,\n" +
                    "theme_id           INTEGER NOT NULL\n" +
                    "REFERENCES         themes_in_course,\n" +
                    "max_score          INTEGER DEFAULT 0 NOT NULL\n);"
        );

        statement.execute(
                "CREATE TABLE IF NOT EXISTS themes_in_course(\n" +
                    "theme_name         TEXT    NOT NULL,\n" +
                    "studentMaxPoint    INTEGER NOT NULL,\n" +
                    "maxPoint           INTEGER NOT NULL,\n" +
                    "course_id          INTEGER NOT NULL\n" +
                    "REFERENCES         data_about_course_from_csv,\n" +
                    "theme_id            integer not null\n" +
                    "PRIMARY KEY        AUTOINCREMENT\n);"
        );
    }

    public static void Write(Student student) throws SQLException {
        var courses = student.getAllCourseData();

        insertPerson(
                new String[]{
                        student.getName(),
                        student.getSurname(),
                        student.getCity(),
                        student.getBirthdate(),
                        student.getPhoto(),
                        String.valueOf(student.getVkId()),
                        String.valueOf(student.getGender())});

        var student_id = statement.executeQuery(
                "SELECT * FROM people_data_from_vk " +
                    "WHERE name='" + student.getName() + "' " +
                        "AND surname= '" + student.getSurname() + "' " +
                        "AND vkId=" + student.getVkId() + ";").getInt("personId");
        var coursesIds = insertCourses(courses, student_id);

        for (var id : coursesIds) {
            var query =
                    "INSERT INTO student_data_from_csv (student_id, course_id)\n"
                    + "VALUES (" + student_id + ", " + id + ");";
            try {
                statement.execute(query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void Clean() {
        try {
            statement.execute("DELETE FROM data_about_course_from_csv");
            statement.execute("DELETE FROM people_data_from_vk");
            statement.execute("DELETE FROM student_data_from_csv");
            statement.execute("DELETE FROM task_in_course");
            statement.execute("DELETE FROM themes_in_course");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Close() throws SQLException {
        statement.close();
        connection.close();
        System.out.println("Соединения закрыты");
    }

    public static void Fill(List<Student> students) throws SQLException {
        for (var student : students) {
            Write(student);
            if (student.getVkId() == -1000)
                continue;
            System.out.println("\n_____________\n"
                    + "user_id: " + student.getVkId()
                    + "\nSurname Name: " + student.getName() + " " + student.getSurname()
                    + "\nCourse name: " +  student.getAllCourseData().get(0).getCourseName());
        }
    }

    private static void insertPerson(String[] person) {
        var builder = new StringBuilder();
        for (var i : person) {
            builder.append("'").append(i).append("'").append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        var query =
                "INSERT INTO people_data_from_vk (name, surname, city, birthdate, image, vkId, gender)\n"
                + "values (" + builder + ");";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Integer> insertCourses(List<MainData> courses, int student_id) throws SQLException {
        var result = new ArrayList<Integer>();
        for (var course : courses) {
            //---Записываем основные данные курса---------------------

            var courseName = course.getCourseName();
            var maxScore = course.getMaxScoreInTheme();
            var group = course.getLearningGroup();

            var courseQuery =
                    "INSERT INTO data_about_course_from_csv (courseName, maxScore, courseGroup, student_id)\n"
                    + "VALUES ('" + courseName + "', " + maxScore + ", '" + group + "', " + student_id + ");";
            try {
                statement.execute(courseQuery);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            //------------------------
            var courseId = statement.executeQuery(
                    "SELECT * FROM data_about_course_from_csv "
                        + "WHERE courseName='" + courseName + "' AND student_id=" + student_id + ";").getInt("id");
            result.add(courseId);
            for (var theme : course.getListOfThemes()) {
                var themeName = theme.getName();
                var studentMaxPoint = theme.getStudentMaxScore();
                var maxPoint = theme.getMaxPoint();

                var themeQuery =
                        "INSERT INTO themes_in_course (theme_name, studentMaxPoint, maxPoint, course_id)\n"
                         + "VALUES ('" + themeName + "', " + studentMaxPoint + ", " + maxPoint + ", " + courseId + ");";

                try {
                    statement.execute(themeQuery);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                var themeId = statement.executeQuery(
                        "SELECT * FROM themes_in_course "
                            + "WHERE theme_name='" + themeName + "' and course_id =" + courseId + ";").getInt("theme_id");

                for (var task : theme.getListHomeworks()) {
                    var taskScore = task.getTaskScore();
                    var taskName = task.getTaskName();

                    var taskQuery =
                            "INSERT INTO task_in_course (task_name, score, theme_id, max_score)\n" +
                            "VALUES ('" + taskName + "', " + taskScore + ", '" + themeId + "', " + task.getMaxTaskScore() + ");";

                    try {
                        statement.execute(taskQuery);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }
}