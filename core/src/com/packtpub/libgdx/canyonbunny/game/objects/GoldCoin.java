package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

public class GoldCoin extends AbstractGameObject {
	
	private TextureRegion regGoldCoin;
	
	public boolean collected;
	
	/**
	 * Empty constructor, runs init function to set up fields
	 */
	public GoldCoin(){
		init();
	}
	/**
	 * Initialize fields for this coin
	 */
	private void init(){
		dimension.set(0.5f, 0.5f);
		regGoldCoin = Assets.instance.goldCoin.goldCoin;
		
		// Set bounding box for colliosion detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	/**
	 * Add this sprite to the sprite batch for rendering. 
	 * @param batch the Sprite Batch that this is added to
	 */
	public void render(SpriteBatch batch){
		// Do not draw if collected
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regGoldCoin;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	/**
	 * Returns the value of collecting a coin
	 * @return 100
	 */
	public int getScore(){
		return 100;
	}
	
}
