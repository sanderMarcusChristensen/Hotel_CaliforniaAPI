package dat.config;

import dat.exceptions.ApiException;
import dat.routes.Routes;
import dat.services.ExceptionController;
import dat.util.ApiProperties;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppConfig {

    private static final Routes routes = new Routes();
    private static final ExceptionController exceptionController =new ExceptionController();

    private static void configuration(JavalinConfig config) {

        config.router.contextPath = ApiProperties.API_CONTEXT;

        config.bundledPlugins.enableRouteOverview("/routes");
        config.bundledPlugins.enableDevLogging();

        config.router.apiBuilder(routes.getApiRoutes());
    }

    public static void startServer() {
        Javalin app = io.javalin.Javalin.create(AppConfig::configuration);
        app.start(ApiProperties.PORT);
        exceptionContext(app);
    }

    public static void exceptionContext(Javalin app){

        app.exception(ApiException.class,exceptionController::apiExceptionsHandler);

    }
}
