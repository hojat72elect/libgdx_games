#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords;
varying vec4 v_color;

uniform sampler2D u_texture;
uniform vec2 u_direction;
uniform float u_radius;
uniform vec2 u_pixelSize;

void main()
{
    vec4 sum = vec4(0.0);

    // Number of pixels of the central pixel to sample from
    vec2 blur = u_radius / u_pixelSize.xy;

    // Apply blur using 9 samples and predefined gaussian weights
    sum += texture2D(u_texture, v_texCoords - 4.0 * blur * u_direction) * 0.006;
    sum += texture2D(u_texture, v_texCoords - 3.0 * blur * u_direction) * 0.044;
    sum += texture2D(u_texture, v_texCoords - 2.0 * blur * u_direction) * 0.121;
    sum += texture2D(u_texture, v_texCoords - 1.0 * blur * u_direction) * 0.194;

    sum += texture2D(u_texture, v_texCoords) * 0.27;

    sum += texture2D(u_texture, v_texCoords + 1.0 * blur * u_direction) * 0.194;
    sum += texture2D(u_texture, v_texCoords + 2.0 * blur * u_direction) * 0.121;
    sum += texture2D(u_texture, v_texCoords + 3.0 * blur * u_direction) * 0.044;
    sum += texture2D(u_texture, v_texCoords + 4.0 * blur * u_direction) * 0.006;

    gl_FragColor = v_color * sum;
}
