package comarch.client.ishift.pl;

import comarch.client.ishift.pl.gui.MainWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("comarch")
public class Application {

	public static void main(String[] args) {

		new MainWindow().showWindow();
		SpringApplication.run(Application.class, args);
	}

}
