// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/Chapter13/RockPaperScissorsGUIMain.java

import java.awt.CardLayout;

import javax.swing.*;
import rpsGame.DrawOrDieGamePanel;
import rpsGame.ShootingScene;
import rpsGame.StagePanel;
import rpsGame.TitlePanel;

public class DrawOrDieMain
{
	//총 쏘는 그림나오는 패널은 게임패널에서 접근할 필요가 있기 때문에 따로 선언해서 전달
	protected static ShootingScene shoot;
	
	public static void main( String[] args ) {
		final String imagePath = "image\\";
		

		
		JFrame frame = new JFrame( "DrawOrDie" );
		final CardLayout cards = new CardLayout();
		frame.setLayout(cards);
		
		// 틀에 판을 끼우고 실행 준비 완료
		shoot = new ShootingScene(frame.getContentPane(), cards, imagePath);
		frame.add( new TitlePanel( frame.getContentPane(), cards, imagePath ), "MAIN" );
		frame.add( new StagePanel( frame.getContentPane(), cards, imagePath), "STAGE" );
		frame.add(shoot,"SHOOT");
		frame.add( new DrawOrDieGamePanel( frame.getContentPane(), cards, imagePath, shoot ), "GAME" );
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible( true );
	}
}



