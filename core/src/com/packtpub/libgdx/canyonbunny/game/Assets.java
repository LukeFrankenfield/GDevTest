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
	
	public AssetPoro poro;
	public AssetIce Ice;
	public AssetSnax snax;
	public AssetSuperS superS;
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
		poro = new AssetPoro(atlas);
		Ice = new AssetIce(atlas);
		snax = new AssetSnax(atlas);
		superS = new AssetSuperS(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
	}
	
	
	
	
	
	
	public class AssetPoro {
		public final AtlasRegion body;
		
		public AssetPoro (TextureAtlas atlas){
			body = atlas.findRegion("Poro");
		}
	}
	
	public class AssetIce {
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetIce (TextureAtlas atlas){
			edge = atlas.findRegion("Ice");
			middle = atlas.findRegion("Ice");
		}
	}
	
	public class AssetSnax {
		public final AtlasRegion snax;
		
		public AssetSnax (TextureAtlas atlas){
			snax = atlas.findRegion("snax");
		}
	}
	
	public class AssetSuperS {
		public final AtlasRegion superS;
		
		public AssetSuperS (TextureAtlas atlas){
			superS = atlas.findRegion("Super");
		}
	}
	
	public class AssetLevelDecoration {
		public final AtlasRegion cloud;
		public final AtlasRegion mountains;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration (TextureAtlas atlas){
			cloud = atlas.findRegion("Cloud");
			mountains = atlas.findRegion("Mountains");
			waterOverlay = atlas.findRegion("Water");
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
