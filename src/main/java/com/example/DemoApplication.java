package com.example;

import com.example.model.ToDo;
import com.example.repository.ToDoRepository;
import com.example.service.ToDoService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
@RequestMapping("/")
		public String hello()
		{
			return "Hello";
		}
@Autowired
private ToDoService toDoService;

@RequestMapping(value = "/todo", method = RequestMethod.GET)
public ResponseEntity<List<ToDo>> getAllToDo() {
    logger.info("Returning all the ToDo´s");
    return new ResponseEntity<List<ToDo>>(toDoService.getAllToDo(), HttpStatus.OK);
}
@Bean
    public CommandLineRunner setup(ToDoRepository toDoRepository) {
        return (args) -> {
            toDoRepository.save(new ToDo("Remove unused imports", true));
            toDoRepository.save(new ToDo("Clean the code", true));
            toDoRepository.save(new ToDo("Build the artifacts", false));
            toDoRepository.save(new ToDo("Deploy the jar file", true));
            logger.info("The sample data has been generated");
        };
    }
}
