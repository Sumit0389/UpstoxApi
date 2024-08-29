package com.sumit.UpstoxApi.service;

import com.sun.source.tree.TryTree;
import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.Configuration;
import com.upstox.api.*;
import com.upstox.auth.OAuth;
import com.upstox.*;
import io.swagger.client.api.OptionsApi;
import io.swagger.client.api.OrderApi;
import io.swagger.client.api.PortfolioApi;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GetPositionService {

    private  String token="eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJGUDIwMzUiLCJqdGkiOiI2NmNhOWIyY2NjODdkNzQ2YjA5NWJkZTUiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzI0NTU0MDI4LCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MjQ2MjMyMDB9.yN7h2xXVhEXtsJo2sEdFEfy7FDh37q0itpas0kMsCfk";
    private final ApiClient defaultClient=Configuration.getDefaultApiClient();;



    // Constructor to initialize the service
    public GetPositionService() {

    }

    // Method to get positionPU

    public String runOptionSellingJob()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Define the task to run at 9:45 AM
        Runnable dataJob = () -> {System.out.println("Data job running at: " + LocalDateTime.now());
                                    getOptionData();
        };

        // Calculate the initial delay until 9:45 AM
        long initialDelay = calculateInitialDelay(LocalTime.of(8, 37));

        // Schedule the task to run at 9:45 AM daily
        scheduler.scheduleAtFixedRate(dataJob, initialDelay, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
  return null;
    }

    private static long calculateInitialDelay(LocalTime targetTime) {
        LocalTime now = LocalTime.now();
        if (now.isAfter(targetTime)) {
            // If the current time is after the target time, schedule for the next day
            targetTime = targetTime.plusHours(24);
          }
        return now.until(targetTime, ChronoUnit.MINUTES);
        }
    public void getOptionData()
    {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: OAUTH2
        OAuth OAUTH2 = (OAuth) defaultClient.getAuthentication("OAUTH2");
        OAUTH2.setAccessToken(token);

        OptionsApi apiInstance = new OptionsApi();
        String instrumentKey = "NSE_INDEX|Nifty 50"; // String | Instrument key for an underlying symbol
        String expiryDate = "2024-08-29"; // String | Expiry date in format: YYYY-mm-dd
       //  testing


       try {
            GetOptionChainResponse result = apiInstance.getPutCallOptionChain(instrumentKey, expiryDate);
           List <OptionStrikeData> response=result.getData();


           Double niftyPrice= response.getFirst().getUnderlyingSpotPrice();
            List<OptionStrikeData> response1= response.stream().filter(data-> Math.abs((data.getStrikePrice()-niftyPrice)) < 1000).toList();
            System.out.println("Size of List :  "+response1.size());
            System.out.println(niftyPrice);
            boolean callFound=false;
            boolean putFound=false;
            Double callPrice=0.0;
            Double putPrice=0.0;

            PlaceOrderRequest callbody= new PlaceOrderRequest();
            callbody.setQuantity(25);
            callbody.setProduct(PlaceOrderRequest.ProductEnum.D);
            callbody.setValidity(PlaceOrderRequest.ValidityEnum.DAY);
            callbody.setPrice(0.0f);
            callbody.setOrderType(PlaceOrderRequest.OrderTypeEnum.MARKET);
            callbody.setTransactionType(PlaceOrderRequest.TransactionTypeEnum.SELL);
            callbody.setDisclosedQuantity(0);
            callbody.setTriggerPrice(0.0f);
            callbody.isAmo(false);

            PlaceOrderRequest putBody= new PlaceOrderRequest();
            putBody.setQuantity(25);
            putBody.setProduct(PlaceOrderRequest.ProductEnum.D);
            putBody.setValidity(PlaceOrderRequest.ValidityEnum.DAY);
            putBody.setPrice(0.0f);
            putBody.setOrderType(PlaceOrderRequest.OrderTypeEnum.MARKET);
            putBody.setTransactionType(PlaceOrderRequest.TransactionTypeEnum.SELL);
            putBody.setDisclosedQuantity(0);
            putBody.setTriggerPrice(0.0f);
            putBody.isAmo(false);






           for(OptionStrikeData strikeData:response1)
           {
               System.out.println(" Strikrprice: "+strikeData.getStrikePrice());

              callPrice= strikeData.getCallOptions().getMarketData().getLtp();
              if(!callFound && (callPrice > 10.0 && callPrice <15))
              {
                  System.out.println(" Call block: "+strikeData.getStrikePrice());
                  System.out.println("Instrument key of call: "+strikeData.getCallOptions().getInstrumentKey());
                 callFound=true;
                 callbody.instrumentToken(strikeData.getCallOptions().getInstrumentKey());
                 continue;
              }

              putPrice= strikeData.getPutOptions().getMarketData().getLtp();
              if(!putFound &&(putPrice > 10.0 && putPrice<15))
               {
                   System.out.println(" Put block: "+strikeData.getStrikePrice());
                   System.out.println("Instrument key of put: "+strikeData.getPutOptions().getInstrumentKey());
                   putFound=true;
                   putBody.setInstrumentToken(strikeData.getPutOptions().getInstrumentKey());
                   continue;

               }
               if(callFound  && putFound)
               {
                  break;
               }
           }
           if(callFound  && putFound)
           {
               System.out.println("Call Body-----------------"+callbody.toString());
               PlaceOrderResponse callOrderResponse=placeOrder(callbody);
               callOrderResponse.getData().getOrderId();

               System.out.println("Put Body-------------------"+putBody.toString());

               PlaceOrderResponse putOrderResponse = placeOrder(putBody);


               System.out.println("Call VAlue :"+callPrice +" Put Price: "+putPrice);


           }

           else {
               System.out.println("not found");
           }


        } catch (ApiException e) {
            System.err.println("Exception when calling OptionsApi#getPutCallOptionChain");
            e.printStackTrace();
        }


    }
    public GetPositionResponse getPosition() {

        System.out.println("token: "+token);

        OAuth oAuth2 = (OAuth) defaultClient.getAuthentication("OAUTH2");
        oAuth2.setAccessToken(token);
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        PortfolioApi apiInstance = new PortfolioApi();
        String apiVersion = "v2"; // Replace with actual API version if needed

        try {
            GetPositionResponse result = apiInstance.getPositions(apiVersion);
           // System.out.println(result);
            return result;
        } catch (ApiException e) {
            System.err.println("Exception when calling PortfolioApi#getPositions");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected exception when calling PortfolioApi#getPositions");
            e.printStackTrace();
            return null;
        }
    }

    private PlaceOrderResponse placeOrder(PlaceOrderRequest body)
    {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: OAUTH2
        OAuth OAUTH2 = (OAuth) defaultClient.getAuthentication("OAUTH2");
        OAUTH2.setAccessToken(token);

        OrderApi apiInstance = new OrderApi();

        // PlaceOrderRequest |
        String apiVersion = "v2"; // String | API Version Header

        try {
            PlaceOrderResponse result = apiInstance.placeOrder(body, apiVersion);
           // System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OrderApi#placeOrder");
            e.printStackTrace();
        }
        return null;
    }
}
