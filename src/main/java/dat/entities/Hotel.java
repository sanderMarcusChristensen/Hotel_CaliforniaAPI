package dat.entities;

import dat.dto.HotelDTO;
import dat.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.Builder;
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

    @OneToMany(mappedBy = "hotel",orphanRemoval = true)
    private Set<Room> rooms = new HashSet<>();

    // Constructor for creating Hotel from HotelDTO
    @Builder
    public Hotel(Long id, String name, String address, Set<Room> rooms) {
        this.id = id;
        this.name = name;
        this.address = address;

        if(rooms != null) {
            this.rooms = new HashSet<>();
            for(Room room : rooms) {
                this.rooms.add(room);
            }
        } else {
            this.rooms = new HashSet<>();
        }
    }
}