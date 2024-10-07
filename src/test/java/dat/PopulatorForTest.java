package dat;

import dat.dao.HotelDAO;
import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopulatorForTest {

    private static HotelDAO hotelDao;
    private static EntityManagerFactory emf;
    private static HotelDTO h1,h2,h3,h4;
    private static RoomDTO r1,r2,r3,r4;
    private static Set<RoomDTO> roomDTO;
    private static List<HotelDTO> hotelDTO;


    public PopulatorForTest(HotelDAO hotelDao, EntityManagerFactory emf) {
        this.hotelDao = hotelDao;
        this.emf = emf;
    }

    public List<HotelDTO> populateHotels() {

        r1 = new RoomDTO(null, 1L, 22, 2000.0);
        r2 = new RoomDTO(null, 2L, 24, 1800.0);
        r3 = new RoomDTO(null, 3L, 26, 2500.0);
        r4 = new RoomDTO(null, 4L, 18, 2300.0);

        roomDTO = new HashSet<>();
        roomDTO.add(r1);
        roomDTO.add(r2);
        roomDTO.add(r3);
        roomDTO.add(r4);

        h1 = new HotelDTO(null, "Hotel 1", "Hotel address 1", null);
        h2 = new HotelDTO(null, "Hotel 2", "Hotel address 2", null);
        h3 = new HotelDTO(null, "Hotel 3", "Hotel address 3", null);
        h4 = new HotelDTO(null, "Hotel 4", "Hotel address 4", null);

        h1 = hotelDao.addHotel(h1);
        h2 = hotelDao.addHotel(h2);
        h3 = hotelDao.addHotel(h3);
        h4 = hotelDao.addHotel(h4);

        return new ArrayList<>(List.of(h1,h2,h3,h4));

    }

    public void cleanUpHotels() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
           em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

