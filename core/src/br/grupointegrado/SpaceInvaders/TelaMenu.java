package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Rhuan Coltre on 14/09/2015.
 */
public class TelaMenu extends TelaBase {

    private OrthographicCamera camera;
    private Stage palco;
    private ImageTextButton btnIniciar;
    private Label lbTitulo;
    private Label lbPontuacao;

    private BitmapFont fonteTitulo;
    private BitmapFont fonteBotoes;

    private Texture texturaBotao;               //  Textura normal do botao
    private Texture texturaBotaoPressionado;    // Textura botao pressionado


    public TelaMenu(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));
        Gdx.input.setInputProcessor(palco);  //Define o palco como processador de entradas

        initFontes();
        initLabels();
        initBotoes();
    }

    private void initBotoes() {
        texturaBotao = new Texture("buttons/button.png");
        texturaBotaoPressionado = new Texture("buttons/button-down.png");
        ImageTextButton.ImageTextButtonStyle estilo = new ImageTextButton.ImageTextButtonStyle();

        estilo.font = fonteBotoes;
        estilo.up = new SpriteDrawable(new Sprite(texturaBotao));
        estilo.down = new SpriteDrawable(new Sprite(texturaBotaoPressionado));

        btnIniciar = new ImageTextButton(" Começa ae Carai ", estilo);
        palco.addActor(btnIniciar);

        btnIniciar.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Evento de clique no botão
                game.setScreen(new TelaJogo(game));
            }
        });

    }

    private void initLabels() {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fonteTitulo;

        lbTitulo = new Label("Space Invaders", estilo);
        palco.addActor(lbTitulo);

        Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
        int pontuacaoMaxima = preferencias.getInteger("pontuacao_maxima", 0);

        estilo = new Label.LabelStyle();
        estilo.font = fonteBotoes;

        lbPontuacao = new Label("Pontuacao máxima: " + pontuacaoMaxima + " pontos ", estilo);
        palco.addActor(lbPontuacao);

    }

    private void initFontes() {
        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        // Instancia a fonte utilizada no titulo
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48;
        params.color = new Color(.25f, .25f, .85f, 1); // tom de azul
        params.shadowOffsetY = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = Color.BLACK;

        // Instacia a fonte utilizada nos botoes
        fonteTitulo = gerador.generateFont(params);

        params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 32;
        params.color = Color.BLACK;

        fonteBotoes = gerador.generateFont(params);

        gerador.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        atualizarLabels();
        atualizarBotoes();
        palco.act(delta);
        palco.draw();
    }

    private void atualizarBotoes() {
        float x = camera.viewportWidth / 2 - btnIniciar.getPrefWidth() / 2;
        float y = camera.viewportHeight / 2 - btnIniciar.getPrefHeight() / 2;
        btnIniciar.setPosition(x, y);
    }

    private void atualizarLabels() {
        float x = camera.viewportWidth / 2 - lbTitulo.getPrefWidth() / 2;     // altura camera div por 2
        float y = camera.viewportHeight - 80;  //


        lbTitulo.setPosition(x, y);

        x = camera.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2;
        y = 100;

        lbPontuacao.setPosition(x, y);
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width,height);
        camera.update();

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

        palco.dispose();
        fonteTitulo.dispose();
        fonteBotoes.dispose();
        texturaBotao.dispose();
        texturaBotaoPressionado.dispose();
    }


}
