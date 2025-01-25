package droneApi.Contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DocumentationController {

    /**
     * Endpoint to serve Redoc API documentation.
     * Maps to GET requests at "/redoc".
     * @return a RedirectView pointing to the Redoc HTML page.
     */
    @GetMapping("/redoc")
    public RedirectView serveRedoc() {
        return new RedirectView("/redoc.html");
    }
}
