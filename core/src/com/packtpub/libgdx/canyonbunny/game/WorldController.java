package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.game.objects.Feather;
import com.packtpub.libgdx.canyonbunny.game.objects.GoldCoin;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();
	
	//Private fields
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;
	
	//PUBLIC FIELDS
	public Level level;
	public int lives;
	public int score;
	
	//TEST SPRITE STUFF
	public CameraHelper cameraHelper;

	//Constructor
	public WorldController(){
		init();
	}
	private void init (){
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	
	public void update (float deltaTime){
		handleDebugInput(deltaTime);
		if(isGameOver()){
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0) init();
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()){
			lives--;
			if(isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}
	
	private void initLevel(){
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}
	
	/**
	 * On Collision with rock make bunny interact with it as if it were a solid material
	 * @param rock
	 */
	private void onCollisionBunnyWithRock(Rock rock){
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f){
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge){
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x  = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}
		
		switch (bunnyHead.jumpState) {
		case GROUNDED:
			break;
		case JUMP_FALLING:
		case FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y+bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}
	/**
	 * On collision with coin, make the coin disappear
	 * @param goldcoin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin){
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}
	/**
	 * On collision with Feather, gain ability to fly and make feather dissappear
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather){
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
		
	}
	
	private void testCollisions(){
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		//test collision bunnyhead <-> rock
		for(Rock rock : level.rocks)
		{	
			r2.set(rock.position.x,rock.position.y,rock.bounds.width,rock.bounds.height);
			if(!r1.overlaps(r2)) continue;
			// Test all rocks
			onCollisionBunnyWithRock(rock);
		}
		//Test collision <-> Gold coins
		for(GoldCoin goldCoin : level.goldCoins)
		{	
			if (goldCoin.collected) continue;
			r2.set(goldCoin.position.x,goldCoin.position.y,goldCoin.bounds.width,goldCoin.bounds.height);
			if(!r1.overlaps(r2)) continue;
			// Test all coins
			onCollisionBunnyWithGoldCoin(goldCoin);
		}
		// Test collision <-> Feather
		for(Feather feather : level.feathers)
		{	
			if (feather.collected) continue;
			r2.set(feather.position.x,feather.position.y,feather.bounds.width,feather.bounds.height);
			if(!r1.overlaps(r2)) continue;
			// Test all featers
			onCollisionBunnyWithFeather(feather);
		}
			
	}
	
	
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
		if (!cameraHelper.hasTarget(level.bunnyHead)){
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				moveCamera(-camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				moveCamera(camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				moveCamera(0, camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveCamera(0, -camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				cameraHelper.setPosition(0, 0);
			}
		}
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		}
		if (Gdx.input.isKeyPressed(Keys.COMMA)) {
			cameraHelper.addZoom(camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			cameraHelper.addZoom(-camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.SLASH)) {
			cameraHelper.setZoom(1);
		}	
	}
	
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	@Override
	public boolean keyUp (int keyCode)
	{
		//Reset Gameworld
		if(keyCode == Keys.R){
			init();
			Gdx.app.debug(TAG, "Game world Resetted");
		}
		else if (keyCode == Keys.ENTER){
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: "+cameraHelper.hasTarget());
		}
		return false;
	}
	
	
	private void handleInputGame (float deltaTime){
		if(cameraHelper.hasTarget(level.bunnyHead)){
			//player movement
			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			}
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				//Execute auto-forward movemnt on non-desktop platform
				if(Gdx.app.getType() != ApplicationType.Desktop) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}
			
			//BunnyJUMP
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.bunnyHead.setJumping(true);
			} else {
				level.bunnyHead.setJumping(false);
			}
		}
	}
	
	public boolean isGameOver(){
		return lives < 0;
	}
	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}
}
