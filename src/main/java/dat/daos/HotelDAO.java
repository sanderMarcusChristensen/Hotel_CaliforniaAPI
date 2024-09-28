package dat.daos;

import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HotelDAO implements IDAO<HotelDTO> {

    private EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Set<HotelDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<Hotel> hotels = em.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();

            Set<HotelDTO> hotelDTOS = hotels.stream()
                    .map(HotelDTO::new)
                    .collect(Collectors.toSet());
            em.getTransaction().commit();
            return hotelDTOS;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HotelDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel hotel = em.find(Hotel.class, id);
            return new HotelDTO(hotel);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HotelDTO create(HotelDTO hotelDTO) {
        Hotel h = new Hotel(hotelDTO);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin(); // Start transaction
            em.persist(h);
            em.getTransaction().commit(); // Commit transaction
            return new HotelDTO(h);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle transaction rollback
        }
    }

    @Override
    public HotelDTO update(HotelDTO hotelDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Hotel foundHotel = em.find(Hotel.class, hotelDTO.getId());

            if (foundHotel != null) {

                if (hotelDTO.getName() != null) {
                    foundHotel.setName(hotelDTO.getName());
                }
                if (hotelDTO.getAddress() != null) {
                    foundHotel.setAddress(hotelDTO.getAddress());
                }

                if (hotelDTO.getRooms() != null) {

                    foundHotel.getRooms().clear(); // Clear existing rooms

                    for (RoomDTO roomDTO : hotelDTO.getRooms()) {
                        Room room = new Room(roomDTO); // Convert DTO to entity
                        room.setHotelId(foundHotel); // Set back reference to hotel
                        foundHotel.addRoom(room); // Use method to add room
                    }
                }

                em.getTransaction().commit();
                return new HotelDTO(foundHotel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void delete(HotelDTO hotelDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin(); // Start transaction
            Hotel hotel = em.find(Hotel.class, hotelDTO.getId());
            if (hotel != null) {
                em.remove(hotel);
            }
            em.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

