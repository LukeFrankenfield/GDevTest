/**
 * 
 */
package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;

/**
 * @author lukef_000
 *
 */
public class Assets implements Disposable, AssetErrorListener {
	
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();

	private AssetManager assetManager;
	private TextureAtlas atlas;

	private Assets() {
	}
	
	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;

	public void init(AssetManager assetManager){
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();
		
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "Asset:  " + a);
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()){
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//create game resource objects
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
	}
	
	
	
	
	
	
	public class AssetBunny {
		public final AtlasRegion head;
		
		public AssetBunny (TextureAtlas atlas){
			head = atlas.findRegion("bunny_head");
		}
	}
	
	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetRock (TextureAtlas atlas){
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	
	public class AssetGoldCoin {
		public final AtlasRegion goldCoin;
		
		public AssetGoldCoin (TextureAtlas atlas){
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}
	
	public class AssetFeather {
		public final AtlasRegion feather;
		
		public AssetFeather (TextureAtlas atlas){
			feather = atlas.findRegion("item_feather");
		}
	}
	
	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration (TextureAtlas atlas){
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.assets.AssetErrorListener#error(com.badlogic.gdx.assets.AssetDescriptor, java.lang.Throwable)
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '"+asset +"'", (Exception)throwable);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		assetManager.dispose();
	}

}