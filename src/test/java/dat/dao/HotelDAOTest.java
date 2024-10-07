package dat.dao;

import dat.config.HibernateConfig;
import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import dat.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HotelDAOTest {

    private static EntityManagerFactory emfTest;
    private static HotelDAO dao;
    private static Set<RoomDTO> roomDTO;
    private static List<HotelDTO> hotelDTO;

    HotelDTO h1, h2, h3, h4;
    RoomDTO r1, r2, r3, r4;

    @BeforeAll
    static void beforeAll() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new HotelDAO(emfTest);
    }

    @BeforeEach
    void setUp() {
        r1 = new RoomDTO(1L, null, 22, 2000.0);
        r2 = new RoomDTO(2L, null, 24, 1800.0);
        r3 = new RoomDTO(3L, null, 26, 2500.0);
        r4 = new RoomDTO(4L, null, 18, 2300.0);

        roomDTO = new HashSet<>();
        roomDTO.add(r1);
        roomDTO.add(r2);
        roomDTO.add(r3);
        roomDTO.add(r4);

        h1 = new HotelDTO(null, "Hotel 1", "Hotel address 1", roomDTO);
        h2 = new HotelDTO(null, "Hotel 2", "Hotel address 2", roomDTO);
        h3 = new HotelDTO(null, "Hotel 3", "Hotel address 3", roomDTO);
        h4 = new HotelDTO(null, "Hotel 4", "Hotel address 4", new HashSet<>());

        hotelDTO = new ArrayList<>();
        hotelDTO.add(h1);
        hotelDTO.add(h2);
        hotelDTO.add(h3);
        hotelDTO.add(h4);

        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();

            hotelDTO.forEach(hotelDTO -> {
                Hotel hotel = hotelDTO.getAsEntity();
                em.persist(hotel);
                hotelDTO.setId(hotel.getId());
            });

            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Get a set of all the hotels in the database")
    void getAll() {
        int actualSize = hotelDTO.size();
        Set<HotelDTO> retrievedHotels = dao.getAll();
        assertEquals(actualSize, retrievedHotels.size());
    }

    @Test
    @DisplayName("Get a hotel by it's id")
    void getById() {
        Long expectedId = h1.getId();
        HotelDTO retrievedHotel = dao.getById(expectedId);
        assertEquals(expectedId, retrievedHotel.getId());
    }

    @Test
    @DisplayName("Create a hotel and persist it in the database")
    void create() {
        // Create a new Hotel
        HotelDTO newHotel = new HotelDTO(null, "New Hotel", "New Address", new HashSet<>());
        HotelDTO createdHotel = dao.create(newHotel);

        // Assert that the hotel was created and assigned an ID
        assertEquals("New Hotel", createdHotel.getName());
        assertEquals("New Address", createdHotel.getAddress());
    }

    @Test
    @DisplayName("Update a Hotel")
    void update() {
        // Update an existing hotel
        Long hotelId = h2.getId();
        HotelDTO hotelToUpdate = dao.getById(hotelId);
        hotelToUpdate.setName("Updated Hotel Name");

        dao.update(hotelToUpdate);

        // Retrieve the updated hotel
        HotelDTO updatedHotel = dao.getById(hotelId);
        assertEquals("Updated Hotel Name", updatedHotel.getName());
    }

    @Test
    @DisplayName("Delete a hotel ")
    void delete() {

        int ex = 3;

        dao.delete(h3);
        Set<HotelDTO> retrievedHotels = dao.getAll();
        assertEquals(ex, retrievedHotels.size());
    }
}
