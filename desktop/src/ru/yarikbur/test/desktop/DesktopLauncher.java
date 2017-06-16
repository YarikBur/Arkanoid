package ru.yarikbur.test.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.yarikbur.test.Main;
import ru.yarikbur.util.In;

public class DesktopLauncher {
	static In in = new In();
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 360;
		config.title = "Test title";
		config.resizable = false;
		System.out.println("'Esc' - Close this game");
		System.out.println("'Q', 'E' - Rotate platform");
		System.out.println("'A', '<-' - Move the platform to the left");
		System.out.println("'D', '->' - Move the platform to the right");
		System.out.println("'Spacebar' - Start game");
		in.getString("Press enter to continue...");
		new LwjglApplication(new Main(), config);
		System.out.println("Loading game...");
	}
}
