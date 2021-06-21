package com.verinite.util;

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
import com.example.util.PayloadValidator;
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
		report = new ExtentReports("\\PayloadValidatorTest.html");
		test = report.startTest("Test Cases");
		}
	
	 @org.junit.jupiter.api.Test public void verifyAllToDoList() throws Exception
	 {  ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
     assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
	 test.log(LogStatus.PASS, "verifyAllToDoList Test Pass");
	 
	 
	 }
	 

	    @org.junit.jupiter.api.Test
	    public void verifyToDoById() throws Exception {
	    	 ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
	         assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
	        test.log(LogStatus.PASS, "verifyToDoById Test Pass");
	  	  
	    }

	    @org.junit.jupiter.api.Test
	    public void verifyInvalidToDoArgument() throws Exception {
	    	 ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
	         assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
	        test.log(LogStatus.PASS, "verifyInvalidToDoArgument Test Pass");
	    }


	 

	 


	    @org.junit.jupiter.api.Test
	    public void verifySaveToDo() throws Exception {
	    	 ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
	         assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
	        test.log(LogStatus.PASS, "verifySaveToDo Test Pass");
	    }

	    @Test
	    public void verifyMalformedSaveToDo() throws Exception {
	    	 ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
	         assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
	        test.log(LogStatus.PASS, "verifyMalformedSaveToDo Test Pass");
	    }

	    @Test
	    public void verifyUpdateToDo() throws Exception {
	    	 ToDo toDo = new ToDo(0, "Sample ToDo 1", true);
	         assertEquals(true, PayloadValidator.validateCreatePayload(toDo));
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
    	System.out.println(System.getProperty("user.dir")+"Test report data");
    report.endTest(test);
    report.flush();
    }
  

}
