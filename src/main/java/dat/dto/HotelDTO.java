package dat.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Hotel;
import dat.entities.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

    private Long id;
    private String name;
    private String address;
    private Set<RoomDTO> rooms;

    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.rooms = hotel.getRooms().stream().map(RoomDTO::new) // converts Room to RoomDTO
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    public Hotel getAsEntity() {
        Set<Room> roomEntities;
        if (this.rooms != null) {
            roomEntities = this.rooms.stream()
                    .map(RoomDTO::getRoomAsEntity) // convert each RoomDTO to Room
                    .collect(Collectors.toSet());
        } else {
            roomEntities = new HashSet<>();
        }

        return Hotel.builder()
                .id(id)
                .name(name)
                .address(address)
                .rooms(roomEntities)
                .build();
    }

    public static Set<HotelDTO> toHotelDTOList(Set<Hotel> hotels) {
        return hotels.stream().map(HotelDTO::new).collect(Collectors.toSet());
    }
}

