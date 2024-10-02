package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final HotelRoutes hotelRoutes = new HotelRoutes();
    private final RoomRoutes roomRoutes = new RoomRoutes();

    public EndpointGroup getApiRoutes() {

        return () -> {
            path("/hotel", hotelRoutes::getHotelRoutes);
            path("/room", roomRoutes::getRoomRoutes);

        };
    }
}
