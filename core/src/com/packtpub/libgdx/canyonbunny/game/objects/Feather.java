package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

public class Feather extends AbstractGameObject {
	
	private TextureRegion regFeather;
	public boolean collected;
	
	
	/**
	 * Constructor that runs the init function
	 */
	public Feather(){
		init();
	}
	/**
	 * Initializes fields
	 */
	private void init() {
		dimension.set(0.5f, 0.5f);
		
		regFeather = Assets.instance.feather.feather;
		//Set bounding box for collision detection
		bounds.set(0,0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	/**
	 * Add the feather to the sprite batch to be drawn
	 */
	public void render(SpriteBatch batch){
		if (collected) return; //do not draw if collected
		
		TextureRegion reg = null;
		reg = regFeather;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	/**
	 * Return score value for this item
	 * @return int (250)
	 */
	public int getScore(){
		return 250;
	}
	
}
