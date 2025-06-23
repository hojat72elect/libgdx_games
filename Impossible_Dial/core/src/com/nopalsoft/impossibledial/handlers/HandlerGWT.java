package com.nopalsoft.impossibledial.handlers;

import com.badlogic.gdx.graphics.Texture;

public interface HandlerGWT {
	public void getTextureFromFacebook(String base64, OnTextureLoaded onTextureLoaded);

	public static interface OnTextureLoaded {
		public void onTextureLoaded(Texture texture);
	};
}
