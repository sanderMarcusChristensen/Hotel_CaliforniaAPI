package dat.daos;

import dat.dto.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoomDAO implements IDAO<RoomDTO> {

    private EntityManagerFactory emf;

    public RoomDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Set<RoomDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Room> rooms = em.createQuery("SELECT r FROM Room r", Room.class).getResultList();

            // Convert List<Room> to Set<RoomDTO>
            Set<RoomDTO> roomDTOS = rooms.stream()
                    .map(RoomDTO::new)
                    .collect(Collectors.toSet());

            return roomDTOS;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RoomDTO getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Room room = em.find(Room.class, id);
            return new RoomDTO(room);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RoomDTO create(RoomDTO roomDTO) {
        Room room = new Room(roomDTO);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin(); // Start transaction
            em.persist(room);
            em.getTransaction().commit(); // Commit transaction
            return new RoomDTO(room);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle transaction rollback
        }
    }

    @Override
    public RoomDTO update(RoomDTO roomDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Room foundRoom = em.find(Room.class, roomDTO.getId());
            if (foundRoom != null) {

                if (roomDTO.getNumber() > 0) { // Assuming 0 is not a valid room number
                    foundRoom.setNumber(roomDTO.getNumber());
                }
                if (roomDTO.getPrice() > 0) { // Assuming 0 is not a valid price
                    foundRoom.setPrice(roomDTO.getPrice());
                }

                if (roomDTO.getHotelId() != null) {
                    Hotel hotel = em.find(Hotel.class, roomDTO.getHotelId());
                    foundRoom.setHotelId(hotel);
                }

                em.getTransaction().commit();
                return new RoomDTO(foundRoom);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void delete(RoomDTO roomDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin(); // Start transaction
            Room room = em.find(Room.class, roomDTO.getId());
            if (room != null) {
                em.remove(room);
            }
            em.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
