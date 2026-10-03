package com.josejavi.apinasa;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApinasaApplication {

	public static void main(String[] args) {
		loadLocalEnvironment();
		SpringApplication.run(ApinasaApplication.class, args);
	}

	/** Loads a local .env while keeping real operating-system variables authoritative. */
	private static void loadLocalEnvironment() {
		Path workingDirectory = Path.of("").toAbsolutePath();
		Path envDirectory = findEnvDirectory(workingDirectory);
		Dotenv dotenv = Dotenv.configure()
			.directory(envDirectory.toString())
			.ignoreIfMissing()
			.load();

		for (DotenvEntry entry : dotenv.entries()) {
			if (System.getenv(entry.getKey()) == null
				&& System.getProperty(entry.getKey()) == null
				&& entry.getValue() != null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	/** Supports starting Maven from either the repository root or the project directory. */
	private static Path findEnvDirectory(Path workingDirectory) {
		Path[] candidates = {
			workingDirectory,
			workingDirectory.resolve("ApiNasa/apinasa"),
			workingDirectory.resolve("apinasa")
		};

		for (Path candidate : candidates) {
			if (Files.isRegularFile(candidate.resolve(".env"))) {
				return candidate;
			}
		}
		return workingDirectory;
	}

}
