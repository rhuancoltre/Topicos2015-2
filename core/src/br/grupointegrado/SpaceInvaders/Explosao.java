package br.grupointegrado.SpaceInvaders;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rhuan Coltre on 31/08/2015.
 */
public class Explosao {

    private static float tempo_troca = 1f / 17f; // Aproximadamente 0,05segundos

    private int estagio = 0; // Controla estagio de 0 a 16
    private Array<Texture> texturas;
    private Image ator;
    private float tempoAcumulado = 0;

    public Explosao(Image ator, Array<Texture> texturas){
        this.ator = ator;
        this.texturas = texturas;
    }


    /**
     * Calcula tempo acumulado e realiza a troca do estágio da explosão
     * Exemplo:
     * Cada quadro demora 0,016 segundos
     * Cada imagem deve permanecer 0,05 segundos
     **/

    public void atualizar (float delta) {
        tempoAcumulado = tempoAcumulado + delta;
        if (tempoAcumulado >= tempo_troca) {
            tempoAcumulado = 0;
            estagio ++;
            Texture textura = texturas.get(estagio);
            ator.setDrawable(new SpriteDrawable(new Sprite(textura)));

        }
    }

    public Image getAtor() {
        return ator;
    }

    public int getEstagio() {
        return estagio;


    }
}
