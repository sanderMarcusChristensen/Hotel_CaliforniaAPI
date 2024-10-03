package dat.routes;

import dat.config.HibernateConfig;
import dat.daos.HotelDAO;
import dat.services.HotelController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    private final HotelDAO dao = new HotelDAO(emf);
    private final HotelController hotelController = new HotelController(dao);

    public EndpointGroup getHotelRoutes(){
        return () -> {

            get("/{id}",hotelController :: getById);
            get("/",hotelController :: getAll);
            post("/",hotelController :: create);
            put("/{id}",hotelController :: update);
            delete("/{id}",hotelController :: delete);

        };
    }


}
