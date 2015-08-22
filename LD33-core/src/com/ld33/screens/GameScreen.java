package com.ld33.screens;

import com.ld33.App;
import com.ld33.game.GameWorld;

public final class GameScreen extends BaseScreen {
	
	private GameWorld gameWorld;

	public GameScreen(final App app) {
		super(app);
	}
	
	protected void onShow() {
		gameWorld = new GameWorld(app);
	}
	
	@Override
	public void setSize(final float width,
						final float height) {
		
		super.setSize(width, height);
	}
	
}
