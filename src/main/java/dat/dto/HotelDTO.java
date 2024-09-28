package dat.dto;

import dat.entities.Hotel;
import dat.entities.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor

public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private Set<RoomDTO> rooms = new HashSet<>();

    // Convert from Entity to DTO
    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();

        for (Room room : hotel.getRooms()) {
            this.rooms.add(new RoomDTO(room));
        }
    }
}

