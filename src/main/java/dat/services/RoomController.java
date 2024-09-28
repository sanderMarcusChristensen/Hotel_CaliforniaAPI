package dat.services;

import dat.config.HibernateConfig;
import dat.daos.RoomDAO;
import dat.dto.RoomDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

public class RoomController implements Controller {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    private final RoomDAO dao = new RoomDAO(emf);



    @Override
    public void getAll(Context ctx) {
        try {
            Set<RoomDTO> roomsDTO = dao.getAll();
            if (roomsDTO.isEmpty()) {
                ctx.result("No Rooms found");
                ctx.status(400);
            } else {
                ctx.status(200);
                ctx.json(roomsDTO);
            }
        } catch (Exception e) {
            ctx.status(500).result("Error, can't fetch Rooms");
        }
    }

    @Override
    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        RoomDTO roomDTO = dao.getById(id);

        if (roomDTO != null) {
            ctx.status(200);
            ctx.json(roomDTO);
        } else {
            ctx.result("Room not found");
            ctx.status(400);
        }

    }

    @Override
    public void create(Context ctx) {
        try {
            RoomDTO dto = ctx.bodyAsClass(RoomDTO.class);
            RoomDTO savedDTO = dao.create(dto);

            if (savedDTO != null) {
                ctx.status(201); // Created
                ctx.json(savedDTO);
            } else {
                ctx.status(400).result("Room creation failed");
            }
        } catch (Exception e) {
            ctx.status(500).result("Error, couldn't create Room: " + e.getMessage());
        }
    }


    @Override
    public void update(Context ctx) {

        Long id = Long.parseLong(ctx.pathParam("id"));
        RoomDTO dto = ctx.bodyAsClass(RoomDTO.class);
        dto.setId(id);

        RoomDTO update = dao.update(dto);
        if (update != null) {
            ctx.status(200);
            ctx.json(update);

        } else {
            ctx.result("Rooms not found or update failed");
            ctx.status(404);

        }
    }

    @Override
    public void delete(Context ctx) {

        try {

            Long id = Long.parseLong(ctx.pathParam("id")); // Extract ID from the path

            RoomDTO dto = new RoomDTO(); // Create a HotelDTO for deletion
            dto.setId(id); // Set the ID of the hotel to be deleted

            dao.delete(dto);
            ctx.status(204); // No Content response if successfully deleted
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("Delete Rooms failed");
        }

    }
}
