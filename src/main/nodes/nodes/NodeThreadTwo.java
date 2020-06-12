package nodes;

import java.util.Scanner;
import java.util.Timer;

public class NodeThreadTwo extends Thread {
    String frase;

    public NodeThreadTwo(String s){
        this.frase = s;
    }

    public void run() {

        Scanner scan = new Scanner(System.in);

        while (true){
            System.out.println("Thread 2 ci sono");

            String userName = scan.nextLine();
            System.out.println("Username is: " + userName);
        }


    }


}
