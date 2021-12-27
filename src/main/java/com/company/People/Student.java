package com.company.People;

import com.company.Course.MainData;
import com.company.InputData.CSV;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Optional;

public class Student extends Person {
    private final List<MainData> allCourseData;

    public Student(Person p, List<MainData> courses) {
        super(p.getName(), p.getSurname());
        this.allCourseData = courses;
    }

    public List<MainData> getAllCourseData() { return allCourseData; }

    public static List<Student> CreateListWithData(List<JsonObject> vkData, String filename) {
        List<Student> students = CSV.ConvertToListStudents(filename);
        for (var student : students) {
            var filtered = Optional.<JsonObject>empty();
            for (JsonObject a : vkData) {
                if (a.get("first_name")
                        .getAsString()
                        .equals(student.getName())
                        && a
                        .get("last_name")
                        .getAsString()
                        .equals(student.getSurname())) {
                    filtered = Optional.of(a);
                    break;
                }
            }

            if (filtered.isPresent()) {
                var value = filtered.get();
                if (value.has("bdate")) {
                    var birthdate = value.get("bdate").getAsString();
                    if (birthdate != null && !birthdate.equals(""))
                        student.setBirthdate(birthdate);
                }

                if (value.has("city")) {
                    var city = value.get("city").getAsJsonObject().get("title").getAsString();
                    if (!city.equals(""))
                        student.setCity(city);
                }

                if (value.has("sex")) {
                    var gender = value.get("sex").getAsInt();
                    if (gender == 1)
                        student.setGender(Gender.Female);
                    if (gender == 2)
                        student.setGender(Gender.Male);
                }

                var photo = value.get("photo_max").getAsString();
                if (!photo.equals(""))
                    student.setPhoto(photo);

                student.setVkId(value.get("id").getAsInt());
            }
        }
        return students;
    }
}
