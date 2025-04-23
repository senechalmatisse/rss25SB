package fr.univrouen.rss25SB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Rss25SbApplication {

	public static void main(String[] args) {
		//System.getProperties().put("server.port", 8100);
		SpringApplication.run(Rss25SbApplication.class, args);
	}

}
