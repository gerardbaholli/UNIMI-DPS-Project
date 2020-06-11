package university;

import com.example.university.StudentOuterClass.Student;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(9999);
        Socket s = ss.accept();

        Student stud = Student.parseFrom(s.getInputStream());

        System.out.println(stud);

    }


}
