import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import stats.Stat;
import stats.Stats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Analyst {


    public static void main(String[] args) throws IOException {

        int response = 0;
        int n = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Client client = Client.create();

        do {

            do {
                System.out.println("INTERFACCIA ANALISTI");
                System.out.println("Seleziona cosa visualizzare:");
                System.out.println("1) Numero di nodi presenti nella rete");
                System.out.println("2) Ultime n statistiche (con timestamp) di quartiere");
                System.out.println("3) Deviazione standard e media delle ultime n statistiche prodotte dal quartiere");
                System.out.println("4) Esci dall'applicazione");
                response = Integer.parseInt(br.readLine());
            } while (response != 1 && response != 2 && response != 3 && response != 4);


            if (response == 1) {

                System.out.println("Nodi presenti nella rete: " + nodesOnNetwork(client));

            } else if (response == 2) {

                do {
                    System.out.println("Inserire n statistiche da analizzare (n > 0)");
                    n = Integer.parseInt(br.readLine());
                } while (n < 1);

                System.out.println("Lista delle ultime " + n + " statistiche:");
                // stampa le ultime n statistiche
                System.out.println(lastStats(client, n));

            } else if (response == 3) {
                do {
                    System.out.println("Inserire n statistiche da analizzare (n > 0)");
                    n = Integer.parseInt(br.readLine());
                } while (n < 1);

                System.out.println("Rispettivamente la deviazione standard e la" +
                        " media degli ultimi " + n + " valori:");
                // stampa la standard deviation e la media delle ultime n statistiche
                System.out.println(standardDevAvg(client, n));

            }

            System.out.println("\n--------------------------------------\n");

        } while (response != 4);


    }


    public static String nodesOnNetwork(Client client) {
        WebResource webResource = client
                .resource("http://localhost:1200/nodes/nodesNumber");

        ClientResponse clientResponse;

        clientResponse = webResource.accept("application/json").type("application/json")
                .get(ClientResponse.class);

        return clientResponse.getEntity(String.class);
    }

    public static String lastStats(Client client, int n) throws IOException {
        WebResource webResource = client
                .resource("http://localhost:1200/stats/lastStats/" + n);

        ClientResponse clientResponse;
        ObjectMapper mapper = new ObjectMapper();


        clientResponse = webResource.accept("application/json").type("application/json")
                .get(ClientResponse.class);

        // trasforma la risposta in stringa
        String responseToString = clientResponse.getEntity(String.class);

        // inserisce la risposta nell'oggetto stats
        Stats stats = new Stats();
        stats.setStatsList(mapper.readValue(responseToString, new TypeReference<List<Stat>>() {
        }));

        return stats.printStatsList();
    }

    public static String standardDevAvg(Client client, int n) {
        WebResource webResource = client
                .resource("http://localhost:1200/stats/standardDevAvg/" + n);

        ClientResponse clientResponse;
        ObjectMapper mapper = new ObjectMapper();

        clientResponse = webResource.accept("application/json").type("application/json")
                .get(ClientResponse.class);

        return clientResponse.getEntity(String.class);
    }


}
