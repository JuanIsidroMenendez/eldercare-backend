package juanim.dev.eldercare;

import org.springframework.boot.SpringApplication;

public class TestEldercareApplication {

	public static void main(String[] args) {
		SpringApplication.from(EldercareApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
