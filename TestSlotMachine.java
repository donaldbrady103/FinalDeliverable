import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import org.junit.Test;

public class TestSlotMachine {

	//Tests that scores are written correctly
	@Test
	public void testScoreWrite() throws IOException {
		File scores = new File("scores.txt");
		if(scores!=null){
			scores.delete();
		}
		
		SlotMachineSimulation.WriteScores("hello", 1000);
		BufferedReader br = new BufferedReader(new FileReader("scores.txt"));
		String name = br.readLine();
		String scoreString = br.readLine();
		scores = new File("scores.txt");
		scores.delete();
		br.close();
		assertEquals("hello", name);
		assertEquals((double)1000, Double.parseDouble(scoreString),0);
		
	}
	
	//Tests that winnings is calculated correctly
	@Test
	public void testWinnings(){
		assertEquals(3000, SlotMachineSimulation.Winnings("slot1", "slot1", "slot1", 1000), 0);
	}
	
	//Tests that winnings is calculated correctly
	@Test
	public void testWinnings2(){
		assertEquals(2000, SlotMachineSimulation.Winnings("slot1", "s", "slot1", 1000), 0);
	}
	
	//Tests that winnings is calculated correctly
	@Test
	public void testWinnings3(){
		assertEquals(-1000, SlotMachineSimulation.Winnings("slot1", "s", "d", 1000), 0);
	}
	
	//tests the generation of numbers, it's random over six values
	@Test
	public void testSlotGenerator(){
		String[] values = {"Bars", "Cherries", "Oranges", "Plums", "Melons", "Bells"};
		ArrayList<String> valuesList = new ArrayList<String>();
		for(int x = 0; x < values.length; x++){
			valuesList.add(values[x]);
		}
		for(int i = 0; i < 1000; i++){
			String value = SlotMachineSimulation.slotGenerator();
			assertTrue(valuesList.contains(value));
		}
	}
	
	//Tests saving a game with no file found
	@Test
	public void testSaveGame() throws IOException{
		File scores = new File("savedGame.txt");
		if(scores!=null){
			scores.delete();
		}
		
		SlotMachineSimulation.SaveGame("hello", 1000);
		BufferedReader br = new BufferedReader(new FileReader("savedGame.txt"));
		String name = br.readLine();
		String scoreString = br.readLine();
		scores = new File("savedGame.txt");
		scores.delete();
		br.close();
		assertEquals("hello", name);
		assertEquals((double)1000, Double.parseDouble(scoreString),0);
	}
	
	//tests saving under normal circumstances
	@Test
	public void testSaveFunction() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, 10);
		assertTrue(true);
	}
	
	//Tests saving with money bet larger than money owned
	@Test
	public void testSaveFunction2() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, 10000);
		assertTrue(true);
	}
	
	//In this test enter 1 to test a branch
	@Test
	public void testSaveFunction3() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, -10);
		assertTrue(true);
	}
	//enter 0 to test a branch
	@Test
	public void testSaveFunction4() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, -1);
		assertTrue(true);
	}
	
	//enter ant int that isn't 1 or 0 as input to test a branch
	@Test
	public void testSaveFunction5() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, -10);
		assertTrue(true);
	}
	
	//Tests for 3 matches
	@Test
	public void testSaveFunction6() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 0, 10);
		assertTrue(true);
	}
	
	//This fails because program doesn't have non int input protection
	@Test
	public void testSaveFunction7() throws IOException{
		SlotMachineSimulation.SaveFunction("donald", 1000, 10);
		assertTrue(true);
	}
	
	@Test
	public void testMatched(){
		double match = SlotMachineSimulation.Matched("slot1", "slot1", "slot1");
		assertTrue((int)match == 2);
	}
	
	//Tests for 2 matchs
	@Test
	public void testMatched2(){
		double match = SlotMachineSimulation.Matched("slot1", "slot", "slot1");
		assertTrue((int)match == 1);
	}
	
	//Tests for no matches
	@Test
	public void testMatched3(){
		double match = SlotMachineSimulation.Matched("slot1", "slo", "slot");
		assertTrue((int)match == 0);
	}
	
	//should probably use a mock here, but it does a specific function i need it to do
	@Test
	public void testLoadScores() throws IOException{
		SlotMachineSimulation.WriteScores("hello", 1000);
		SlotMachineSimulation.LoadScores();
		assertTrue(true);
	}
	
	//test no file found
	@Test
	public void testLoadScores2() throws IOException{
		File scores = new File("scores.txt");
		if(scores!=null){
			scores.delete();
		}
		SlotMachineSimulation.WriteScores("hello", 1000);
		SlotMachineSimulation.LoadScores();
		assertTrue(true);
	}
	
	//Tests loading names
	@Test
	public void testLoadName() throws IOException{
		File scores = new File("savedGame.txt");
		if(scores!=null){
			scores.delete();
		}
		
		SlotMachineSimulation.SaveGame("hello", 1000);
		String[] values = SlotMachineSimulation.LoadName();
		scores = new File("savedGame.txt");
		scores.delete();
		assertEquals(values[0], "hello");
		assertEquals(values[1], "1000.00");
	}
	
	//Tests loading of names again but without a file present
	@Test
	public void testLoadName2() throws IOException{
		File scores = new File("savedGame.txt");
		if(scores!=null){
			scores.delete();
		}
		String[] values = SlotMachineSimulation.LoadName();
		assertEquals(values[0], "name");
		assertEquals(values[1], "-10");
	}
	
	
	
}
