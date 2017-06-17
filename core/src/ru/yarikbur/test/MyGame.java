package ru.yarikbur.test;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ru.yarikbur.controller.Contact;
import ru.yarikbur.controller.Input;
import ru.yarikbur.util.In;

public class MyGame extends ApplicationAdapter implements Screen {
	HashMap<String, TextureRegion> Bricks = new HashMap<String, TextureRegion>();
	Texture ball_small;
	Texture ball_big;
	Texture platform;
	
	Sprite Ball_small;
	Sprite Ball_big;
	Sprite Platfom;
	Sprite brick;
	
	Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Matrix4 debugMatrix;
	
	public World world;
	public Contact contact;
	public Input input;
	
	Body Ball;
	Body Platform;
	Body Destroy=null;
	
	float speed=0, key=0, speedP=0;
	
	final float PTM = 100f;
	
	boolean pressedKey=false, debug=true;
	public boolean con=false;
	
	Main main;
	In in = new In();
	
	SpriteBatch batch;
	
	public MyGame(Main main) { this.main = main; }
	
	public MyGame(MyGame myGame) { }

	@Override
	public void create () {
		batch = new SpriteBatch();
		input = new Input();
		Gdx.input.setInputProcessor(input);
		loadTextures();
		loadSprite();
		
		world = new World(new Vector2(0, -1f), false);
		contact = new Contact(world);
		world.setContactListener(contact);
		
		Ball = createObj(BodyDef.BodyType.DynamicBody, Ball_small, "Player", Ball_small.getX(), Platfom.getY()+Platfom.getHeight()+3);
		Ball.getFixtureList().get(0).setUserData("Ball");
		Ball.setFixedRotation(false);
		
		Platform = createObj(BodyDef.BodyType.StaticBody, Platfom, "Platform", Platfom.getX(), Platfom.getY());
		Platform.getFixtureList().get(0).setUserData("Platform");
		
		Body Wall = createWall(Gdx.graphics.getWidth()/PTM, Gdx.graphics.getHeight()/PTM+280/PTM, "Right");
		Wall.getFixtureList().get(0).setUserData("Right");
		Wall = createWall(Gdx.graphics.getWidth()/PTM, Gdx.graphics.getHeight()/PTM-359/PTM, "Left");
		Wall.getFixtureList().get(0).setUserData("Left");
		Wall = createWall(Gdx.graphics.getWidth()/PTM-280/PTM, Gdx.graphics.getHeight()/PTM+80/PTM, "Up");
		Wall.getFixtureList().get(0).setUserData("Up");
		
		for(float y=Gdx.graphics.getHeight()-5-brick.getHeight();y>=brick.getHeight()*7+5*6;y-=brick.getHeight()+5)for(int i=18;i<=585;i+=brick.getWidth()){
			Body Brick = createObj(BodyDef.BodyType.StaticBody, new Sprite(Bricks.get("brick0")), "Brick", i, y);
			Brick.getFixtureList().get(0).setUserData("Brick");
		}
	}
	
	@Override
	public void render (float delta) {
		camera.update();
		updateVariables();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(delta, 4, 4);
		batch.begin();
		
		pressed();
		updatePosition();
		drawSprite();
		
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PTM, PTM, 0);
		if(debug) renderer.render(world, debugMatrix);
		
		batch.end();
		gameover();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		ball_small.dispose();
		ball_big.dispose();
		platform.dispose();
	}
	
	private void updateVariables(){
		this.con = contact.con();
		this.speed = contact.speed();
		this.key = input.key();
		this.debug = input.debug();
		this.pressedKey = input.pressedKey();
		this.speedP = contact.speedP();
	}
	
	private void gameover(){
		if(Ball.getPosition().y<=0){
			main.setScreen(main.game);
		}
	}
	
	private void loadSprite(){
		Platfom = new Sprite(platform);
		Platfom.setPosition(Gdx.graphics.getWidth()/2-Platfom.getWidth()/2, 10);
		
		Ball_small = new Sprite(ball_small);
		Ball_small.setPosition(Platfom.getX()+Platfom.getWidth()/2-Ball_small.getWidth()/2, Gdx.graphics.getHeight()/2-Ball_small.getHeight()/2);
		
		brick = new Sprite(Bricks.get("brick1"));
		brick.setPosition(25, Gdx.graphics.getHeight()-50-brick.getHeight());
	}
	
	private void drawSprite(){
		Ball_small.draw(batch);
		Platfom.draw(batch);
	}
	
	private void updatePosition(){
		pos(Ball, Ball_small);
		pos(Platform, Platfom);
	}
	
	private void pos(Body body, Sprite sprite){
		sprite.setPosition((body.getPosition().x*PTM)-sprite.getWidth()/2, 
				(body.getPosition().y*PTM)-sprite.getHeight()/2);
		sprite.setRotation(body.getAngle()*PTM);
	}
	
	private Body createWall(float w, float h, String str){
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        FixtureDef fDef = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        if(str.equals("Left") | str.equals("Right")) edgeShape.set(h,-w,h,w);
        else edgeShape.set(h+h/2+h/4,w,-h/2,w);
        fDef.shape = edgeShape;
        fDef.restitution=1;

        Body body = world.createBody(bodyDef);
        body.createFixture(fDef);
        edgeShape.dispose();
		return body;
	}
	
	@SuppressWarnings("unused")
	private Body createObj(BodyDef.BodyType type, Sprite sprite, String str, float x, float y){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set((x + sprite.getWidth()/2)/PTM, (y+sprite.getHeight()/2)/PTM);
		
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2/PTM, sprite.getHeight()/2/PTM);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(sprite.getWidth()/2/PTM);
		
		FixtureDef fDef = new FixtureDef();
		if(str.equals("Player")) fDef.shape = circle;
		else fDef.shape = shape;
		fDef.density=1;
		fDef.friction=0;
		if(!str.equals("player")) fDef.restitution=(float) 1.05;
		Fixture fixture = body.createFixture(fDef);
		shape.dispose();
		circle.dispose();
		return body;
	}
	
	private void loadTextures(){
		Texture texture  = new Texture(Gdx.files.internal("my/brick_atlas.png"));
		TextureRegion brick[][] = TextureRegion.split(texture, texture.getWidth()/5, texture.getHeight());
		for(int x=0;x<5;x++) Bricks.put("brick"+x, brick[0][x]);
		
		ball_small = new Texture("my/ball_small.png");
		ball_big = new Texture("my/ball_big.png");
		platform = new Texture("my/platform.png");
	}
	
	private void pressed(){
		Vector2 xy = Platform.getPosition();
		 
		if(pressedKey){
			if(key==29 | key==21) Platform.setTransform(xy.x-=speedP/PTM, xy.y, Platform.getAngle());
			else if(key==32 | key==22) Platform.setTransform(xy.x+=speedP/PTM, xy.y, Platform.getAngle());
			else if(key==51 | key==19) speed+=0.5;
			else if(key==47 | key==20) speed-=0.5;
			else if(key==62 & con) Ball.setLinearVelocity(speed, 3);
			else if(key==33) Platform.setTransform(xy.x, xy.y, Platform.getAngle()-1/PTM);
			else if(key==45) Platform.setTransform(xy.x, xy.y, Platform.getAngle()+1/PTM);
			else if(key==131) Gdx.app.exit();
		}
	}

	@Override
	public void show() { this.create(); }

	@Override
	public void hide() { this.dispose(); }
}
