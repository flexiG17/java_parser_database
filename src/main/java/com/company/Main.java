package com.company;

import com.company.InputData.Vk;
import com.company.People.Student;
import com.company.SqLite.Database;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ClientException, ApiException {

        var students = Student.CreateListWithData(new Vk("iot_second_urfu").find_users(), "1.csv");

        Database.ConnectToProject();
        Database.Clean();
        Database.Create();
        Database.Fill(students);
    }
}

