package command.test;

import static org.junit.Assert.*;

import org.junit.*;

import response.Response;

import authentication.Authenticate;

import command.Command;
import command.CommandFactory;
import command.ProcessCommand;

public class CreateProcessCommandTest {

	CommandFactory cmdf;
	ProcessCommand processCommand;
	String uuid;

	String username = "splutt";
	String filename = "filename12";
	String filepath = "path/to/local/file";
	String expid = "Exp1";
	String processtype = "rawtoprofile";
	String parameters = "\"param1\"," +
						"\"param2\"," +
						"\"param3\"," +
						"\"param4\"";
	String metadata = "astringofmetadata";
	String genomeRelease = "hg38";
	String author = "yuri";

	@Before
	public void setup(){

		cmdf = new CommandFactory();



		String json = "{" +
							"\"filename\": \"" + filename + "\"," +
							"\"filepath\": \"" + filepath + "\"," +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username);
	}

	@Test
	public void shouldInitiateProcessCommand(){
		assertNotNull(processCommand);
	}
	@Test
	public void shouldSetMetadata(){
		assertEquals("astringofmetadata", processCommand.getMetadata());
	}

	@Test
	public void shouldAddFourParameters(){
		assertEquals(4,processCommand.getParameters().length);
	}

	@Test
	public void shouldSetParameters(){
		assertEquals("param1",(processCommand.getParameters())[0]);
		assertEquals("param2",(processCommand.getParameters())[1]);
		assertEquals("param3",(processCommand.getParameters())[2]);
		assertEquals("param4",(processCommand.getParameters())[3]);
	}

	@Test
	public void shouldSetGenomeRelease(){
		assertEquals(genomeRelease, processCommand.getGenomeRelease());
	}

	@Test
	public void shouldSetProcessType(){
		assertEquals(processtype,processCommand.getProcessType());
	}

	@Test
	public void shouldSetUserID(){
		assertEquals(username, processCommand.getUsername());
	}

	@Test
	public void shouldSetFilename(){
		assertEquals(filename, processCommand.getFilename());
	}

	@Test
	public void shouldSetFilepath(){
		assertEquals(filepath,processCommand.getFilepath());
	}

	@Test
	public void shouldSetExpID(){
		assertEquals(expid,processCommand.getExpID());
	}

	@Test
	public void execute(){
		Response response = processCommand.execute();
		System.out.println("Execute response: " + response.getCode());
	}

}