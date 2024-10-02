package dat.entities;

import dat.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Column(name ="room_number")
    private Integer number;

    private Double price;

    @Builder
    public Room(Long id, Hotel hotel, Integer number, Double price) {
        this.id = id;
        this.hotel = hotel;
        this.number = number;
        this.price = price;
    }
}