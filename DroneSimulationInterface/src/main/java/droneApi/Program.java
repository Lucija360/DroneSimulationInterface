/*
package droneApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
public class Program implements WebMvcConfigurer {

    public static void main(String[] args) {
    	
    	 /**
         * Main method that starts the Spring Boot application.
         * @param args command-line arguments.
        //deleted
        SpringApplication.run(Program.class, args);
    }

    /**
     * Configures the default view controller.
     * Maps the root URL "/" to the "index" view.
     * This expects an "index.html" file in the "resources/templates" directory.
     //deleted
    //TODO: You can create corresponding HTML templates in the resources/templates folder for UI elements
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    /**
     * Registers additional view controllers.
     * Maps the "/drones" URL to the "drones" view.
     * This expects a "drones.html" file in the "resources/templates" directory.
     * @return WebMvcConfigurer instance with custom view controllers.
     //deleted
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/drones").setViewName("drones");
            }
        };
    }
}

*/

// UPDATED PROGRAM.java

package droneApi;

import droneApi.Gui.MainMenu;

import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
public class Program implements WebMvcConfigurer {

    public static void main(String[] args) {
       
    	  // Start Spring Boot application on its own thread
        new Thread(() -> SpringApplication.run(Program.class, args)).start();

        // Start Swing GUI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });

        System.out.println("Spring Boot Application and Swing GUI are running...");
    }

}

