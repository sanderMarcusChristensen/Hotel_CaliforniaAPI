package dat.daos;

import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;

import java.util.HashSet;
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
            if(hotel != null){
                return new HotelDTO(hotel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HotelDTO create(HotelDTO hotelDTO) {
        Hotel hotel = hotelDTO.getAsEntity();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Set<Room> roomEntities = new HashSet<>();
            for (Room room : hotel.getRooms()) {
                Room foundRoom = em.find(Room.class, room.getId()); //checking if room already exists

                if (foundRoom != null) {
                    roomEntities.add(foundRoom);
                } else {
                    em.persist(room);
                    roomEntities.add(room);
                }

                room.setHotel(hotel);
            }
            hotel.setRooms(roomEntities);
            em.persist(hotel);
            em.getTransaction().commit();

        }
        return new HotelDTO(hotel);
    }

    @Override
    public HotelDTO update(HotelDTO hoteldto) {
        Hotel hotel = hoteldto.getAsEntity();
        try (EntityManager em = emf.createEntityManager()) {

            Hotel existingHotel = em.find(Hotel.class, hotel.getId());
            if (existingHotel == null) {
                throw new EntityNotFoundException("Hotel not found");
            }
            em.getTransaction().begin();

            if (hotel.getName() != null) {
                existingHotel.setName(hotel.getName());
            }
            if (hotel.getAddress() != null) {
                existingHotel.setAddress(hotel.getAddress());
            }
            if (hotel.getRooms() != null) {
                existingHotel.getRooms().addAll(hotel.getRooms()); // add all new rooms
            }

            em.getTransaction().commit();
            return new HotelDTO(existingHotel);

        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to update hotel, with id: %d : %s", hoteldto.getId(), e.getMessage()));
        }
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

