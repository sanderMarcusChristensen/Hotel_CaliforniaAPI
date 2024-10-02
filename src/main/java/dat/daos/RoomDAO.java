package dat.daos;

import dat.dto.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;

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
        Room room = roomDTO.getRoomAsEntity();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel foundHotel = em.find(Hotel.class, roomDTO.getHotelId());
            if (foundHotel == null) {
                throw new EntityNotFoundException("Hotel with ID " + roomDTO.getHotelId() + " does not exist.");
            }

            room.setHotel(foundHotel);
            em.persist(room);
            em.getTransaction().commit();
        }
        return new RoomDTO(room);
    }

    @Override
    public RoomDTO update(RoomDTO roomDTO) {
        Room room = roomDTO.getRoomAsEntity();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Room existingRoom = em.find(Room.class, room.getId());
            if (existingRoom == null) {
                throw new EntityNotFoundException("Room not found");
            }

            if (room.getNumber() != 0) {
                existingRoom.setNumber(room.getNumber());
            }
            if (room.getPrice() != 0) {
                existingRoom.setPrice(room.getPrice());
            }
            em.getTransaction().commit();
            return new RoomDTO(existingRoom);

        } catch (RollbackException e) {
            throw new RollbackException(String.format("Unable to update room, with id : %d : %s", roomDTO.getId(), e.getMessage()));
        }
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
