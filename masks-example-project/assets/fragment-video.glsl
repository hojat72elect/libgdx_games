#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_mask;

varying vec4 v_color;
varying vec2 v_texCoord0;

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoord0);
    vec4 mask = texture2D(u_mask, v_texCoord0);
    texColor.a *= mask.r;
    gl_FragColor = v_color * texColor;
}