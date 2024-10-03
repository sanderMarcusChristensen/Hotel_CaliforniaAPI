package dat.dao;

import dat.config.HibernateConfig;
import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import dat.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HotelDAOTest {


    private static EntityManagerFactory emfTest;
    private static HotelDAO dao;
    private static Set<RoomDTO> roomDTO;
    private static List<HotelDTO> hotelDTO;

    HotelDTO h1, h2, h3, h4;
    RoomDTO r1, r2, r3, r4;


    @BeforeAll
    static void beForeAll() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new HotelDAO(emfTest);
    }

    @BeforeEach
    void setUp() {

        r1 = new RoomDTO(1L, h2.getId(), 22, 2000.0);
        r2 = new RoomDTO(2L, h2.getId(), 24, 1800.0);
        r3 = new RoomDTO(3L, h2.getId(), 26, 2500.0);
        r4 = new RoomDTO(4L, h2.getId(), 18, 2300.0);

        roomDTO = new HashSet<>();

        roomDTO.add(r1);
        roomDTO.add(r2);
        roomDTO.add(r3);
        roomDTO.add(r4);

        h1 = new HotelDTO(1L, "Hotel 1", "Hotel address 1", roomDTO);
        h2 = new HotelDTO(2L, "Hotel 2", "Hotel address 2", roomDTO);
        h3 = new HotelDTO(3L, "Hotel 3", "Hotel address 3", roomDTO);
        h4 = new HotelDTO(4L, "Hotel 3", "Hotel address 3", roomDTO);

        hotelDTO = new ArrayList<>();

        hotelDTO.add(h1);
        hotelDTO.add(h2);
        hotelDTO.add(h3);
        hotelDTO.add(h4);


        h3.setRooms(roomDTO);

        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();


            hotelDTO.forEach(hotelDTO -> {
                Hotel hotel = hotelDTO.getAsEntity();
                em.persist(hotel);

                hotel.getRooms().forEach(room -> {
                    room.setHotel(hotel);
                    em.persist(room);
                });

                hotelDTO.setId(hotel.getId());
                hotelDTO.setRooms(hotel.getRooms().stream().map(RoomDTO::new).collect(Collectors.toSet()));
            });

            em.getTransaction().commit();
        }
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {

        int actual = hotelDTO.size();
        Set<HotelDTO> expected = dao.getAll();

        assertEquals(actual, expected.size());
    }

    @Test
    void getById() {
        Long expected = h1.getId();
        HotelDTO actual = dao.getById(expected);
        Long actualId = actual.getId();

        assertEquals(expected, actualId);
    }

    @Test
    void create() {

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}