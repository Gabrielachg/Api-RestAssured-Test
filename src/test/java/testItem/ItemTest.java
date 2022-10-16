package tests;

import FactoryRequest.FactoryRequest;
import FactoryRequest.RequestInfo;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import util.ApiConfiguration;

import static org.hamcrest.Matchers.equalTo;

public class ItemTest {

    Response response;
    JSONObject body= new JSONObject();
    RequestInfo requestInfo = new RequestInfo();

    @Test
    public void VerifyCRUDItems() {

        body.put("Content","Titulo de Item");
        requestInfo.setUrl(String.format(ApiConfiguration.CREATE_ITEM));
        requestInfo.setBody(body.toString());

        response= FactoryRequest.make("post").send(requestInfo);
        response.then().body("Content",equalTo(body.get("Content"))).statusCode(200);
        int idItem=response.then().extract().path("Id");


        requestInfo.setUrl(String.format(ApiConfiguration.READ_ITEM,idItem));
        response= FactoryRequest.make("get").send(requestInfo);
        response.then()
                .body("Content", equalTo(body.get("Content"))).body("Collapsed", equalTo(false))
                .statusCode(200);

        body.put("Content","Titulo de Item Actualizado");
        body.put("Priority",1);
        requestInfo.setUrl(String.format(ApiConfiguration.UPDATE_ITEM,idItem));
        requestInfo.setBody(body.toString());

        response= FactoryRequest.make("put").send(requestInfo);
        response.then().body("Content",equalTo(body.get("Content"))).body("Priority",equalTo(body.get("Priority"))).statusCode(200);

        requestInfo.setUrl(String.format(ApiConfiguration.DELETE_ITEM,idItem));
        response=FactoryRequest.make("delete").send(requestInfo);
        response.then().body("Deleted", equalTo(true)).body("Id",equalTo(idItem)).statusCode(200);


    }
}
