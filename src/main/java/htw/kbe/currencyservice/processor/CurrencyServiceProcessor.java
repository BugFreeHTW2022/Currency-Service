package htw.kbe.currencyservice.processor;


import htw.kbe.currencyservice.config.MessagingConfig;
import htw.kbe.currencyservice.entity.Currencies;
import htw.kbe.currencyservice.entity.CurrencyServiceRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Component
public class CurrencyServiceProcessor {


    Map<String, Float> eurTo = Stream.of(new String[][] {
            { "USD", "1" },
            { "RUB", "61" },
            { "YEN", "143.86" },
            { "CNY", "6.96" },
    }).collect(Collectors.toMap(data -> data[0], data -> Float.parseFloat(data[1])));

    Map<String, Float> usdTo = Stream.of(new String[][] {
            { "EUR", "1" },
            { "RUB", "61" },
            { "YEN", "143.97" },
            { "CNY", "6.96" },
    }).collect(Collectors.toMap(data -> data[0], data -> Float.parseFloat(data[1])));

    Map<String, Float> rubTo = Stream.of(new String[][] {
            { "EUR", "0.016" },
            { "USD", "0.016" },
            { "YEN", "2.36" },
            { "CNY", "0.11" },
    }).collect(Collectors.toMap(data -> data[0], data -> Float.parseFloat(data[1])));

    Map<String, Float> yenTo = Stream.of(new String[][] {
            { "EUR", "0.0069" },
            { "USD", "0.0069" },
            { "RUB", "0.43" },
            { "CNY", "0.048" },
    }).collect(Collectors.toMap(data -> data[0], data -> Float.parseFloat(data[1])));

    Map<String, Float> cnyTo = Stream.of(new String[][] {
            { "EUR", "0.14" },
            { "USD", "0.14" },
            { "RUB", "8.81" },
            { "YEN", "20.66" },
    }).collect(Collectors.toMap(data -> data[0], data -> Float.parseFloat(data[1])));



    @RabbitListener(queues = MessagingConfig.QUEUE)
    public double receiveMessage(CurrencyServiceRequest request){
        System.out.println(request);


        return Math.round(processRequest(request.getFrom(),request.getTo())*request.getQuantity() * 100.0) / 100.0;


    }

    public String sendRequestToExternalApi(String from,String to){
        String key = "75c0606ea32fd5bd3a6caace";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api.com/v6/"+ key +"/pair/"+from+"/"+to))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(response.statusCode() == 200) {
            System.out.println(response.body().toString().split(",")[11].replaceAll("}","").split(":")[1]);
            return response.body().toString().split(",")[11].replaceAll("}","").split(":")[1];
        } else return "ERROR";
    }


    public float processRequest(String from, String to){
        List<String> enumNames = Stream.of(Currencies.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if(enumNames.contains(from) && enumNames.contains(to) && !from.equals(to)){

            switch (from) {
                case "EUR":
                    return eurTo.get(to);

                case "USD":
                    return usdTo.get(to);

                case "RUB":
                    return rubTo.get(to);

                case "YEN":
                    return yenTo.get(to);

                case "CNY":
                    return cnyTo.get(to);
            }
        }
        return 0;
    }

}
