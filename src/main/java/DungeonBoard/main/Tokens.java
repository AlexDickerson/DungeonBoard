package DungeonBoard.main;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import org.javatuples.Quintet;

public class Tokens {

    public Tokens(){
		tokens = new ArrayList<Token>();
    }

	public Token getToken(int tokenIndex){
		return tokens.get(tokenIndex);
	}

	public void setToken(int index, Token token){
		tokens.set(index, token);
	}

	public void setToken(int index, BufferedImage image, Integer xPos, Integer yPos, Integer width, Integer height, String name, List<String> statuses){
		tokens.set(index, new Token(image, xPos, yPos, width, height, name, statuses));
	}

	public void addToken(BufferedImage image, String name){ 
		tokens.add(new Token(image, 0, 0, image.getWidth(), image.getHeight(), name, new ArrayList<String>())); 
	}

	public void copyToken(int index){ tokens.add(tokens.get(index)); }

	public void removeToken(int index){ tokens.remove(index); }

    public int tokenSize(){ return tokens.size(); }
    
	private List<Token> tokens;

	public class Token{
		private Quintet<BufferedImage, Point, Dimension, String, List<String>> token;
	 	private Token(BufferedImage iamge, Integer xPos, Integer yPos, Integer width, Integer height, String name, List<String> status){
			token = Quintet.with(iamge, new Point(xPos, yPos), new Dimension(width, height), name, status);
		}

		private Token(Quintet<BufferedImage, Point, Dimension, String, List<String>> newToken){
			token = newToken;
		}

		public BufferedImage getImage() { return token.getValue0(); }
		public int getXPos() { return token.getValue1().x; }
		public int getYPos() { return token.getValue1().y; }
		public int getWidth() { return token.getValue2().width; }
		public int getHeight() { return token.getValue2().height; }
		public String getName() { return token.getValue3(); }
		public List<String> getStatusList() { return token.getValue4(); }

		public Token setStatusList(List<String> status) { return new Token(token.setAt4(status)); }
	}

}