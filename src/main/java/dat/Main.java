package dat;

import dat.config.HibernateConfig;
import dat.services.HotelController;
import dat.services.RoomController;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Main {
    public static void main(String[] args) {

        //EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
        HotelController hotelController = new HotelController();
        RoomController roomController = new RoomController();

        Javalin app = Javalin.create((config) -> {
            config.router.contextPath = "/api";
        }).start(7777);


        app.get("/hotel/{id}",hotelController :: getById);
        app.get("/hotel",hotelController :: getAll);
        app.post("/hotel",hotelController :: create);
        app.put("/hotel/{id}",hotelController :: update);
        app.delete("/hotel/{id}",hotelController :: delete);


        app.get("/room/{id}",roomController :: getById);
        app.get("/room",roomController :: getAll);
        app.post("/room",roomController :: create);
        app.put("/room/{id}",roomController :: update);
        app.delete("/room/{id}",roomController :: delete);




    }
}