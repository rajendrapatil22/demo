package com.example.util;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.DemoApplication;
import com.example.model.ToDo;
import com.example.repository.ToDoRepository;
import com.example.service.ToDoServiceImpl;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



@ContextConfiguration(classes = DemoApplication.class)
@SpringBootTest

public class PayloadValidatorTest {
	static ExtentTest test;
	static ExtentReports report;
	 private MockMvc mockMvc;

	    @Autowired
	    private WebApplicationContext wac;

	 @Mock
	    private ToDoRepository toDoRepository;

	    @InjectMocks
	    private ToDoServiceImpl toDoService;

	    @BeforeEach
	    public void setup() {
	        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	    }
		@BeforeAll
		public static void startTest()
		{
		report = new ExtentReports(System.getProperty("user.dir")+"\\PayloadValidatorTest.html");
		test = report.startTest("Test Cases");
		}
	/*
	 * @org.junit.jupiter.api.Test public void verifyAllToDoList() throws Exception
	 * { mockMvc.perform(MockMvcRequestBuilders.get("/todo").accept(MediaType.
	 * APPLICATION_JSON)) .andExpect(jsonPath("$", hasSize(4))).andDo(print());
	 * test.log(LogStatus.PASS, "verifyAllToDoList Test Pass");
	 * 
	 * 
	 * }
	 */

	    @org.junit.jupiter.api.Test
	    public void verifyToDoById() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.get("/todo/3").accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.id").exists())
	                .andExpect(jsonPath("$.text").exists())
	                .andExpect(jsonPath("$.completed").exists())
	                .andExpect(jsonPath("$.id").value(3))
	                .andExpect(jsonPath("$.text").value("Build the artifacts"))
	                .andExpect(jsonPath("$.completed").value(false))
	                .andDo(print());
	        test.log(LogStatus.PASS, "verifyToDoById Test Pass");
	  	  
	    }

	    @org.junit.jupiter.api.Test
	    public void verifyInvalidToDoArgument() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.get("/todo/f").accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.errorCode").value(400))
	                .andExpect(jsonPath("$.message").value("The request could not be understood by the server due to malformed syntax."))
	                .andDo(print());
	        test.log(LogStatus.PASS, "verifyInvalidToDoArgument Test Pass");
	    }


	 

	 


	    @org.junit.jupiter.api.Test
	    public void verifySaveToDo() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/todo/")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"text\" : \"New ToDo Sample\", \"completed\" : \"false\" }")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.id").exists())
	                .andExpect(jsonPath("$.text").exists())
	                .andExpect(jsonPath("$.completed").exists())
	                .andExpect(jsonPath("$.text").value("New ToDo Sample"))
	                .andExpect(jsonPath("$.completed").value(false))
	                .andDo(print());
	        test.log(LogStatus.PASS, "verifySaveToDo Test Pass");
	    }

	    @Test
	    public void verifyMalformedSaveToDo() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/todo/")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{ \"id\": \"8\", \"text\" : \"New ToDo Sample\", \"completed\" : \"false\" }")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.errorCode").value(404))
	                .andExpect(jsonPath("$.message").value("Payload malformed, id must not be defined"))
	                .andDo(print());
	        test.log(LogStatus.PASS, "verifyMalformedSaveToDo Test Pass");
	    }

	    @Test
	    public void verifyUpdateToDo() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.patch("/todo/")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{ \"id\": \"1\", \"text\" : \"New ToDo Text\", \"completed\" : \"false\" }")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.id").exists())
	                .andExpect(jsonPath("$.text").exists())
	                .andExpect(jsonPath("$.completed").exists())
	                .andExpect(jsonPath("$.id").value(1))
	                .andExpect(jsonPath("$.text").value("New ToDo Text"))
	                .andExpect(jsonPath("$.completed").value(false))
	                .andDo(print());
	        test.log(LogStatus.PASS, "verifyUpdateToDo Test Pass");
	    }

    @Test
    public void validatePayLoad() {
        ToDo toDo = new ToDo(1, "Sample ToDo 1", true);
        assertEquals(false, PayloadValidator.validateCreatePayload(toDo));
        test.log(LogStatus.PASS, "validatePayLoad Test Pass");
    }

    @Test
    public void validateInvalidPayLoad() {
        ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
        assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
        test.log(LogStatus.PASS, "validateInvalidPayLoad Test Pass");   
}
    @Test
    public void testGetAllToDo() {
        List<ToDo> toDoList = new ArrayList<ToDo>();
        toDoList.add(new ToDo(1, "Todo Sample 1", true));
        toDoList.add(new ToDo(2, "Todo Sample 2", true));
        toDoList.add(new ToDo(3, "Todo Sample 3", false));
        when(toDoRepository.findAll()).thenReturn(toDoList);

        List<ToDo> result = toDoService.getAllToDo();
        assertEquals(3, result.size());
        test.log(LogStatus.PASS, "testGetAllToDo Test Pass");
    }

    @Test
    public void testGetToDoById() {
        ToDo toDo = new ToDo(1, "Todo Sample 1", true);
        when(toDoRepository.findById(1L)).thenReturn(Optional.of(toDo));
        Optional<ToDo> resultOpt = toDoService.getToDoById(1);
        ToDo result = resultOpt.get();
        if(result.isCompleted()==true) {
        assertEquals(1, result.getId());
        assertEquals("Todo Sample 1", result.getText());
        assertEquals(true, result.isCompleted());
        test.log(LogStatus.PASS, "testGetToDoById Test Pass");
        }
    }

    @Test
    public void saveToDo() {
        ToDo toDo = new ToDo(8, "Todo Sample 8", true);
        when(toDoRepository.save(toDo)).thenReturn(toDo);
        ToDo result = toDoService.saveToDo(toDo);
        assertEquals(8, result.getId());
        assertEquals("Todo Sample 8", result.getText());
        assertEquals(true, result.isCompleted());
        test.log(LogStatus.PASS, "saveToDo Test Pass");
    }

    @Test
    public void removeToDo() {
        ToDo toDo = new ToDo(8, "Todo Sample 8", true);
        toDoService.removeToDo(toDo);
        verify(toDoRepository, times(1)).delete(toDo);
        
    }
    @AfterAll
    public static void endTest()
    {
    	System.out.println("Test report data");
    report.endTest(test);
    report.flush();
    }
  

}
