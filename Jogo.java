package Jogo;

import java.net.URL;
import java.util.LinkedList;

import api.FX_CG_2D_API;
import api.FX_CG_2D_API.EfeitosSonoros;
import api.FX_CG_2D_API.Estilo;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Jogo extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	public void start(Stage cena) throws Exception {
		//EfeitosSonoros.carregarSom("music", getClass().getResource("musica.mp3"));
		final int largura = 600, altura = 400, fps = 100;
		final String nomeJogo = "Harry Potter!";
		new FX_CG_2D_API("A Caminho de Hogwarts!", cena, 100, 600, 400) {
						
			
			//controles de movimento
			boolean colidir = false;
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			
			//personagem
			double x = 30;
			double y = 30;
			
			//colisores
			LinkedList<Rectangle2D> colisores;
			Rectangle2D personagem;
			Rectangle2D linhaInicio1;
			Rectangle2D linhaInicio2;
			Rectangle2D linhaVertical1;
			Rectangle2D linhaVertical2;
			Rectangle2D linhaVertical3;
			Rectangle2D linhaVertical4;
			Rectangle2D linhaVertical5;
			Rectangle2D linhaVertical6;
			Rectangle2D linhaHorizontal1;
			Rectangle2D linhaHorizontal2;
			Rectangle2D linhaHorizontal3;
			Rectangle2D linhaHorizontal4;
			Rectangle2D linhaHorizontal5;
			Rectangle2D linhaHorizontal6;

			
//			Image harry = new Image(getClass().getResourceAsStream("hp.png"));
//			int largHarry = 20;
//        	int altHarry = 40;
//		
//			Image aragogue = new Image(getClass().getResourceAsStream("aragogue.jpg"));
//			int largAragogue = 600;
//			int altAragogue = 400;
//
//			Image castle = new Image(getClass().getResourceAsStream("castelo.png"));
//			int largCastle = 20;
//			int altCastle = 20;
			
			@Override
			public void teclaPressionada(KeyEvent e) {
				if(e.getCode() == KeyCode.UP) {
					up = true;
				}
				
				if(e.getCode() == KeyCode.DOWN) {
					down = true;
				} 
				
				if(e.getCode() == KeyCode.LEFT) {
					left = true;
				}
				
				if(e.getCode() == KeyCode.RIGHT) {
					right = true;
				}				
				
			}
			
			@Override
			public void teclaLiberada(KeyEvent e) {
				if(e.getCode() == KeyCode.UP) {
					up = false; 
				}
				
				if(e.getCode() == KeyCode.DOWN) {
					down = false;
				} 
				
				if(e.getCode() == KeyCode.LEFT) {
					left = false;
				}
				
				if(e.getCode() == KeyCode.RIGHT) {
					right = false;
				}	
				
			}
			
			@Override
			public void teclaDigitada(KeyEvent e) {
				
			}
			
			@Override
			public void movimentoDoMousePressionado(MouseEvent e) {
				
			}
			
			@Override
			public void movimentoDoMouse(MouseEvent e) {
				
			}
			
			@Override
			public void desenhar() {
				preenchimento(Color.LIGHTGRAY);
				retangulo(0, 0, larguraTela(), alturaTela(), Estilo.PREENCHIDO);
					
				//Rectangle2D castelo = new Rectangle2D(530, 320, largCastle, altCastle);
							
				preenchimento(Color.BLACK);
				Rectangle2D final1 = new Rectangle2D(530, 320, 50, 50);
				retangulo(final1, Estilo.PREENCHIDO);
				
				preenchimento(Color.DIMGRAY);
				Rectangle2D final2 = new Rectangle2D(545, 340, 20, 30);
				retangulo(final2, Estilo.PREENCHIDO);
								
				triangulo(530.0, 320.0, 580.0, 320.0, 555.5, 290.0, Estilo.PREENCHIDO);
								
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaInicio1 = new Rectangle2D(70, 10, 120, 2);
				retangulo(linhaInicio1, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaInicio2 = new Rectangle2D(70, 60, 70, 2);
				retangulo(linhaInicio2, Estilo.PREENCHIDO);
 				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaVertical1 = new Rectangle2D(190, 10, 2, 100);
				retangulo(linhaVertical1, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaVertical2 = new Rectangle2D(140, 60, 2, 100);
				retangulo(linhaVertical2, Estilo.PREENCHIDO);
				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaHorizontal1 = new Rectangle2D(190, 110, 120, 2);
				retangulo(linhaHorizontal1, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaHorizontal2 = new Rectangle2D(140, 160, 120, 2);
				retangulo(linhaHorizontal2, Estilo.PREENCHIDO);
				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaVertical3 = new Rectangle2D(310, 110, 2, 100);
				retangulo(linhaVertical3, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaVertical4 = new Rectangle2D(260, 160, 2, 100);
				retangulo(linhaVertical4, Estilo.PREENCHIDO);
				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaHorizontal3 = new Rectangle2D(310, 210, 150, 2);
				retangulo(linhaHorizontal3, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaHorizontal4 = new Rectangle2D(260, 260, 150, 2);
				retangulo(linhaHorizontal4, Estilo.PREENCHIDO);
				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaVertical5 = new Rectangle2D(460, 210, 2, 110);
				retangulo(linhaVertical5, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaVertical6 = new Rectangle2D(410, 260, 2, 110);
				retangulo(linhaVertical6, Estilo.PREENCHIDO);
				
				preenchimento(Color.DARKRED);
				//Rectangle2D linhaHorizontal5 = new Rectangle2D(460, 320, 60, 2);
				retangulo(linhaHorizontal5, Estilo.PREENCHIDO);
				
				//Rectangle2D linhaHorizontal6 = new Rectangle2D(410, 370, 110, 2);
				retangulo(linhaHorizontal6, Estilo.PREENCHIDO);
				
				
				
				//tratar colisão
				for (Rectangle2D colisor : colisores) {
					colidir = colisao(personagem, colisor);
					if(!colidir) {
						//castelo = new Rectangle2D(530, 520, largCastle, altCastle);
						desenhaPersonagem();
						
					} else {
						y = 30;
						x = 30;
						//imagem(aragogue, largAragogue, altAragogue);
						//texto("COLIDIU", 200, 300, 50);
					}
					acaoAoIniciar();
				}
				
				
			}
			
			public void desenhaPersonagem() {
				personagem = new Rectangle2D(x, y, 15, 15);
				//imagem(harry, x, y); //desenhando o personagem
				empilhar();
				preenchimento(Color.GOLDENROD);
				contorno(Color.MAROON);
				retangulo(personagem, Estilo.PREENCHIDO);
				retangulo(personagem, Estilo.LINHAS);
				desempilhar();
			}

			
			@Override
			public void cliqueDoMouse(MouseEvent e) {
				
			}
			
			@Override
			public void atualizar() {
				//fazer a img não sair da tela
				if (x > largura || x < 0) {
                    x = 30; //30 é a posição inicial do personagem
                }
                
                if (y > altura || y < 0) {
                    y = 30; //30 é a posição inicial do personagem
                }
				
                
				if(up && !colidir) {
					y--;
				}
				
				if(down && !colidir) {
					y++;
				}

				if(left && !colidir) {
					x--;
				}
				
				if(right && !colidir) {
					x++;
				}
				
			}
			
			@Override
			public void acaoAoSair() {
				
			}
			
			@Override
			public void acaoAoIniciar() {
				personagem = new Rectangle2D(x, y, 15, 15);
				colisores = new LinkedList<Rectangle2D>();
				
					
					linhaInicio1 = new Rectangle2D(70, 10, 120, 2);
					linhaInicio2 = new Rectangle2D(70, 60, 70, 2);
					linhaVertical1 = new Rectangle2D(190, 10, 2, 100);
					linhaVertical2 = new Rectangle2D(140, 60, 2, 100);
					linhaHorizontal1 = new Rectangle2D(190, 110, 120, 2);
					linhaHorizontal2 = new Rectangle2D(140, 160, 120, 2);
					linhaVertical3 = new Rectangle2D(310, 110, 2, 100);
					linhaVertical4 = new Rectangle2D(260, 160, 2, 100);
					linhaHorizontal3 = new Rectangle2D(310, 210, 150, 2);
					linhaHorizontal4 = new Rectangle2D(260, 260, 150, 2);
					linhaVertical5 = new Rectangle2D(460, 210, 2, 110);
					linhaVertical6 = new Rectangle2D(410, 260, 2, 110);
					linhaHorizontal5 = new Rectangle2D(460, 320, 60, 2);			
					linhaHorizontal6 = new Rectangle2D(410, 370, 110, 2);			
					
					colisores.add(linhaInicio1);
					colisores.add(linhaInicio2);
					colisores.add(linhaVertical1);
					colisores.add(linhaVertical2);
					colisores.add(linhaHorizontal1);
					colisores.add(linhaHorizontal2);
					colisores.add(linhaVertical3);
					colisores.add(linhaVertical4);
					colisores.add(linhaHorizontal3);
					colisores.add(linhaHorizontal4);
					colisores.add(linhaVertical5);
					colisores.add(linhaVertical6);
					colisores.add(linhaHorizontal5);
					colisores.add(linhaHorizontal6);
					
					//EfeitosSonoros.tocarSom("music", true, false);
				
			}
		};
	}


}


