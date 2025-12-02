// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/Chapter13/RockPaperScissorsGUIMain.java

import java.awt.CardLayout;

import javax.swing.*;
import rpsGame.RockPaperScissorsPanel;
import rpsGame.ShootingScene;
import rpsGame.CardMatchingPanel;
import rpsGame.QuestionPanel;

public class RockPaperScissorsGUIMain
{
	protected static ShootingScene shoot;
	
	public static void main( String[] args ) {
		final String imagePath = "image\\";
		

		// 틀에 판을 끼우고 실행 준비 완료
		JFrame frame = new JFrame( "가위바위보" );
		final CardLayout cards = new CardLayout();
		frame.setLayout(cards);
		
		shoot = new ShootingScene(frame.getContentPane(), cards, imagePath);
		frame.add( new QuestionPanel( frame.getContentPane(), cards, imagePath ), "MAIN" );
		frame.add( new CardMatchingPanel( frame.getContentPane(), cards, imagePath), "STAGE" );
		frame.add(shoot,"SHOOT");
		frame.add( new RockPaperScissorsPanel( frame.getContentPane(), cards, imagePath, shoot ), "GAME" );
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible( true );
	}
}


