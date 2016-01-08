/**
 * Game where the actual game is played
 * @author Tony Li
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class GameScreen extends JFrame implements MouseListener, MouseMotionListener
{
	//variables so that we can easily change board size and dimensions
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 50;
	public static final int EDGE_SPACE = 75;
	
	static Piece[][] board;
	
	//player instances for computer and human player
	static Player comp;
	static Player human;
	
	DetectAction da;
	
	Timer tm;
	ActionListener al;
	BoardPanel boardPnl;
	
	JButton btnMenu;
	JButton btnB;
	JPanel everything;
	JLabel message;
	
	Piece selectedPiece;
	
	//variables to store the pixel and board coordinates of the mouse
	int mouseX = -9999;
	int mouseY = -9999;
	int mouseSqX;
	int mouseSqY;
	
	public GameScreen()
	{
		super("RTC");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{
			this.setIconImage(ImageIO.read(new File("Piece Images/1Knight.png")));
		}
		catch(Exception e)
		{
		}
		
		//set layout and add contents
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		Box b1 = Box.createHorizontalBox();
		boardPnl = new BoardPanel();
		boardPnl.addMouseListener(this);
		boardPnl.addMouseMotionListener(this);
		b1.add(boardPnl);
		
		Box b2 = Box.createHorizontalBox();
		btnMenu = new JButton("Return to Menu");
		btnMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				da.returnToMenu();
			}
		});
		btnB = new JButton("B");
		b2.add(btnMenu);
		b2.add(btnB);
		
		this.add(b1);
		this.add(b2);
		
		//timer for animation
		al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boardPnl.repaint();
				boardPnl.revalidate();
				//check for either player having won
				if (human.hasLost())
				{
					boardPnl.displayMessage("computer won");
					tm.stop();
				}
				else if (comp.hasLost())
				{
					boardPnl.displayMessage("player won");
					tm.stop();
				}
			}
		};
		//adds new timer
		tm = new Timer(10, al);
		tm.start();
		
		board = new Piece[BOARD_W][BOARD_H];
		
		//black pieces
		board[0][0] = 						new Piece(2, 0, 0,Piece.ROOK);
		board[BOARD_W - 1][0] = 			new Piece(2, BOARD_W - 1, 0, Piece.ROOK);
		board[1][0] = 						new Piece(2, 1, 0, Piece.KNIGHT);
		board[BOARD_W - 2][0] = 			new Piece(2, BOARD_W - 2, 0, Piece.KNIGHT);
		board[2][0] = 						new Piece(2, 2, 0,Piece.BISHOP);
		board[BOARD_W - 3][0] = 			new Piece(2, BOARD_W - 3, 0, Piece.BISHOP);
		board[3][0] = 						new Piece(2, 3, 0, Piece.QUEEN);
		board[4][0] = 						new Piece(2, 4, 0, Piece.KING);
		for(int x = 0; x < BOARD_W; x++){
			board[x][1] = 					new Piece(2, x, 1, Piece.PAWN);
		}
		
		//white pieces
		board[0][BOARD_H - 1] = 			new Piece(1, 0, BOARD_H - 1, Piece.ROOK);
		board[BOARD_W - 1][BOARD_H - 1] = 	new Piece(1, BOARD_W - 1, BOARD_H - 1, Piece.ROOK);
		board[1][BOARD_H - 1] = 			new Piece(1, 1, BOARD_H - 1, Piece.KNIGHT);
		board[BOARD_W - 2][BOARD_H - 1] = 	new Piece(1, BOARD_W - 2, BOARD_H - 1, Piece.KNIGHT);
		board[2][BOARD_H - 1] = 			new Piece(1, 2, BOARD_H - 1, Piece.BISHOP);
		board[BOARD_W - 3][BOARD_H - 1] = 	new Piece(1, BOARD_W - 3, BOARD_H - 1, Piece.BISHOP);
		board[3][BOARD_H - 1] = 			new Piece(1, 3, BOARD_H - 1, Piece.KING);
		board[4][BOARD_H - 1] = 			new Piece(1, 4, BOARD_H - 1, Piece.QUEEN);
		for(int x = 0; x < BOARD_W; x++){
			board[x][BOARD_H - 2] = 		new Piece(1, x, BOARD_H - 2, Piece.PAWN);
		}
		
		/*for(int x = 0; x < BOARD_W; x++){
			for(int y = 0; y < BOARD_H; y++){
				if(board[x][y] != null && (board[x][y].xPos != x || board[x][y].xPos != x)){
					System.out.println(x + " " + y);
				}
			}
		}*/
		
		human = new Player(board, 1);
		comp = new Player(board, 2);
		
		this.pack();
		this.updateMoves();
	}
	
	//handle mouse actions
	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
		//if a piece is not selected, select one; otherwise, move it to the space
		if (human.selected == null)
		{
			human.select(mouseSqX, mouseSqY);
		}
		else
		{
			human.move(mouseSqX, mouseSqY);
			human.select(mouseSqX, mouseSqY);
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}
	
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}
	
	/**
	 * updates all the moves (for all pieces)
	 *
	 */
	public static void updateMoves(){
		for (Piece[] row : board)
		{
			for (Piece e : row)
			{
				if (e != null)
					e.updateMoves();
			}
		}
		/*
		for(int x = 0; x < BOARD_W; x++){
			for(int y = 0; y < BOARD_H; y++){
				if(board[x][y] != null){
					board[x][y].updateMoves();
				}
			}
		}
		*/
	}
	
	//class where the actual board is drawn
	class BoardPanel extends JPanel{
		Color brown = new Color(139, 69, 19);
		//store the graphics object so we can use it in other methods
		Graphics grfx;
		
		public BoardPanel()
		{
			this.setPreferredSize(new Dimension(SQUARE_SIZE * BOARD_W + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}
		
		public void displayMessage(String message)
		{
			grfx.drawString(message, 50, 50);
			this.repaint();
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			//set the stored graphics variable
			grfx = g;
			
			//draws the side labels
			for(int x = 0; x < BOARD_H; x++)
			{
				g.drawString(-x + BOARD_H + "", EDGE_SPACE / 2, (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE);
			}
			for(int x = 0; x < BOARD_W; x++)
			{
				g.drawString((char) (x + 'A') + "", (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE, EDGE_SPACE / 2);
			}
			
			//loop through every square on the board
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					//alternates between brown and white squares
					if((x + y) % 2 == 0)
					{
						g.setColor(Color.white);
					}
					else
					{
						g.setColor(brown);
					}
					
					//draw the squares on the board
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			
			//highlight squares the selected piece can move to
			if (human.selected != null)
			{
				for (Coord e : human.selected.moves)
				{
					g.setColor(Color.yellow);
					g.fillRect(e.x * SQUARE_SIZE + EDGE_SPACE, e.y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					g.setColor(Color.black);
					g.drawRect(e.x * SQUARE_SIZE + EDGE_SPACE, e.y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			
			//draw a blue square to indicate the selected piece
			if (human.selected != null)
			{
				g.setColor(Color.blue);
				g.fillRect(human.selected.xPos * SQUARE_SIZE + EDGE_SPACE, human.selected.yPos * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
			}
			
			//draws the pieces
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					if(board[x][y] != null)
					{
						//draws the piece
						g.drawImage(board[x][y].img, x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE, null);

					}
					g.setColor(Color.red);
					g.drawString("{" + x + ", " + y + "}", x * SQUARE_SIZE + EDGE_SPACE + 20, y * SQUARE_SIZE + EDGE_SPACE + 20);
				}
			}
			for (int e = 0; e < human.captured.size(); e++)
			{
				grfx.drawImage(human.captured.get(e).img, 50 + SQUARE_SIZE / 2 * e, 80, SQUARE_SIZE, SQUARE_SIZE, null);
			}
			for (int e = 0; e < comp.captured.size(); e++)
			{
				grfx.drawImage(comp.captured.get(e).img, 50 + SQUARE_SIZE / 2 * e, 380, SQUARE_SIZE, SQUARE_SIZE, null);
			}
			
			//update mouse location
			//adds edge space and subtracts 1 to prevent bug where the mouse was outside but the green square was still drawn because it rounded negative decimals to 0
			mouseSqX = (mouseX + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			mouseSqY = (mouseY + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			
			//draws green square around the selected square
			g.setColor(Color.green);
			//checks if the mouse is inside the board
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				g.drawRect(mouseSqX * SQUARE_SIZE + EDGE_SPACE, mouseSqY * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);	
			}
		}
	}
}