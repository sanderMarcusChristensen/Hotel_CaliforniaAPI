package dat.daos;

import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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
        Hotel h = new Hotel(hotelDTO);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin(); // Start transaction
            em.persist(h);

            if(h.getRooms() != null){
                for(Room room: h.getRooms()){
                    room.setHotel(h);
                    em.persist(room);
                }
            }
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

            // Find the existing hotel
            Hotel foundHotel = em.find(Hotel.class, hotelDTO.getId());

            if (foundHotel != null) {
                // Update the basic fields
                if (hotelDTO.getName() != null) {
                    foundHotel.setName(hotelDTO.getName());
                }
                if (hotelDTO.getAddress() != null) {
                    foundHotel.setAddress(hotelDTO.getAddress());
                }

                // Update rooms
                if (hotelDTO.getRooms() != null) {
                    Set<Room> existingRooms = foundHotel.getRooms();

                    // Use a helper Set to manage room updates and avoid orphaned entries
                    Set<Room> updatedRooms = new HashSet<>();

                    for (RoomDTO roomDTO : hotelDTO.getRooms()) {
                        // Check if the room already exists by its ID
                        Room existingRoom = existingRooms.stream()
                                .filter(r -> r.getId().equals(roomDTO.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingRoom != null) {
                            // Update existing room
                            existingRoom.setNumber(roomDTO.getNumber());
                            existingRoom.setPrice(roomDTO.getPrice());
                            updatedRooms.add(existingRoom);  // Keep track of updated rooms
                        } else {
                            // Add new room if it doesn't exist
                            Room newRoom = new Room(roomDTO, foundHotel);  // Create Room from RoomDTO
                            updatedRooms.add(newRoom);
                        }
                    }

                    // Clear and update the hotel’s rooms to avoid orphans
                    foundHotel.getRooms().clear();
                    foundHotel.getRooms().addAll(updatedRooms);
                }

                // Merge the updated hotel back into the persistence context
                em.merge(foundHotel);

                em.getTransaction().commit();
                return new HotelDTO(foundHotel);  // Return the updated HotelDTO
            } else {
                em.getTransaction().rollback();  // Rollback if hotel not found
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

