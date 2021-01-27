package ca;

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
		// CellularAutomata c = new CellularAutomata();
		// int[][][] tmp = c.gl(0.2d, 10, 100, 1);
		// for (int x = 0; x < 3; x++) {
		// 	for (int i = 0; i < 10; i++) {
		// 		for (int j = 0; j < 10; j++) {
		// 			System.out.print(tmp[x][i][j]+ " ");
		// 		}
		// 			System.out.println();
		// 		}
		// 		System.out.println();
		// }
		SpringApplication.run(CASymulation.class, args);

		
	}
}
