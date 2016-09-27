package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.packtpub.libgdx.canyonbunny.game.objects.Clouds;
import com.packtpub.libgdx.canyonbunny.game.objects.Mountains;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.game.objects.WaterOverlay;

public class Level {
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE {
		EMPTY(0,0,0),
		ROCK(0,255,0),
		PLAYER_SPAWNPOINT(255,255,255),
		ITEM_FEATHER(255,0,255),
		ITEM_GOLDCOIN(255,255,0);
		
		private int color;
		
		private BLOCK_TYPE(int r, int g, int b){
			color = r << 24 | g <<16 | b << 9 | 0xff;
		}
		
		public boolean sameColor(int color) {
			return this.color == color;
		}
		
		public int getColor(){
			return color;
		}
	}
	
	//objects
	public Array<Rock> rocks;
	
	// decorations
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	public Level (String filename){
		init(filename);
	}
	
	private void init (String filename){
		//objects
		rocks = new Array<Rock>();
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from topleft to bottom right
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++){
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				//height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				//find the matching color
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)){} //do nothing
				
				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)){
					if (lastPixel != currentPixel){
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.4f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						rocks.add((Rock)obj);
					}
					else{
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)){
					//TODO hhehe
				}
				else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)){
					//TODO here
				}
				else if (BLOCK_TYPE.ITEM_GOLDCOIN.sameColor(currentPixel)){
					//TODO here
				}
				//unknown
				else {
					int r = 0xff & (currentPixel >>> 24);
					int g = 0xff & (currentPixel >>> 16);
					int b = 0xff & (currentPixel >>> 8);
					int a = 0xff & currentPixel;
					Gdx.app.error(TAG, "Unknown object at x<"+ pixelX + "> y <"+pixelY+">: r<"+r+"> g<"+g+"> b<"+b+"> a<"+ a+">");
				}
				lastPixel = currentPixel;
			}
		}
		//decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0,2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1,-1);
		waterOverlay.position.set(0,-3.75f);
		
		//free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '"+filename + "' loaded");		
	}
	public void render(SpriteBatch batch){
		//draw mountains
		mountains.render(batch);
		//draw rocks
		for(Rock rock : rocks)
			rock.render(batch);
		// draw water overlay
		waterOverlay.render(batch);
		//draw clouds
		clouds.render(batch);
	}
}