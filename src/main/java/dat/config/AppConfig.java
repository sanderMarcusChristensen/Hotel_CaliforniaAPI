package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.exceptions.ApiException;
import dat.routes.Routes;
import dat.security.controller.SecurityController;
import dat.security.routes.SecurityRoutes;
import dat.security.utils.Utils;
import dat.services.ExceptionController;
import dat.util.ApiProperties;

import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {

    private static final Routes routes = new Routes();
    private static final SecurityRoutes securityRoutes = new SecurityRoutes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();

    private static final ExceptionController exceptionController =new ExceptionController();

    private static void configuration(JavalinConfig config) {

        config.router.contextPath = ApiProperties.API_CONTEXT;

        config.bundledPlugins.enableRouteOverview("/routes");
        config.bundledPlugins.enableDevLogging();

        config.router.apiBuilder(routes.getApiRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());        //idk
    }

    public static void startServer() {
        Javalin app = Javalin.create(AppConfig::configuration);

        app.beforeMatched(ctx -> { // Before matched is different from before, in that it is not called for 404 etc.
            if (ctx.routeRoles().isEmpty()) // no roles were added to the route endpoint so OK
                return;
            // 1. Get permitted roles
            Set<String> allowedRoles = ctx.routeRoles().stream().map(role -> role.toString().toUpperCase()).collect(Collectors.toSet());
            if (allowedRoles.contains("ANYONE")) {
                return;
            }
            // 2. Get user roles
            UserDTO user = ctx.attribute("user"); // the User was put in the context by the SecurityController.authenticate method (in a before filter on the route)

            // 3. Compare
            if (user == null)
                ctx.status(HttpStatus.FORBIDDEN)
                        .json(jsonMapper.createObjectNode()
                                .put("msg", "Not authorized. No username was added from the token"));

            if (!SecurityController.getInstance().authorize(user, allowedRoles)) {
                // throw new UnAuthorizedResponse(); // version 6 migration guide
                throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "Unauthorized with roles: " + user.getRoles() + ". Needed roles are: " + allowedRoles);
            }
        });

        app.start(ApiProperties.PORT);
        exceptionContext(app);
    }

    public static void exceptionContext(Javalin app){

        app.exception(ApiException.class,exceptionController::apiExceptionsHandler);

    }
}
