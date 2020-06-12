package nodes;


import java.util.Scanner;

public class NodeThread extends Thread{

    String frase;

    public NodeThread(String s){
        this.frase = s;
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Thread 1 ci sono");


            String userName = scan.nextLine();
            System.out.println(frase + " " + userName);
        }


    }


}


