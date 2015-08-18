package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;


/**
 * Created by Rhuan Coltre on 03/08/2015.
 */
public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbpontuacao;
    private Image jogador;
    private Texture texturaJogador;
    private Texture texturaJogadorDireita;
    private Texture texturaJogadorEsquerda;
    private boolean indoDireita;
    private boolean indoEsquerda;
    private boolean indoCima;
    private boolean indoBaixo;
    private boolean atirando;
    private Array<Image> tiros = new Array<Image>();
    private Texture texturaTiro;
    private Texture texturaMeteoro1;
    private Texture texturaMeteoro2;
    private Array<Image> meteoro1 = new Array<Image>();
    private Array<Image> meteoro2 = new Array<Image>();

    /**
     * Contructor padrao de tela do Jogo
     * @param game Referencia para a classe principal;
     */
    public TelaJogo(MyGdxGame game) {
        super(game);
    }

    /**
     * Chamado quando a tela eh exibida.
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        initTexturas();
        initFonte();
        initInformacoes();
        initJogador();
    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");
    }

    /**
     * Instancia os objeytos do jogador e adiciona no palco
     */

    private void initJogador() {
        texturaJogador = new Texture("sprites/player.png");
        texturaJogadorDireita = new Texture("sprites/player-right.png");
        texturaJogadorEsquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturaJogador);
        float x = camera.viewportWidth / 2 - jogador.getWidth() / 2;
        float y = 10;
        jogador.setPosition(x, y);
        palco.addActor(jogador);
    }

    /**
     * Instancia as informacoes escritas na tela
     */
    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbpontuacao = new Label("0 pontos", lbEstilo);
        palco.addActor(lbpontuacao);
    }

    /**
     * Instancia os ojetos de Fonte
     */
    private void initFonte() {
        fonte = new BitmapFont();
    }

    /**
     * Chamado todo quadro de atualizacao do jogo (FPS)
     * @param delta Tempo entre um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.05f, .05f, .15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbpontuacao.setPosition(10, camera.viewportHeight - 20);
        capturaTeclas();
        atualizarJogador(delta);
        atualizarTiros(delta);
        atualizarMeteoros(delta);


        //Atualiza a situacao do Palco
        palco.act(delta);
        //Desenha o palco na tela
        palco.draw();

    }

    private void atualizarMeteoros(float delta) {
        int tipo = MathUtils.random(1, 3);
        if (tipo == 1) {
            //Cria meteoro1
            Image meteoro = new Image(texturaMeteoro1);
            float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
            float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
            meteoro.setPosition(x, y);
            meteoro1.add(meteoro);
            palco.addActor(meteoro);
        }else {
            //Cria meteoro2
            Image meteoro = new Image(texturaMeteoro2);
        }
        float velocidade = 200;
        for (Image meteoro : meteoro1){
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade * delta;
            meteoro.setPosition(x, y);
        }

    }

    private final float maxIntervaloTiros = 0.3F; //Minimo de tempo entre os tiros
    private float intervaloTiros = 0; //Tempo acumulado entre os tiros

    private void atualizarTiros(float delta) {
        intervaloTiros = intervaloTiros + delta; //Acumula o tempo percorrido
        //Cria um novo tiro se necessário
        if (atirando) {
            //Verifica se o tempo minimo foi atingido
            if (intervaloTiros >= maxIntervaloTiros) {


                Image tiro = new Image(texturaTiro);
                float x = jogador.getX() + jogador.getWidth() / 2 - tiro.getWidth() / 2;
                float y = jogador.getY() + jogador.getHeight();
                tiro.setPosition(x, y);
                tiros.add(tiro);
                palco.addActor(tiro);
                intervaloTiros = 0;
            }
        }
        float velocidade = 200; //Velocidade de movimentação do tiro
        //percorre todos os tiros existentes
        for (Image tiro : tiros) {
            //Movimenta o tiro em direção ao topo
            float x = tiro.getX();
            float y = tiro.getY() + velocidade * delta;
            tiro.setPosition(x, y);
            //Remove os tiros que sairam da tela
            if (tiro.getY() > camera.viewportHeight) {
                tiros.removeValue(tiro, true); //Remove da Lista
                tiro.remove(); // Remove do palco

            }


        }
    }

    /**
     * Atualiza a posição do jogador
     * @param delta
     */
    private void atualizarJogador(float delta) {
        float velocidade = 200; //Velocidade de movimento do jogador
        if (indoDireita) {
            //Verifica se o jogador está dentro da tela
            if (jogador.getX() < camera.viewportWidth - jogador.getWidth()) {
                float x = jogador.getX() + velocidade * delta;
                float y = jogador.getY();
                jogador.setPosition(x, y);

            }
        }
        if (indoEsquerda) {
            //Verifica se o jogador está dentro da tela.
            if (jogador.getX() > 0) {
                float x = jogador.getX() - velocidade * delta;
                float y = jogador.getY();
                jogador.setPosition(x, y);

            }
        }
        if (indoCima) {
            float x = jogador.getX();
            float y = jogador.getY() + velocidade * delta;
            jogador.setPosition(x, y);

        }

        if (indoBaixo) {
                float x = jogador.getX();
                float y = jogador.getY() - velocidade * delta;
                jogador.setPosition(x, y);


        }
        if (indoDireita) {
            //trocar imagem direita
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorDireita)));
        }else if (indoEsquerda) {
            //trocar imagem esqueda
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorEsquerda)));
        }else {
            //trocar imagem cena
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogador)));
        }

    }

    /**
     * Verifica se as teclas estão pressionadas
     */
    private void capturaTeclas() {
        indoDireita = false;
        indoEsquerda = false;
        indoCima = false;
        indoBaixo = false;
        atirando = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            indoEsquerda = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            indoDireita = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            indoCima = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            indoBaixo = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            atirando = true;
        }


    }

    /**
     * � chamado sempre que h� uma altera��o no tamanho da tela
     * @param width Novo valor de largura da tela
     * @param height Novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    /**
     * � chamado sempre o jogo for minimizado.
     */
    @Override
    public void pause() {

    }

    /**
     * � chamado sempre que o jogo voltar para o primeiro plano
     */
    @Override
    public void resume() {

    }

    /**
     * Eh chamado quando a tela for destruida;
     */
    @Override
    public void dispose() {
        batch.dispose();
        palco.dispose();
        fonte.dispose();
        texturaJogador.dispose();
        texturaJogadorDireita.dispose();
        texturaJogadorEsquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();
    }
}
