package com.sumit.UpstoxApi.service;

import com.upstox.ApiException;
import com.upstox.api.PositionData;
import com.upstox.api.TokenResponse;
import io.swagger.client.api.LoginApi;

public class UpstoxTokenApiService {

    private static String token="";
    public static String getToken()
    {

       if(token.isEmpty()) {
           LoginApi apiInstance = new LoginApi();
           PositionData positionDataApi = new PositionData();

           String apiVersion = "v2"; // String | API Version Header
           String code = "bqg5Uk"; // String |
           String clientId = "e4ab9bd1-8f98-433b-bc00-a596ef80ab3f"; // String |
           String clientSecret = "c9rac5wrqn"; // String |
           String redirectUri = "http://localhost:8080"; // String |
           String grantType = "authorization_code"; // String |

           try {

               TokenResponse result = apiInstance.token(apiVersion, code, clientId, clientSecret, redirectUri, grantType);
               System.out.println(result);
               token = result.getAccessToken();
           } catch (ApiException e) {
               System.err.println("Exception when calling LoginApi#token");
               e.printStackTrace();
           }

       }
        return token;
    }
    // Optionally, you could add methods to interact with positionDataApi or other APIs
}
