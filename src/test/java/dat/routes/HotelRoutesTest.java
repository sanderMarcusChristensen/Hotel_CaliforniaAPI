package dat.routes;

import dat.PopulatorForTest;
import dat.config.AppConfig;
import dat.config.HibernateConfig;
import dat.dao.HotelDAO;
import dat.dto.HotelDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelRoutesTest {

    //private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static String BASE_URL = "http://localhost:7777/api/hotel";
    private static HotelDAO hotelDao = new HotelDAO(emf);
    private static PopulatorForTest populator = new PopulatorForTest(hotelDao, emf);
    //private static EntityManager em;

    private static HotelDTO h1, h2, h3,h4;
    private static List<HotelDTO> hotels;

    @BeforeAll
    void init() {
        HibernateConfig.getEntityManagerFactoryForTest(); // Set the test environment
        AppConfig.startServer(); // Start the server using AppConfig
    }

    @BeforeEach
    void setUp() {
        hotels = populator.populateHotels(); // Populate hotels before each test
        h1 = hotels.get(0);
        h2 = hotels.get(1);
        h3 = hotels.get(2);
        h4 = hotels.get(3);
    }

    @AfterEach
    void tearDown() {
        populator.cleanUpHotels(); // Clean up hotels after each test

    }

    @AfterAll
    void closeDown() {

    }

    @Test
    void testGetHotel() {
        HotelDTO hotel =
                given()
                        .when()
                        .get(BASE_URL + "/" + h1.getId()) // Fetch hotel by ID
                        .then()
                        .log().all()
                        .statusCode(200) // Expecting 200 OK
                        .extract()
                        .as(HotelDTO.class); // Extract response as HotelDTO

        assertThat(hotel, equalTo(h1)); // Assert fetched hotel matches h1
    }
    @Test
    void testGetAlleTheHotels() {
        Set<HotelDTO> hotelsInDb = hotelDao.getAll();
        assertEquals(4, hotelsInDb.size());

        HotelDTO[] hotelsArray =
                given()
                        .when()
                        .get(BASE_URL) // Fetch all hotels
                        .then()
                        .log().all()
                        .statusCode(200) // Expecting 200 OK
                        .extract()
                        .as(HotelDTO[].class); // Extract response as HotelDTO array

        assertThat(hotelsArray, arrayContainingInAnyOrder(h1, h2, h3, h4)); // Assert that the fetched hotels are the same
    }
    @Test
    void testCreateHotel() {
        HotelDTO newHotel = new HotelDTO(null, "Hotel D", "Address D", null); // New hotel data

        HotelDTO createdHotel =
                given()
                        .contentType("application/json")
                        .body(newHotel) // Send new hotel data in request body
                        .when()
                        .post(BASE_URL) // POST to create a new hotel
                        .then()
                        .log().all()
                        .statusCode(201) // Expecting 201 Created
                        .extract()
                        .as(HotelDTO.class); // Extract response as HotelDTO

        assertThat(createdHotel.getId(), notNullValue()); // Ensure that the new hotel has a non-null ID
        assertThat(createdHotel.getName(), equalTo("Hotel D")); // Assert hotel name
    }

    @Test
    void testUpdateHotel() {
        h1.setName("Updated Hotel 1"); // Modify hotel data

        HotelDTO updatedHotel =
                given()
                        .contentType("application/json")
                        .body(h1) // Send updated hotel data in request body
                        .when()
                        .put(BASE_URL + "/" + h1.getId()) // PUT to update hotel
                        .then()
                        .log().all()
                        .statusCode(200) // Expecting 200 OK
                        .extract()
                        .as(HotelDTO.class); // Extract response as HotelDTO

        assertThat(updatedHotel.getName(), equalTo("Updated Hotel 1")); // Assert updated hotel name
    }

    @Test
    void testDeleteHotel() {
        given()
                .when()
                .delete(BASE_URL + "/" + h1.getId()) // DELETE hotel by ID
                .then()
                .log().all()
                .statusCode(204); // Expecting 204 No Content

        // Verify that the hotel has been deleted
        given()
                .when()
                .get(BASE_URL + "/" + h1.getId()) // Attempt to fetch deleted hotel
                .then()
                .log().all()
                .statusCode(404); // Expecting 404 Not Found
    }
}
