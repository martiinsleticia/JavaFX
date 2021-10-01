package api;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class FX_CG_2D_API{

    private int fps;
    private Timeline loop;		
    protected GraphicsContext gc;
    private Canvas canvas;    
    private Dimension tamanhoTela;
    private int altura, largura;
    private int alturaPadrao, larguraPadrao;
    private Stage cena;
    private String nome;
    
    public FX_CG_2D_API(String n, Stage s, int fps, int w, int h) {
    	this.nome = n;
    	this.tamanhoTela = Toolkit.getDefaultToolkit().getScreenSize();
    	this.cena = s;
    	this.largura = w;
    	this.altura = h;
    	this.larguraPadrao = w;
    	this.alturaPadrao = h;
        this.fps = fps;
        this.loop = criarloop();
        this.canvas = new Canvas(this.largura, this.altura);        
        this.canvas.setFocusTraversable(true);
        this.gc = canvas.getGraphicsContext2D();
        
        acaoAoIniciar();
        
        // Callback para reajustar quando a tela sai do modo tela cheia...
        this.cena.fullScreenProperty().addListener(new ChangeListener<Boolean>() {   		 
 	        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
 	            if(newValue != null && !newValue.booleanValue()) {
 	            	largura = larguraPadrao;
 	           	    altura =  alturaPadrao;    		 
 	           		cena.setHeight(altura+27);
	           	    cena.setWidth(largura+5);
	           	    cena.setX(tamanhoTela.getWidth()/2 - larguraPadrao/2);
	           	    cena.setY(tamanhoTela.getHeight()/2 - alturaPadrao/2);	           	    
 	           	    canvas.setHeight(altura);
 	           	    canvas.setWidth(largura); 	           	     	           	    
 	            }
 	        }
 	    });

        // Defini��o de callbacks para mouse e teclado. Os m�todos s�o abstratos para a aplica��o do jogo implementar.
        this.gc.getCanvas().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){cliqueDoMouse(e);}
        });
        
        this.gc.getCanvas().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){movimentoDoMouse(e);}
        });
        
        this.gc.getCanvas().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){movimentoDoMousePressionado(e);}
        });
        
        this.gc.getCanvas().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaPressionada(e);}
        });
        
        this.gc.getCanvas().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaLiberada(e);}
        });
        
        this.gc.getCanvas().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){teclaDigitada(e);}
        });
        
        Platform.setImplicitExit(true);
        this.cena.setOnCloseRequest((ae) -> {
        	acaoAoSair();
            Platform.exit();
            System.exit(0);
        });
        
        this.iniciar();
    }
    
    /** M�todo para retorno da altura da tela.*/
    public int alturaTela() {
    	return this.altura;
    }
    
    /** M�todo para retorno da largura da tela.*/
    public int larguraTela() {
    	return this.largura;
    }
    
    /** M�todo para desenho de pontos2D nas coordenadas X e Y.*/     
    public void ponto(double x, double y){
    	ponto(x, y, this.gc.getLineWidth());
    }
    
    /** M�todo para desenho de pontos2D nas coordenadas X e Y com tamanho TAM.*/
    public void ponto(double x, double y, double tam){
    	double largura = this.gc.getLineWidth();
    	Color contorno = (Color) this.gc.getStroke();
    	Color preenchimento = (Color) this.gc.getFill();    	
    	this.gc.setFill(contorno);
    	this.gc.setLineWidth(tam);
    	circulo(x, y, 2, 2, Estilo.LINHAS);
    	circulo(x, y, 2, 2, Estilo.PREENCHIDO);
    	this.gc.setFill(preenchimento);
    	this.gc.setLineWidth(largura);
    }
    
    
    /** M�todo para desenho de c�rculos s�lidos ou apenas com a linha de contorno.
     * O c�rculo � desenhado nas coordenadas [x,y], com largura l e altura a. Se 
     * o par�metro estilo for Estilo.PREENCHIDO, ele desenhado s�lido, caso contr�rio, apenas
     * � desenhado o contorno da forma.*/
    public void circulo(double x, double y, double l, double a, Estilo estilo){
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillOval(x, y, l, a);
    	}else{
    		this.gc.strokeOval(x, y, l, a);
    	}
    }
    
    /** M�todo para desenho de ret�ngulos s�lidos ou apenas com a linha de contorno ou apenas com os v�rtices.
     * O ret�ngulo � desenhado nas coordenadas [x,y], com largura l e altura a. Se o par�metro estilo for 
     * Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/     
    public void retangulo(double x, double y, double l, double a, Estilo estilo){    	    	        
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillRect(x, y, l, a);
    	}else{
    		if(estilo == Estilo.LINHAS){
    			this.gc.strokeRect(x, y, l, a);
    		}else{
    			ponto(x, y);
    	        ponto(x+l-2, y);
    	        ponto(x, y+a-2);
    	        ponto(x+l-2, y+a-2);
    		}
    	}
    }
    
    /** M�todo para desenho de tri�ngulos s�lidos ou apenas com a linha de contorno ou apenas com os v�rtices.
     * O tri�ngulo � desenhado nas coordenadas dos v�rtices [x0,y0], [x1,y1] e [x2,y2]. Se o par�metro estilo for 
     * Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/
    public void triangulo(double x0, double y0, double x1, double y1, double x2, double y2, Estilo estilo){
    	double x[] = {x0, x1, x2};
    	double y[] = {y0, y1, y2};
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillPolygon(x, y, 3);
    	}else {
    		if(estilo == Estilo.LINHAS){
    			this.gc.strokePolygon(x, y, 3);
    		}else {
    			ponto(x0, y0);
    			ponto(x1, y1);
    			ponto(x2, y2);
    		}
    	}
    }
    
    /** M�todo para desenho de paralelogramos s�lidos ou apenas com a linha de contorno ou apenas com os v�rtices.
     * O paralelogramo � desenhado nas coordenadas dos v�rtices [x0,y0], [x1,y1], [x2,y2] e [x3, y3]. Se o par�metro estilo for 
     * Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/
    public void paralelogramo(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3, Estilo estilo){
    	double x[] = {x0, x1, x2, x3};
    	double y[] = {y0, y1, y2, y3};    	
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillPolygon(x, y, 4);
    	}else {
    		if(estilo == Estilo.LINHAS){
    			this.gc.strokePolygon(x, y, 4);
    		}else {
    			ponto(x0, y0);
    			ponto(x1, y1);
    			ponto(x2, y2);
    			ponto(x3, y3);
    		}
    	}
    }
  
    /** M�todo para desenho de poligonos fechados s�lidos ou apenas com a linha de contorno ou apenas com os v�rtices.
     * O poligono � desenhado nas coordenadas dos v�rtices vetX[] e vetY[], onde o primeiro vetor tem as coordenadas do eixo X e
     * o segundo vetor tem as coordenadas do eixo Y. Os dois vetores devem conter o mesmo tamanho. Se o par�metro estilo for 
     * Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas de contorno da forma, e se for 
     * Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/
    public void poligono(double vetX[], double vetY[], Estilo estilo){    	    	
    	if(estilo == Estilo.PREENCHIDO){
    		this.gc.fillPolygon(vetX, vetY, vetX.length);
    	}else {
    		if(estilo == Estilo.LINHAS){
    			this.gc.strokePolygon(vetX, vetY, vetX.length);
    		}else {
    			for(int i=0; i<vetX.length; i++) {
    				ponto(vetX[i], vetY[i]);
    			}
    		}
    	}
    }
    
    /** M�todo para desenho de poligonos fechados s�lidos ou apenas com a linha de contorno ou apenas com os v�rtices.
     * O poligono � desenhado nas coordenadas dos v�rtices vetX[] e vetY[], onde o primeiro vetor tem as coordenadas do eixo X e
     * o segundo vetor tem as coordenadas do eixo Y. Os dois vetores devem conter o mesmo tamanho. Se o par�metro estilo for 
     * Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas de contorno da forma, e se for 
     * Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/
    public void poligono(Polygon p, Estilo estilo){
    	double vetX[] = new double[p.getPoints().size()/2];
    	double vetY[] = new double[p.getPoints().size()/2];
    	ObservableList<Double> pontos = p.getPoints();    	
    	int xpoly=0;
    	int ypoly=0;
    	for(int i=0; i<pontos.size(); i++){
    		vetX[xpoly] = pontos.get(i);
    		i++;
    		vetY[ypoly] = pontos.get(i);
    		xpoly++;
    		ypoly++;
    	}    	
    	poligono(vetX, vetY, estilo);
    }
           
    
    /** M�todo para desenho de ret�ngulos s�lidos ou apenas com a linha de contorno.
     * Esse m�todo recebe um Rectangle2D para ser desenhado. Ideal para o uso em tratamento de colis�es.
     * Se o par�metro estilo for Estilo.PREENCHIDO, ele desenhado s�lido, se for Estilo.LINHAS ser�o desenhadas as linhas 
     * de contorno da forma, e se for Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma.*/
    public void retangulo(Rectangle2D retangulo, Estilo estilo){
    	double x = retangulo.getMinX();
    	double y = retangulo.getMinY();
    	double l = retangulo.getWidth();
    	double a = retangulo.getHeight();
    	retangulo(x, y, l, a, estilo);
    }
    
    /** M�todo para mudar a cor de preenchimento de um objeto para a Color cor.*/
    public void preenchimento(Color cor){
    	this.gc.setFill(cor);
    }
    
    /** M�todo para mudar a cor do contorno de um objeto para a Color cor.*/
    public void contorno(Color cor){
    	this.gc.setStroke(cor);
    }
    
    /** M�todo para mudar a cor e expessura do contorno de um objeto.*/
    public void contorno(double expessura, Color cor){
    	this.gc.setStroke(cor);
    	this.gc.setLineWidth(expessura);
    }
    
    /** M�todo para mudar a expessura do contorno de um objeto.*/
    public void contorno(double expessura){
    	this.gc.setLineWidth(expessura);
    }
    
    /** M�todo para desenhar uma linha que vai de [xi,yi] at� [xf,yf].
     *	Se o par�metro estilo for Estilo.LINHAS ser�o desenhadas as linhas de contorno da forma, 
     *  e se for Estilo.PONTOS, ser�o desenhados apenas os v�rtices da forma. */
    public void linha(double xi, double yi, double xf, double yf, Estilo estilo){
    	if(estilo == Estilo.PONTOS){
    		ponto(xi-1, yi-1);
    		ponto(xf-1, yf-1);
    	}else{
	    	this.gc.beginPath();
	    	this.gc.moveTo(xi, yi);
	    	this.gc.lineTo(xf, yf);
	    	this.gc.stroke();
	    	this.gc.closePath();
    	}
    }
    
    /** M�todo para desenhar uma linha que vai de [xi,yi] at� [xf,yf].*/
    public void linha(double xi, double yi, double xf, double yf){    	
    	this.gc.beginPath();
    	this.gc.moveTo(xi, yi);
    	this.gc.lineTo(xf, yf);
    	this.gc.stroke();
    	this.gc.closePath();    
    }


    /** M�todo para escrever um texto na tela, nas coordenadas [x,y], com a fonte de tamanho tam.*/
    public void texto(String texto, double x, double y, int tam){
    	texto(texto, x, y, tam, FontWeight.NORMAL);
    }
    
    /** M�todo para escrever um texto na tela, nas coordenadas [x,y], com a fonte de tamanho tam e tipo tipo.*/
    public void texto(String texto, double x, double y, int tam, FontWeight tipo){
        Font fonte = Font.font("Times New Roman", tipo, tam);
        this.gc.setFont(fonte);
        this.gc.fillText(texto, x, y);
        this.gc.strokeText(texto, x, y);
    }
    
    /** M�todo para detectar a colis�o entre dois ret�ngulos.*/
    public boolean colisao(Rectangle2D objeto1, Rectangle2D objeto2){    	
    	return objeto1.intersects(objeto2);     
    }
    
    /** M�todo para detectar a colis�o entre dois poligonos.*/
    public boolean colisao(Polygon objeto1, Polygon objeto2){    	
    	Shape colisor = Shape.intersect(objeto1, objeto2);
        if (colisor.getBoundsInParent().getWidth() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    
    /** M�todo para detectar a colis�o entre duas formas.*/
    public boolean colisao(double xP1, double yP1, double xP2, double yP2, double larguraP1, double alturaP1, double larguraP2, double alturaP2){
    	Rectangle2D objeto1 = new Rectangle2D(xP1, yP1, larguraP1, alturaP1);
    	Rectangle2D objeto2 = new Rectangle2D(xP2, yP2, larguraP2, alturaP2);
    	return objeto1.intersects(objeto2);     
    }    
    
    
    /** M�todo para rotacionar um objeto em ang graus.*/
    public Polygon rotacionar(Polygon p, double ang){    			
		double[] points = p.getPoints().stream().mapToDouble(Number::doubleValue).toArray();		
		p.setRotate(ang);		
		p.getLocalToParentTransform().transform2DPoints(points, 0, points, 0, points.length/2);
		return new Polygon(points);				        
    }
    
    /** M�todo para transladar um objeto para as coordenadas [x,y].*/
    public Polygon transladar(Polygon p, double x, double y){
    	double[] points = p.getPoints().stream().mapToDouble(Number::doubleValue).toArray();    	
		p.setTranslateX(x);
		p.setTranslateY(y);		
		p.getLocalToParentTransform().transform2DPoints(points, 0, points, 0, points.length/2);
		return new Polygon(points);		
    }
    
    /** M�todo para aplicar a transforma��o de escala em um objeto nas coordenadas x e y.*/
    public Polygon escalar(Polygon p, double x, double y){
		double[] points = p.getPoints().stream().mapToDouble(Number::doubleValue).toArray();		
		p.setScaleX(x);
		p.setScaleY(y);
		p.getLocalToParentTransform().transform2DPoints(points, 0, points, 0, points.length/2);
		return new Polygon(points);
		
		
    }
    
    /** M�todo para rotacionar um objeto em ang graus.*/
    public void rotacionar(double ang){
    	this.gc.rotate(ang);        
    }
    
    /** M�todo para transladar um objeto para as coordenadas [x,y].*/
    public void transladar(double x, double y){
    	this.gc.translate(x,  y);        
    }
    
    /** M�todo para aplicar a transforma��o de escala em um objeto nas coordenadas x e y.*/
    public void escalar(double x, double y){
    	this.gc.scale(x, y);
    }
    
    /** M�todo para desenhar uma imagem na tela. Pode ser usado em conjunto com um Rectangle2D para colis�o.*/
    public void imagem(Image img, double x, double y){
    	this.gc.drawImage(img, x, y);
    }
    
    /** M�todo para empilhar uma transforma��o.*/
    public void empilhar(){
    	this.gc.save();
    }
    
    /** M�todo para desempilhar uma transforma��o.*/
    public void desempilhar(){
    	this.gc.restore();
    }
           
    private void rodar(Event e) {
        this.atualizar();
        this.desenhar();
    }

    /** M�todo para retomar o jogo pausado.*/
    public void retomar() {
    	this.loop.play();
    }

    /** M�todo para pausar o jogo.*/
    public void pausar() {
    	this.loop.pause();
    }

    /** M�todo para resetar o jogo.*/
    public void resetar() {
    	this.loop.stop();
    	this.loop.playFromStart();
    }

    private Timeline criarloop() {
        // Baseado em https://carlfx.wordpress.com/2012/04/09/javafx-2-gametutorial-part-2/
        final Duration d = Duration.millis(1000 / fps);
        final KeyFrame oneFrame = new KeyFrame(d, this::rodar);
        Timeline t = new Timeline(fps, oneFrame);
        t.setCycleCount(Animation.INDEFINITE);
        return t;
    }    
      
    /** M�todo para usar a tela cheia no jogo.*/
    public void telaCheia(boolean sair) {    			
	    this.largura = (int) this.tamanhoTela.getWidth();
	    this.altura =  (int) this.tamanhoTela.getHeight();    		 		
	    this.canvas.setHeight(this.altura);
	    this.canvas.setWidth(this.largura);	   				
		if(!sair) {
			cena.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		}
		this.cena.setFullScreen(true);
    }
    
    /** M�todo para fazer alguma a��o ao fechar jogo, como salvar o estado atual..*/
    public abstract void acaoAoSair();
    
    /** M�todo para fazer alguma a��o ao iniciar jogo, como carregar o estado anterior..*/
    public abstract void acaoAoIniciar();
    
    /** M�todo para tratar eventos de tecla pressionada do teclado.*/
    public abstract void teclaPressionada(KeyEvent e);
    
    /** M�todo para tratar eventos de tecla liberada do teclado.*/
    public abstract void teclaLiberada(KeyEvent e);
    
    /** M�todo para tratar eventos de tecla digitada do teclado.*/
    public abstract void teclaDigitada(KeyEvent e);
    
    /** M�todo para tratar eventos de clique do mouse.*/
    public abstract void cliqueDoMouse(MouseEvent e);
    
    /** M�todo para tratar eventos de movimento do mouse.*/
    public abstract void movimentoDoMouse(MouseEvent e);
    
    /** M�todo para tratar eventos de movimento do mouse quando est� com um bot�o pressionado.*/
    public abstract void movimentoDoMousePressionado(MouseEvent e);
    
    /** M�todo chamado continuamente para atualizar valores e realizar c�lculos necess�rios para o jogo.*/
    public abstract void atualizar();

    /** M�todo chamado continuamente para desenhar na tela.*/
    public abstract void desenhar();
        
    
    /** M�todo para iniciar o jogo. */
    private void iniciar() {
		// Inicializa��es do JavaFX para iniciar o jogo.
    	this.cena.setResizable(false);
    	// Ajuste para corrigir escala devido ao resizable false...
    	this.cena.setHeight(altura+27);
    	this.cena.setWidth(largura+5);
   	    // Posicionando a janela no centro do monitor..
    	this.cena.setX(this.tamanhoTela.getWidth()/2 - larguraPadrao/2);
    	this.cena.setY(this.tamanhoTela.getHeight()/2 - alturaPadrao/2);   	    
    	this.canvas.setHeight(altura);
    	this.canvas.setWidth(largura); 
    	this.cena.setScene(new Scene(new StackPane(this.canvas)));
    	this.cena.setTitle(this.nome);
    	this.cena.show();
        // Inicia o loop do jogo.
        this.loop.playFromStart();
	}

    /** M�todo auxiliar para trocar o �cone da janela do jogo.*/
    public static void adicionarIcone(Stage cena, Image icone) {    	
    	cena.getIcons().add(icone);
    }

    
    public static class EfeitosSonoros{
    	static ExecutorService soundPool = Executors.newFixedThreadPool(2);
    	static Map<String, Integer> soundEffectsMap = new HashMap<>();
    	private static LinkedList<AudioClip> listaDeAudios = new LinkedList<AudioClip>();
        
        public static void definirThreadsSom(int soundThreads) {
        	soundPool = Executors.newFixedThreadPool(soundThreads);
        }
      	
        /** M�todo para carregar um arquivo de som.*/
        public static void carregarSom(String id, URL url) {
            AudioClip sound = new AudioClip(url.toExternalForm());
            listaDeAudios.add(sound);
            soundEffectsMap.put(id, listaDeAudios.size() - 1);
        }
        
        /** M�todo para ajustar o volume de um determinado som j� carregado. De 0.0 a 1.0*/
        public static void volumeSom(String id, double volume) {
        	if(soundEffectsMap.containsKey(id)) {
	        	listaDeAudios.get(soundEffectsMap.get(id)).setVolume(volume);	        	
        	}else {
        		System.out.println("Som [ " + id + " ] n�o carregado ainda.");
        	}
        }

        /** M�todo para tocar um som. */
        public static void tocarSom(final String id, final boolean exclusivo, final boolean sobreposto) {
        	if(soundEffectsMap.containsKey(id)) {
	        	if (exclusivo) {
	        		for (int i = 0; i < listaDeAudios.size(); i++) {
	    				if (listaDeAudios.get(i).isPlaying() && soundEffectsMap.get(id) != i) {
	    					listaDeAudios.get(i).stop();
	    				}
	    			}
	        	}
	            Runnable soundPlay = new Runnable() {
	                @Override
	                public void run() {
	                	if(!sobreposto) {
		                	if(!listaDeAudios.get(soundEffectsMap.get(id)).isPlaying()) {
		                		listaDeAudios.get(soundEffectsMap.get(id)).play();                	
		                	}                
		                }else {
		                	listaDeAudios.get(soundEffectsMap.get(id)).play();	                	
	                	}
	                }
	            };
	            soundPool.execute(soundPlay);
        	}else {
        		System.out.println("Som [ " + id + " ] n�o carregado ainda.");
        	}
        }
        
        /** M�todo para parar um som.*/
        public static void pararSom(final String id) {
        	if(soundEffectsMap.containsKey(id)) {
	            if(listaDeAudios.get(soundEffectsMap.get(id)).isPlaying()) {
	            	listaDeAudios.get(soundEffectsMap.get(id)).stop();                	
	             }                	                          
        	}else {
        		System.out.println("Som [ " + id + " ] n�o carregado ainda.");
        	}
        }       
    }

    public static enum Estilo{	
    	PONTOS(1), LINHAS(2), PREENCHIDO(3);    	
    	private final int valor;
    	
    	Estilo(int valor){
    		this.valor = valor;
    	}
    	public int getValor(){
    		return valor;
    	}
    }    
}
