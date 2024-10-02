package dat.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor

public class RoomDTO {
    private Long id;
    private Long hotelId; // to match the ID of the hotel
    private Integer number;
    private Double price;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.hotelId = room.getHotel().getId();
        this.number = room.getNumber();
        this.price = room.getPrice();
    }

    @JsonIgnore
    public Room getRoomAsEntity() {
        return Room.builder()
                .id(id)
                .number(number)
                .price(price)
                .build();
    }

  public static Set<RoomDTO> addARoomToTheList(Set<Room> rooms){
        return rooms.stream().map(RoomDTO :: new).collect(Collectors.toSet());
  }

}