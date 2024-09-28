package dat.dto;


import dat.entities.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RoomDTO {
    private Long id;
    private Long hotelId; // to match the ID of the hotel
    private int number;
    private int price;

    // Convert from Entity to DTO
    public RoomDTO(Room room) {
        this.id = room.getId();
        // // Take the ID from the hotel the room belongs to and set it on the room; set to null if no Hotel is linked.
        this.hotelId = room.getHotel() != null ? room.getHotel().getId() : null;
        this.number = room.getNumber();
        this.price = room.getPrice();
    }
}
