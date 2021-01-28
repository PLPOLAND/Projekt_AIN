package ca;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import ca.algorithms.CellularAutomata;




@SpringBootApplication
@EnableScheduling
public class CASymulation extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CASymulation.class);
	}

	public static void main(String[] args) throws Exception{
		
		
		SpringApplication.run(CASymulation.class, args);

		
	}
}
