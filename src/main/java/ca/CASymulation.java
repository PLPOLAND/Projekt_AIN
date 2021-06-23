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
		// CellularAutomata ca = new CellularAutomata();
		// int[][][] tab = ca.klAndGlv2(0.5, 10, 10, 22, 0.5, 1, 1, false, 0.4);
		// for(int x = 0; x < tab.length; x++){
		// 	for(int y = 0; y < tab[0].length; y++){
		// 		for(int z = 0; z<tab[0][0].length; z++){
		// 			System.out.print(tab[x][y][z]+" ");
		// 		}
		// 		System.out.println();
		// 	}
		// 	System.out.println("///////////////////////////////////////");
		// }
		
	}
}
