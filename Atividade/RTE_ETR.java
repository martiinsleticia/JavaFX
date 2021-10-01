package Atividade;

import api.FX_CG_2D_API;
import api.FX_CG_2D_API.Estilo;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RTE_ETR extends Application{
	
	final int largura = 600, altura = 600;

	public static void main(String[] args) {
		launch(args);

	}

	public void start(Stage triangulo) throws Exception {
		
		new FX_CG_2D_API("Transformações Bidimensionais", triangulo, 100, largura, altura) {
			
			@Override
			public void teclaPressionada(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void teclaLiberada(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void teclaDigitada(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void movimentoDoMousePressionado(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void movimentoDoMouse(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void desenhar() {
					                
				preenchimento(Color.WHITE);
				retangulo(0, 0, larguraTela(), alturaTela(), Estilo.PREENCHIDO);
				
				preenchimento(Color.BLUE);
				
				empilhar();
					rotacionar(60);
					transladar(6, 2);
					escalar(4, 2);
					
					retangulo(70.10, 40.30, 30.40, 40.10, Estilo.PREENCHIDO);
				desempilhar();
				
				preenchimento(Color.GREEN);
				
				empilhar();
					escalar(4, 2);
					transladar(6, 2);
					rotacionar(60);
					
					retangulo(70.10, 40.30, 30.40, 40.10, Estilo.PREENCHIDO);
				desempilhar();
					                
			}
			
			
			@Override
			public void cliqueDoMouse(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void atualizar() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void acaoAoSair() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void acaoAoIniciar() {
				// TODO Auto-generated method stub
				
			}
		};
	}


}
