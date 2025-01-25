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

