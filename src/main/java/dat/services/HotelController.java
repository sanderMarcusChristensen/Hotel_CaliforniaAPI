package dat.services;

import dat.config.HibernateConfig;
import dat.daos.HotelDAO;
import dat.dto.HotelDTO;

import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class HotelController implements Controller {

    private final Logger log = LoggerFactory.getLogger(HotelController.class);
    private HotelDAO dao;

    public HotelController(HotelDAO dao){
        this.dao = dao;
    }


    @Override
    public void getAll(Context ctx) {
        try {
            Set<HotelDTO> hotels = dao.getAll();
            if (hotels.isEmpty()) {
                ctx.result("No hotels found");
                ctx.status(404);
            } else {
                ctx.status(200);
                ctx.json(hotels);
            }
        } catch (Exception e) {
            ctx.status(500).result("An error occurred while fetching hotels");
        }
    }

    // GET /hotel/{id}
    @Override
    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        HotelDTO hotelDTO = dao.getById(id);
        if (hotelDTO != null) {
            ctx.status(200);
            ctx.json(hotelDTO);
        } else {
            ctx.result("Hotel not found");
            ctx.status(404);
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);
            HotelDTO savedHotelDTO = dao.create(hotelDTO);

            if (savedHotelDTO != null) {
                ctx.status(201);
                ctx.json(savedHotelDTO);
            } else {
                ctx.status(400).result("Hotel creation failed");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace
            ctx.status(500).result("An error occurred while creating the hotel: " + e.getMessage());
        }
    }


    // PUT /hotel/{id}
    @Override
    public void update(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);
        hotelDTO.setId(id);

        HotelDTO updatedHotelDTO = dao.update(hotelDTO);
        if (updatedHotelDTO != null) {
            ctx.status(200);
            ctx.json(updatedHotelDTO);
        } else {
            ctx.result("Hotel not found or update failed");
            ctx.status(404);
        }
    }

    // DELETE /hotel/{id}
    @Override
    public void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id")); // Extract ID from the path

        HotelDTO hotelDTO = new HotelDTO(); // Create a HotelDTO for deletion
        hotelDTO.setId(id); // Set the ID of the hotel to be deleted

        dao.delete(hotelDTO);
        ctx.status(204); // No Content response if successfully deleted
    }
}
