package dat;

import dat.dao.HotelDAO;
import dat.dto.HotelDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PopulatorForTest {

    private static HotelDAO hotelDao;
    private static EntityManagerFactory emf;

    public PopulatorForTest(HotelDAO hotelDao, EntityManagerFactory emf) {
        this.hotelDao = hotelDao;
        this.emf = emf;
    }

    public List<HotelDTO> populate3Hotels() {

        HotelDTO h1 = new HotelDTO(1L, "Hotel 1", "Address 1", null);
        HotelDTO h2 = new HotelDTO(2L, "Hotel 2", "Address 2", null);
        HotelDTO h3 = new HotelDTO(3L, "Hotel 3", "Address 3", null);

        h1 = hotelDao.create(h1);
        h2 = hotelDao.create(h2);
        h3 = hotelDao.create(h3);

        return List.of(h1, h2, h3);
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

