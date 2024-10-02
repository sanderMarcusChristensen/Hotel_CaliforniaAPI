package dat;

import dat.config.AppConfig;
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

        AppConfig.startServer();

    }
}