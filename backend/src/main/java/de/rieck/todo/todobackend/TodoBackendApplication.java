package de.rieck.todo.todobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TodoBackendApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("busy waiting 10 seconds");
        TimeUnit.SECONDS.sleep(10); // Slow down a bit to so we are able to watch the deployment process
        System.out.println("done busy waiting");
    	SpringApplication.run(TodoBackendApplication.class, args);
    }

}
