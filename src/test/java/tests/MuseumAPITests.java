package tests;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static api.SpecBuilder.getRequestSpec;
import static api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class MuseumAPITests {
    ArrayList<String> picturesNumbers = new ArrayList<>();
    String pictureNumber;

    @BeforeTest
    public static void setUp() {
        RestAssured.baseURI = ConfigLoader.getInstance().getBaseUri();
    }

    @Test
    public void getCollection() {
        Response response = given(getRequestSpec())
                .param("involvedMaker", "Vincent van Gogh")
                .when().get()
                .then().spec(getResponseSpec())
                .extract().response();

        picturesNumbers = response.path("artObjects.objectNumber");
        pictureNumber = picturesNumbers.get(0);
        assertEquals(pictureNumber, "SK-A-3262");
        assertEquals(picturesNumbers.size(), 10);
    }

    @Test(dependsOnMethods="getCollection")
    public void getCollectionDetails() {
        Response response = given(getRequestSpec())
                .basePath(pictureNumber)
                .when().get()
                .then().spec(getResponseSpec())
                .extract().response();

        assertEquals(response.path("artObject.title"), "Zelfportret");
        assertEquals(response.path("artObject.dating.presentingDate"), "1887");
    }

    @Test(dependsOnMethods="getCollection")
    public void getCollectionImage() {
        Response response = given(getRequestSpec())
                .basePath(pictureNumber + "/tiles")
                .when().get();

        String responseBody = response.getBody().asString();

        ArrayList<String> actualNames = new ArrayList<>();
        List<String> expectedNames = Arrays.asList("z0", "z1", "z2", "z3", "z5", "z4");

        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("levels");

            for (int i = 0; i < jsonArray.length(); i++) {
                actualNames.add(jsonArray.getJSONObject(i).getString("name"));
                System.out.println(jsonArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(actualNames, expectedNames);
    }
}