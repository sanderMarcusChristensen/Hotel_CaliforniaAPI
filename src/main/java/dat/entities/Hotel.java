package dat.entities;

import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long id;

    private String name;

    private String address;

    @OneToMany
    private Set<Room> rooms = new HashSet<>();

    // Constructor for creating Hotel from HotelDTO
    public Hotel(HotelDTO hotelDTO) {
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();

        for (RoomDTO roomDTO : hotelDTO.getRooms()) {
            Room room = new Room(roomDTO);
            room.setHotelId(this); // Set back-reference
            rooms.add(room);
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotelId(this); // Set back-reference
    }
}