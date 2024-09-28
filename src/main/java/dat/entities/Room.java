package dat.entities;

import dat.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotelId;

    private int number;

    private int price;

    // Constructor for creating Room from RoomDTO
    public Room(RoomDTO roomDTO) {
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
        //this.hotelId = hot
    }
}