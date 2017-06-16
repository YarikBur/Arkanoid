package ru.yarikbur.controller;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
	public Input(){
		super();
	}
	
	boolean pressedKey=false, debug=true;
	float key=0;
	
	public boolean pressedKey(){ return pressedKey; }
	public boolean debug(){ return debug; }
	public float key(){ return key; }

	@Override
	public boolean keyDown(int keycode) {
		pressedKey=true;
		key=keycode;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		pressedKey=false;
		key=0;
		if(keycode==245) debug = !debug;
		return true;
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }
}
