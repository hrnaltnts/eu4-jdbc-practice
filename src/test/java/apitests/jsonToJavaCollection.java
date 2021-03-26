package apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

public class jsonToJavaCollection {

    @BeforeClass
    public void beforeclass(){
        baseURI= ConfigurationReader.get("spartan_api_url");
    }

    @Test
    public void SpartanToMap(){

        Response response = given().accept(ContentType.JSON)
                .pathParam("id", 15)
                .when().get("/api/spartans/{id}");

        assertEquals(response.statusCode(),200);

        //we will convert json response to java map
        Map<String,Object> jsonDataMap = response.body().as(Map.class);
        System.out.println("jsonDataMap = " + jsonDataMap);
        //jsonDataMap = {id=15.0, name=Meta, gender=Female, phone=1.938695106E9}

        String name = (String) jsonDataMap.get("name");
        assertEquals(name,"Meta");

        BigDecimal phone = new BigDecimal(String.valueOf(jsonDataMap.get("phone")));

        System.out.println("phone = " + phone);
        //phone = 1938695106
    }

    @Test
    public void allSpartansToListOfMap(){

        Response response = given().accept(ContentType.JSON)
                .when().get("/api/spartans");

        assertEquals(response.statusCode(),200);

        //we need to de-serialize JSON response to List of Maps
        List<Map<String,Object>> allSpartanList = response.body().as(List.class);

        System.out.println(allSpartanList);
        //[{id=101.0, name=Kim, gender=Male, phone=1.231232133E9}, {id=102.0, name=Paige, gender=Female, phone=3.786741487E9}...

        //print second spartan first name
        System.out.println(allSpartanList.get(1).get("name"));
        //Paige

        //save spartan 3 in a map
        Map<String,Object> spartan3 = allSpartanList.get(2);

        System.out.println(spartan3);
        //{id=104.0, name=Ahmet, gender=Male, phone=1.231231231E9}

    }

    @Test
    public void regionToMap(){

        Response response = when().get("http://52.55.102.92:1000/ords/hr/regions");

        assertEquals(response.statusCode(),200);

        //we de-serialize JSON response to Map
        Map<String,Object> regionMap = response.body().as(Map.class);

        System.out.println(regionMap.get("count"));

        System.out.println(regionMap.get("hasMore"));

        System.out.println(regionMap.get("items"));

        List<Map<String,Object>> itemsList = (List<Map<String, Object>>) regionMap.get("items");

        //print first region name
        System.out.println(itemsList.get(0).get("region_name"));


    }
}
