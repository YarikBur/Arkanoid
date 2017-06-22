package ru.yarikbur.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import ru.yarikbur.util.Bool;

public class Contact implements ContactListener {
	World world;
	Body Destroy;
	boolean con=false, one=true;
	float speed=0, speedP=6;
	
	public Contact(World world){
		super();
		this.world = world;
	}
	
	public boolean con(){ return con; }
	public float speed(){ return speed; }
	public float speedP(){ return speedP; }
	
	@Override
	public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
		Gdx.app.postRunnable(new Runnable(){
			@Override
			public void run() {
				try{ if(Destroy != null & Destroy.getFixtureList().get(0).getUserData().equals("Brick")) world.destroyBody(Destroy); Destroy=null; speedP+=0.75; }
				catch (java.lang.NullPointerException e) {}
			}
		});
	}

	@Override
	public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {
		if(contact.getFixtureA().getUserData().equals("Platform") & contact.getFixtureB().getUserData().equals("Ball"))
			con=false;
	}

	@Override
	public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, Manifold oldManifold) {
		WorldManifold manifold = contact.getWorldManifold();
		System.out.println("A: "+contact.getFixtureA().getUserData()+"  B: "+contact.getFixtureB().getUserData()+"  Contact: "+con+"  Speed: "+speedP);
		for(int i=0;i<manifold.getNumberOfContactPoints();i++){
			if(contact.getFixtureB().getUserData() != null & contact.getFixtureB().getUserData().equals("Ball") &
					contact.getFixtureA().getUserData() != null & contact.getFixtureA().getUserData().equals("Platform")){
				if(one){
					if(Bool.getRandomBoolean()) this.speed = 2;
					else this.speed = -2;
				}
				one=false;
				con=true;
				contact.setEnabled(true);
			} else if(contact.getFixtureB().getUserData() != null & contact.getFixtureB().getUserData().equals("Ball") &
					contact.getFixtureA().getUserData() != null & contact.getFixtureA().getUserData().equals("Right")){
				speed = -speed;
				contact.setEnabled(true);
			} else if(contact.getFixtureB().getUserData() != null & contact.getFixtureB().getUserData().equals("Ball") &
					contact.getFixtureA().getUserData() != null & contact.getFixtureA().getUserData().equals("Left")){
				speed = -speed;
				contact.setEnabled(true);
			} else if(contact.getFixtureB().getUserData() != null & contact.getFixtureB().getUserData().equals("Ball") &
					contact.getFixtureA().getUserData() != null & contact.getFixtureA().getUserData().equals("Brick")){
				speed = -speed;
				contact.setEnabled(true);
			}
		}
	}

	@Override
	public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, ContactImpulse impulse) {
		Body body = null;
		if(contact.getFixtureB() != null & contact.getFixtureB().getUserData() != null & contact.getFixtureB().getUserData().equals("Ball") &
				contact.getFixtureA() != null & contact.getFixtureA().getUserData() != null & contact.getFixtureA().getUserData().equals("Brick")) 
			body = contact.getFixtureA().getBody();
		else body=null;
		
		Destroy = body;
	}
}
