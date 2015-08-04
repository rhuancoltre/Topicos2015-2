package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Screen;

/**
 * Created by Rhuan Coltre on 03/08/2015.
 */
public abstract class TelaBase implements Screen {

    protected MyGdxGame game;

    public TelaBase(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void hide() {
        dispose();
    }
}
