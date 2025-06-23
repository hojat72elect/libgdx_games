/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.nopalsoft.ponyrace.objetos;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	public final Vector3 position;
	public final Polygon bounds;

	
	public GameObject (float x,float y,float z,float[] vertices) {
		this.position = new Vector3(x, y,z);
		this.bounds = new Polygon(vertices);
	}
	
	public GameObject (float x,float y,float z) {
		this.position = new Vector3(x, y,0);
		this.bounds = new Polygon(new float[]{0,0,0,0,0,0});
	}
}
