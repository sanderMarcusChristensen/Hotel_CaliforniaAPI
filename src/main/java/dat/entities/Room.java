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

    //// Links this room to a hotel; the hotel details are loaded only when needed.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    private int number;

    private int price;

    // Constructor for creating Room from RoomDTO
    public Room(RoomDTO roomDTO, Hotel hotel) {
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
        this.hotel = hotel;
    }
}