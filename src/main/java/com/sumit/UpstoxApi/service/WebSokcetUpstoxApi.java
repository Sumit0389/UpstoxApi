package com.sumit.UpstoxApi.service;

import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.Configuration;
import com.upstox.api.WebsocketAuthRedirectResponse;
import com.upstox.auth.OAuth;
import io.swagger.client.api.WebsocketApi;

public class WebSokcetUpstoxApi {

    private  String token="eyJ0eXAiOiJKV1QiLCJrZXlfaWQiOiJza192MS4wIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJGUDIwMzUiLCJqdGkiOiI2NmNmMWYwM2IwODdhYjUzYjMwMzg3OWUiLCJpc011bHRpQ2xpZW50IjpmYWxzZSwiaWF0IjoxNzI0ODQ5OTIzLCJpc3MiOiJ1ZGFwaS1nYXRld2F5LXNlcnZpY2UiLCJleHAiOjE3MjQ4ODI0MDB9.1WqKk-VzGwLTOE9uyPoOqv86wM00yAywYISLPRMDKGc";
    private final ApiClient defaultClient= Configuration.getDefaultApiClient();;

    public WebSokcetUpstoxApi(){}

    public  void callWebSocketApi()
    {
        // Configure OAuth2 access token for authorization: OAUTH2
        OAuth OAUTH2 = (OAuth) defaultClient.getAuthentication("OAUTH2");
        OAUTH2.setAccessToken(token);

        WebsocketApi apiInstance = new WebsocketApi();
        String apiVersion = "v2"; // String | API Version Header
        try {
            apiInstance.getPortfolioStreamFeed(apiVersion);
        } catch (ApiException e) {
            System.err.println("Exception when calling WebsocketApi#getPortfolioStreamFeed");
            e.printStackTrace();
        }
    }

}
