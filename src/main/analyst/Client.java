import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {


    public static void main(String[] args) throws IOException {

        int response = 0;
        int n = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println("INTERFACCIA ANALISTI");
            System.out.println("Seleziona cosa visualizzare:");
            System.out.println("1) Numero di nodi presenti nella rete");
            System.out.println("2) Ultime n statistiche (con timestamp) di quartiere");
            System.out.println("3) Deviazione standard e media delle ultime n statistiche prodotte dal quartiere");
            response = Integer.parseInt(br.readLine());
        } while (response != 1 && response != 2 && response != 3);


        if (response == 1) {
            System.out.println("Stampa 1");
        } else if (response == 2) {
            do {
                System.out.println("Inserire n statistiche da analizzare (n > 0)");
                n = Integer.parseInt(br.readLine());
            } while (n < 1);
            System.out.println("Stampa 2 - " + n);
        } else if (response == 3) {
            do {
                System.out.println("Inserire n statistiche da analizzare (n > 0)");
                n = Integer.parseInt(br.readLine());
            } while (n < 1);
            System.out.println("Stampa 3 - " + n);
        }

    }

}
