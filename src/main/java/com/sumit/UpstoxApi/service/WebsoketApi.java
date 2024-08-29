package com.sumit.UpstoxApi.service;

import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.Configuration;
import com.upstox.api.GetPositionResponse;
import com.upstox.auth.OAuth;
import io.swagger.client.api.PortfolioApi;
import io.swagger.client.api.WebsocketApi;

public class WebsoketApi {

    private final String token=UpstoxTokenApiService.getToken();
    private final ApiClient defaultClient= Configuration.getDefaultApiClient();;


    // Constructor to initialize the service
    public WebsoketApi() {

    }

    // Method to get position
    public void getPortfolioApi() {
        /*
        // Configure OAuth2 access token for authorization: OAUTH2
        OAuth oAuth2 = (OAuth) defaultClient.getAuthentication("OAUTH2");
        oAuth2.setAccessToken(token);


        String apiVersion = "v2"; // Replace with actual API version if needed

        WebsocketApi apiInstance = new WebsocketApi();

        try {
            apiInstance.getPortfolioStreamFeed(apiVersion);
        } catch (ApiException e) {
            System.err.println("Exception when calling WebsocketApi#getPortfolioStreamFeed");
            e.printStackTrace();
        }*/
    }
}
