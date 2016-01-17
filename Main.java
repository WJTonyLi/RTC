/**
 * Class to handle program flow
 * @author Tony Li
 */
import javax.swing.*;

public class Main
{
	public static void main(String[] args)
	{
		new DetectAction();
	}
}//end Main class
	
//class to handle the menu and game screens
class DetectAction
{
	//menu and game screen
	Menu mn;
	GameScreen gs;
	//game over screen
	GameOverScreen gv;
	//other relevant details
	double difficulty = 5;
	boolean cheat = false;
	boolean startBlack = false;
	boolean smallWindow = false;
	int windowX = 1000, windowY = 700;
	
	/**
	* Default constructor:
	* @return - void
	* */
	public DetectAction(){
		mn = new Menu(windowX, windowY);
		mn.addDetectAction(this);
		mn.setVisible(true);
	}
		
	/**
	* quits program
	* @return - void
	* */
	public void quitRequest()
	{
		System.exit(0);
	}
	
	/**
	* starts the game after play is clicked
	* @return - void
	* */
	public void startGameRequest()
	{
		terminate(mn);
		terminate(gv);
		gs = new GameScreen(windowX, windowY);
		gs.addDetectAction(this);
		gs.setVisible(true);
	}
	
	/**
	 * go to game over screen
	 *
	 */
	public void gameOver()
	{
		terminate(mn);
		terminate(gs);
		gv = new GameOverScreen(windowX, windowY);
		gv.addDetectAction(this);
		gv.setVisible(true);
	}
	
	/**
	* returns to menu
	* @return - void
	* */
	public void returnToMenu()
	{
		terminate(gs);
		terminate(gv);
		mn = new Menu(windowX, windowY);
		mn.addDetectAction(this);
		mn.setVisible(true);
	}
	
	/**
	 * given a JFrame of some kind, checks for null and closes it
	 */
	public static void terminate(JFrame item)
	{
		if (item instanceof Menu)
			item.setVisible(false);
		else if (item != null)
			item.dispose();
	}
}//end DetectAction class