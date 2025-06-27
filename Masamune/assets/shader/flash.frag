#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec4 u_flashColor;
uniform float u_flashWeight;

void main()
{
    float weight = clamp(u_flashWeight, 0.0, 1.0);
    // get original color of texture
    gl_FragColor = texture2D(u_texture, v_texCoords);
    // mix the RGB values of the original color with the given flash color
    // we don't mix alpha value to avoid that transparent pixels become non-transparent
    gl_FragColor.rgb = mix(gl_FragColor.rgb, u_flashColor.rgb, weight);
    // mix some pixels' alpha that have a certain threshold
    if (gl_FragColor.a > 0.5) {
        gl_FragColor.a = mix(gl_FragColor.a, u_flashColor.a, weight);
    }
}
