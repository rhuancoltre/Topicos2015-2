package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
    private Stage palcoInformacoes;
    private BitmapFont fonte;
    private Label lbpontuacao;
    private Label lbGameOver;
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

    private Array<Texture> texturasExplosao = new Array<Texture>();
    private Array<Explosao> explosoes = new Array<Explosao>();

    private Sound somTiro;
    private Sound somExplosao;
    private Sound somGameOver;
    private Music musicaFundo;

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
        palcoInformacoes = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        initSons();
        initTexturas();
        initFonte();
        initInformacoes();
        initJogador();
    }

    private void initSons() { //Declarando som do tiro e da explosao
        somTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        somExplosao = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        somGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.mp3"));
        musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
        musicaFundo.setLooping(true);

    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");

        for (int i =1; i<= 17; i++) {
            Texture text = new Texture("sprites/explosion-" + i + ".png");
            texturasExplosao.add(text);
        }
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
        palcoInformacoes.addActor(lbpontuacao);

        lbGameOver = new Label("Já era Mermao ! ", lbEstilo);
        lbGameOver.setVisible(false);
        palco.addActor(lbGameOver);
    }

    /**
     * Instancia os ojetos de Fonte
     */
    private void initFonte() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.size = 24;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        param.shadowColor = Color.BLUE;

        //fonte = new BitmapFont();
        fonte = generator.generateFont(param);

        generator.dispose();




    }

    /**
     * Chamado todo quadro de atualizacao do jogo (FPS)
     * @param delta Tempo entre um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.05f, .05f, .15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbpontuacao.setPosition(10, camera.viewportHeight -
                lbpontuacao.getPrefHeight() - 20);
        lbpontuacao.setText(pontuacao + " pontos");

        lbGameOver.setPosition(camera.viewportWidth / 2 -
                lbGameOver.getPrefWidth() / 2,
            camera.viewportHeight / 2);
      lbGameOver.setVisible(gameover == true);

        atualizarExplosoes(delta);

        if ( gameover == false) {
            if (!musicaFundo.isPlaying()) // se nao está tocando
                musicaFundo.play();     //inicia musica

            capturaTeclas();
            atualizarJogador(delta);
            atualizarTiros(delta);
            atualizarMeteoros(delta);
            detectarColisoes(meteoro1, 5);
            detectarColisoes(meteoro2, 15);

        } else {
            if (musicaFundo.isPlaying()) // se está tocando
                musicaFundo.stop();     //parar musica
            reiniciarJogo();
            
        }


        //Atualiza a situacao do Palco
        palco.act(delta);
        //Desenha o palco na tela
        palco.draw();

        //Desenha o palco de informações
        palcoInformacoes.act(delta);
        palcoInformacoes.draw();

    }

    /**
     * Verifica se o usuário pressionou ENTER para reiniciar jogo
     */
    private void reiniciarJogo() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
            int pontuacaoMaxima = preferencias.getInteger("pontuacao_maxima", 0);
            //Verifica se minha nova pontuação =e maior que a pontuação maxima.
            if (pontuacao > pontuacaoMaxima) {
                preferencias.putInteger("pontuacao_maxima", pontuacao);
                preferencias.flush();
            }
            game.setScreen(new TelaMenu(game));
        }

    }

    private void atualizarExplosoes(float delta) {
        for (Explosao explosao : explosoes) {
            //Verifica se a explosão chegou ao fim
            if (explosao.getEstagio() >= 16) {
                explosoes.removeValue(explosao, true); //Remove a explosao do array
                explosao.getAtor().remove();
            } else {
                //ainda nao chegou ao fim
                explosao.atualizar(delta);
            }
        }
    }

    private Rectangle recJogador =  new Rectangle();
    private Rectangle recTiro = new Rectangle();
    private Rectangle recMeteoro = new Rectangle();

    private int pontuacao = 0;
    private boolean gameover = false;

    private void detectarColisoes(Array<Image> meteoros, int valePonto) {
        recJogador.set(jogador.getX(), jogador.getY(), jogador.getWidth(), jogador.getHeight());

        for (Image meteoro : meteoros) {
            recMeteoro.set(meteoro.getX(), meteoro.getY(), meteoro.getWidth(), meteoro.getHeight());
            //detecta colisoes com os tiros
            for (Image tiro : tiros) {
                recTiro.set(tiro.getX(), tiro.getY(), tiro.getWidth(), tiro.getHeight());

                if (recMeteoro.overlaps(recTiro)) {
                    //aqui ocorre uma colisão do tiro com o meteoro
                    pontuacao += 15;
                    tiro.remove(); // remove do palco
                    tiros.removeValue(tiro, true); // remove da lista
                    meteoro.remove();
                    meteoros.removeValue(meteoro, true); //remove da lista
                    criarExplosao (meteoro.getX() + meteoro.getWidth() / 2,
                            meteoro.getY()+ meteoro.getHeight() / 2);
                }

            }
            //detecta colisao com o player
        if (recJogador.overlaps(recMeteoro)) {
                //ocorre colisao de jogador com meteoro 1
            gameover = true;
            somGameOver.play();
            }

        }

    }

    //Cria a explosão na posiçao X e Y
    private void criarExplosao(float x, float y) {
        Image ator = new Image(texturasExplosao.get(0));
        ator.setPosition(x - ator.getWidth() / 2, y - ator.getHeight() / 2);
        palco.addActor(ator);

        Explosao explosao = new Explosao(ator, texturasExplosao);
        explosoes.add(explosao);

        somExplosao.play();
    }

    private void atualizarMeteoros(float delta) {
        int qtdMeteoros = meteoro1.size + meteoro2.size; // retorna a qtd meteoros criado

        if (qtdMeteoros < 10) {

            int tipo = MathUtils.random(1, 4); // Retorna 1 ou 2 aleartóriamente

            if (tipo == 1) {
                //Cria meteoro1
                //Cria meteoro1
                Image meteoro = new Image(texturaMeteoro1);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);

                meteoro.setPosition(x, y);
                meteoro1.add(meteoro);
                palco.addActor(meteoro);
            } else if (tipo == 2){
                //Cria meteoro2
                Image meteoro = new Image(texturaMeteoro2);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoro2.add(meteoro);
                palco.addActor(meteoro);
            }
        }
            float velocidade1 = 150; // 200 pixels por segundos
            for (Image meteoro : meteoro1) {
                float x = meteoro.getX();
                float y = meteoro.getY() - velocidade1 * delta;
                meteoro.setPosition(x, y);  // atualiza a posição do meteoro
                if (meteoro.getY() + meteoro.getHeight() < 0) {
                    meteoro.remove(); // remove do palco
                    meteoro1.removeValue(meteoro, true); // remove da lista
                    pontuacao = pontuacao - 30;
                }
            }

            float velocidade2 = 250; // 200 pixels por segundos
            for (Image meteoro : meteoro2) {
                float x = meteoro.getX();
                float y = meteoro.getY() - velocidade2 * delta;
                meteoro.setPosition(x, y);  // atualiza a posição do meteoro
                if (meteoro.getY() + meteoro.getHeight() < 0) {
                    meteoro.remove(); // remove do palco
                    meteoro2.removeValue(meteoro, true); // remove da lista
                    pontuacao = pontuacao - 60;
                }
            }


    }

    private final float maxIntervaloTiros = 0.14F; //Minimo de tempo entre os tiros
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
                somTiro.play();
            }
        }
        float velocidade = 250; //Velocidade de movimentação do tiro
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
        float velocidade = 300; //Velocidade de movimento do jogador
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

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || clicouEsquerda()) {
            indoEsquerda = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || clicourDireita() ) {
            indoDireita = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            indoCima = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            indoBaixo = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.app.getType() == Application.ApplicationType.Android) {
            atirando = true;
        }


    }

    private boolean clicourDireita() {
        if (Gdx.input.isTouched()) {
            Vector3 posicao = new Vector3();
            //Captura clique/toque na janela do Windows
            posicao.x = Gdx.input.getX();
            posicao.y = Gdx.input.getY();
            //Converter para uma coordenado do jogo
            camera.unproject(posicao);
            float meio = camera.viewportWidth / 2;
            if (posicao.x > meio) {
                return true;
            }
        }
            return false;

    }

    private boolean clicouEsquerda() {
        if (Gdx.input.isTouched()) {
            Vector3 posicao = new Vector3();
            //Captura clique/toque na janela do Windows
            posicao.x = Gdx.input.getX();
            posicao.y = Gdx.input.getY();
            //Converter para uma coordenado do jogo
            camera.unproject(posicao);
            float meio = camera.viewportWidth / 2;
            if (posicao.x < meio) {
                return true;
            }
        }
            return false;

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
        palcoInformacoes.dispose();
        fonte.dispose();
        texturaJogador.dispose();
        texturaJogadorDireita.dispose();
        texturaJogadorEsquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();
        for (Texture text : texturasExplosao) {
            text.dispose();
        }
        somTiro.dispose();
        somExplosao.dispose();
        somGameOver.dispose();
    }
}
