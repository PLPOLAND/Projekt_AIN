package ca;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.*;
import java.awt.GraphicsEnvironment;
import java.net.URISyntaxException;

import ca.algorithms.CellularAutomata;
import ca.statistics.Stats;




@SpringBootApplication
@EnableScheduling
public class CASymulation extends SpringBootServletInitializer {
	final static boolean DevelopmentMode = false;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CASymulation.class);
	}

	public static void main(String[] args) throws Exception{
		if (DevelopmentMode) {
			SpringApplication.run(CASymulation.class, args);
		}
		else{
			Console console = System.console();
			if (console == null && !GraphicsEnvironment.isHeadless()) {
				String filename = CASymulation.class.getProtectionDomain().getCodeSource().getLocation().toString()
						.substring(6);
				Runtime.getRuntime().exec(
						new String[] { "cmd", "/c", "start", "cmd", "/k", "java -jar \"" + "KLGLPaldyna-v11.jar" + "\"" });
			} else {
				SpringApplication.run(CASymulation.class, args);
			}
		}
	}
}
