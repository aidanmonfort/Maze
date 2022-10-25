package com.monfort;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.FileNotFoundException;

public class MyMazeSolver extends Game{
	public void create(){
		try {
			setScreen(new GameScreen());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
