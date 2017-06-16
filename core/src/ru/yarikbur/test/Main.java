package ru.yarikbur.test;

import com.badlogic.gdx.Game;

public class Main extends Game {
	public MyGame game;
	
	@Override
	  public void create() {
	    game = new MyGame(this);
	    setScreen(game);
	}
}
