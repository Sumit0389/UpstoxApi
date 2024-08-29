package com.sumit.UpstoxApi.controller;

import com.sumit.UpstoxApi.UpstoxApiApplication;
import com.sumit.UpstoxApi.service.GetPositionService;
import com.sumit.UpstoxApi.service.WebSokcetUpstoxApi;
import com.sumit.UpstoxApi.service.WebsoketApi;
import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.ApiResponse;
import com.upstox.Configuration;
import com.upstox.api.GetPositionResponse;
import com.upstox.api.PositionData;
import com.upstox.api.TokenResponse;
import com.upstox.auth.OAuth;
import io.swagger.client.api.LoginApi;
import io.swagger.client.api.PortfolioApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class SumitUpstoxApi {

    @GetMapping("/websocket")
    public void webSocket()
    {
        WebSokcetUpstoxApi api = new WebSokcetUpstoxApi();
        api.callWebSocketApi();
    }

 @GetMapping("/getOptionData")
 public String getOptionData()
 {
     GetPositionService service= new GetPositionService();
     service.getOptionData();
     return null;
 }

   @GetMapping("/getMyPosition")
    public List<PositionData> getPosition()
    {
        GetPositionService service= new GetPositionService();
        WebsoketApi api= new WebsoketApi();
        api.getPortfolioApi();
        return service.getPosition().getData();
    }

    @GetMapping("/getToken")
            public String getToken()
    {
        String token="";
        LoginApi apiInstance = new LoginApi();
        PositionData positionDataApi= new PositionData();

        String apiVersion = "v2"; // String | API Version Header
        String code = "YcQBD6"; // String |
        String clientId = "e4ab9bd1-8f98-433b-bc00-a596ef80ab3f"; // String |
        String clientSecret = "c9rac5wrqn"; // String |
        String redirectUri = "http://localhost:8080"; // String |
        String grantType = "authorization_code"; // String |

        try {

            TokenResponse result = apiInstance.token(apiVersion,code, clientId, clientSecret, redirectUri, grantType);
            System.out.println(result);
            token=result.getAccessToken();
        } catch (ApiException e) {
            System.err.println("Exception when calling LoginApi#token");
            e.printStackTrace();
        }
        return token;
    }

}
