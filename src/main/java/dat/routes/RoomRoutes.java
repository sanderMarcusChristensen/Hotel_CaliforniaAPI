package dat.routes;

import dat.config.HibernateConfig;
import dat.daos.HotelDAO;
import dat.daos.RoomDAO;
import dat.services.HotelController;
import dat.services.RoomController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class RoomRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    private final RoomDAO dao = new RoomDAO(emf);
    private final RoomController roomController = new RoomController(dao);

    public EndpointGroup getRoomRoutes(){
        return () -> {

            get("/{id}",roomController :: getById);
            get("/",roomController :: getAll);
            post("/",roomController :: create);
            put("/{id}",roomController :: update);
            delete("/{id}",roomController :: delete);

        };
    }


}


