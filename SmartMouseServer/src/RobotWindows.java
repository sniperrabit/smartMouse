
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;

import javax.imageio.ImageIO;

public class RobotWindows {
	Robot robot;

	public RobotWindows() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("Co ten robot wyprawia?!");
			e.printStackTrace();
		}
	}
	
	public boolean listen(BufferedReader bReader) throws NumberFormatException, IOException{
		String line;
        String[] y;	
		int a,b;
		while (( line = bReader.readLine()) != null) {
				//System.out.println(line);
			if(line.startsWith("Mouse")){
				line=line.replaceFirst("Mouse", "");
				y=line.split(" ");
	
	//			a=Integer.valueOf(y[0])*6;
	//			b=Integer.valueOf(y[1])*6;
				
				
				a=Integer.valueOf(y[0]);
				b=Integer.valueOf(y[1]);
				
				moveMouse(a,b);
				System.out.println(a+" "+b);	
				
			}else if(line.startsWith("CLICK1")){
				System.out.println("CLICK1");
				mouseClick(InputEvent.BUTTON1_DOWN_MASK);
				
			}else if(line.startsWith("CLICK2")){
				System.out.println("CLICK2");
				mouseClick(InputEvent.BUTTON3_DOWN_MASK);
			}else if(line.startsWith("PRESS")){
				System.out.println("PRESS");
				mousePress(InputEvent.BUTTON1_DOWN_MASK);
			}else if(line.startsWith("RELEASE")){
				System.out.println("RELEASE");
				mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}else if(line.startsWith("EXIT")){
					System.out.println("Connection cancel");
					return false;
			}else{//in case of text
				System.out.println(line);
				//objRobot.printText(line);
				type(line);
			}
		}
		 System.err.println("Connection cancel");
		 return false;
	}
	
	
	public void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

	public void type(char character) {
        switch (character) {
        case 'a': doType(VK_A); break;
        case 'b': doType(VK_B); break;
        case 'c': doType(VK_C); break;
        case 'd': doType(VK_D); break;
        case 'e': doType(VK_E); break;
        case 'f': doType(VK_F); break;
        case 'g': doType(VK_G); break;
        case 'h': doType(VK_H); break;
        case 'i': doType(VK_I); break;
        case 'j': doType(VK_J); break;
        case 'k': doType(VK_K); break;
        case 'l': doType(VK_L); break;
        case 'm': doType(VK_M); break;
        case 'n': doType(VK_N); break;
        case 'o': doType(VK_O); break;
        case 'p': doType(VK_P); break;
        case 'q': doType(VK_Q); break;
        case 'r': doType(VK_R); break;
        case 's': doType(VK_S); break;
        case 't': doType(VK_T); break;
        case 'u': doType(VK_U); break;
        case 'v': doType(VK_V); break;
        case 'w': doType(VK_W); break;
        case 'x': doType(VK_X); break;
        case 'y': doType(VK_Y); break;
        case 'z': doType(VK_Z); break;
        case 'A': doType(VK_SHIFT, VK_A); break;
        case 'B': doType(VK_SHIFT, VK_B); break;
        case 'C': doType(VK_SHIFT, VK_C); break;
        case 'D': doType(VK_SHIFT, VK_D); break;
        case 'E': doType(VK_SHIFT, VK_E); break;
        case 'F': doType(VK_SHIFT, VK_F); break;
        case 'G': doType(VK_SHIFT, VK_G); break;
        case 'H': doType(VK_SHIFT, VK_H); break;
        case 'I': doType(VK_SHIFT, VK_I); break;
        case 'J': doType(VK_SHIFT, VK_J); break;
        case 'K': doType(VK_SHIFT, VK_K); break;
        case 'L': doType(VK_SHIFT, VK_L); break;
        case 'M': doType(VK_SHIFT, VK_M); break;
        case 'N': doType(VK_SHIFT, VK_N); break;
        case 'O': doType(VK_SHIFT, VK_O); break;
        case 'P': doType(VK_SHIFT, VK_P); break;
        case 'Q': doType(VK_SHIFT, VK_Q); break;
        case 'R': doType(VK_SHIFT, VK_R); break;
        case 'S': doType(VK_SHIFT, VK_S); break;
        case 'T': doType(VK_SHIFT, VK_T); break;
        case 'U': doType(VK_SHIFT, VK_U); break;
        case 'V': doType(VK_SHIFT, VK_V); break;
        case 'W': doType(VK_SHIFT, VK_W); break;
        case 'X': doType(VK_SHIFT, VK_X); break;
        case 'Y': doType(VK_SHIFT, VK_Y); break;
        case 'Z': doType(VK_SHIFT, VK_Z); break;
        case '`': doType(VK_BACK_QUOTE); break;
        case '0': doType(VK_0); break;
        case '1': doType(VK_1); break;
        case '2': doType(VK_2); break;
        case '3': doType(VK_3); break;
        case '4': doType(VK_4); break;
        case '5': doType(VK_5); break;
        case '6': doType(VK_6); break;
        case '7': doType(VK_7); break;
        case '8': doType(VK_8); break;
        case '9': doType(VK_9); break;
        case '-': doType(VK_MINUS); break;
        case '=': doType(VK_EQUALS); break;
        case '~': doType(VK_SHIFT, VK_BACK_QUOTE); break;
        case '!': doType(VK_EXCLAMATION_MARK); break;
        case '@': doType(VK_AT); break;
        case '#': doType(VK_NUMBER_SIGN); break;
        case '$': doType(VK_DOLLAR); break;
        case '%': doType(VK_SHIFT, VK_5); break;
        case '^': doType(VK_CIRCUMFLEX); break;
        case '&': doType(VK_AMPERSAND); break;
        case '*': doType(VK_ASTERISK); break;
        case '(': doType(VK_LEFT_PARENTHESIS); break;
        case ')': doType(VK_RIGHT_PARENTHESIS); break;
        case '_': doType(VK_UNDERSCORE); break;
        case '+': doType(VK_PLUS); break;
        case '\t': doType(VK_TAB); break;
        case '\n': doType(VK_ENTER); break;
        case '[': doType(VK_OPEN_BRACKET); break;
        case ']': doType(VK_CLOSE_BRACKET); break;
        case '\\': doType(VK_BACK_SLASH); break;
        case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
        case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
        case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
        case ';': doType(VK_SEMICOLON); break;
        case ':': doType(VK_COLON); break;
        case '\'': doType(VK_QUOTE); break;
        case '"': doType(VK_QUOTEDBL); break;
        case ',': doType(VK_COMMA); break;
        case '<': doType(VK_SHIFT, VK_COMMA); break;
        case '.': doType(VK_PERIOD); break;
        case '>': doType(VK_SHIFT, VK_PERIOD); break;
        case '/': doType(VK_SLASH); break;
        case '?': doType(VK_SHIFT, VK_SLASH); break;
        case ' ': doType(VK_SPACE); break;
        default:
        	System.out.println("Cannot type character " + character);
           // throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    public void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    public void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        robot.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
    }

	
	
	
//	public void printText(String text)
//	  {
//	    byte[] bytes = text.getBytes();
//	    for (byte b : bytes)
//	    {
//	      int code = b;
//	      // keycode only handles [A-Z] (which is ASCII decimal [65-90])
//	      if (code > 96 && code < 123) code = code - 32;
//	      robot.delay(40);
//	      robot.keyPress(code);
//	      robot.keyRelease(code);
//	    }
//	  }
//	
//	public void printText(String text) {
//		int[] text = {KeyEvent.VK_H, KeyEvent.VK_E, KeyEvent.VK_L, KeyEvent.VK_L, KeyEvent.VK_O, KeyEvent.VK_SPACE,
//				KeyEvent.VK_W, KeyEvent.VK_O, KeyEvent.VK_R, KeyEvent.VK_L, KeyEvent.VK_D};
//		//pętla - wciśnięcie przycisku i przerwa na 200ms
//		
//		
//		for(int i=0; i<text.length(); i++) {
//			robot.keyPress(text[i]);
//			robot.delay(200);
//		}
//	}
 
	public void moveMouse(int arg0,int arg1){
		robot.mouseMove(arg0, arg1);
		
	}
	public void mouseClick(int e){
		robot.mousePress(e);
		robot.mouseRelease(e);
		
	}
	public void mousePress(int e){
		robot.mousePress(e);
	}
	public void mouseRelease(int e){
		robot.mouseRelease(e);
	}
		
	
//	boolean unclicked=true;
//	public void mousePress(int e){
//		if(unclicked){
//			robot.mousePress(e);
//			unclicked=false;
//		}else{
//			robot.mouseRelease(e);
//			unclicked=true;
//		}
//	}
	
//	/**
//	 * Metoda robi screenshot ekranu i zapisuje go na dysku
//	 */
//	public void screenCapture() {
//		//pobieramy rozmiar ekranu i tworzymy Rectangle
//		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//		Rectangle rectangle = new Rectangle(dimension);
//		//robimy screenshot z utworzonego obszaru
//		BufferedImage screen = robot.createScreenCapture(rectangle);
//		try {
//			ImageIO.write(screen, "jpg", new File("screenshot.jpg"));
//		} catch (IOException e) {
//			System.err.println("Błąd zapisu obrazu");
//			e.printStackTrace();
//		}
//	}

	/**
	 * Testujemy czy działa
	 */
//	public static void main(String[] args) {
//		RobotWindows test = new RobotWindows();
//		test.robot.delay(3000);
//		test.moveMouse(300, 230);
////		test.printText();
////		test.screenCapture();
//	}

}