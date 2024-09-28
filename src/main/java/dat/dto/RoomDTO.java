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
        this.hotelId = room.getHotelId() != null ? room.getHotelId().getId() : null;
        this.number = room.getNumber();
        this.price = room.getPrice();
    }
}
