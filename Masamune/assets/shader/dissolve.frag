#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_dissolve;
uniform vec2 u_uvOffset;
uniform vec2 u_atlasMaxUV;
uniform vec2 u_fragmentNumber;

void main() {
    // convert atlas UV to normal UV (0,0 .. 1,1)
    vec2 uvRange = u_atlasMaxUV - u_uvOffset;
    vec2 realUV = (v_texCoords - u_uvOffset) / uvRange;
    // we split the sprite into multiple cells (=fragmentNumber)
    // -> calculate pixel fractional of its cell location (=0.0 .. 0.99)
    vec2 pixelFract = fract(realUV * u_fragmentNumber);
    // get distance to its center
    float pixelDistance = distance(pixelFract, vec2(0.5, 0.5));
    // modify alpha value of fragment according to its distance of the center and dissolve value (=0..1)
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    gl_FragColor.a *= step(pixelDistance, 1.0 - u_dissolve);
}
