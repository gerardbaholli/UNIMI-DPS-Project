package university;

import com.example.university.StudentOuterClass.Student;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 9999);

        Student stud =
                Student.newBuilder()
                .setName("Gerard")
                .setSurname("Baholli")
                .setYear(1994)
                .addExam(Student.Exam.newBuilder()
                        .setName("ASD")
                        .setMark(30)
                        .setDate("18/11/2018")
                        .build())
                .addExam(Student.Exam.newBuilder()
                        .setName("SO")
                        .setMark(18)
                        .setDate("13/12/2017")
                        .build())
                .build();

        stud.writeTo(s.getOutputStream());

        s.close();

    }

}
