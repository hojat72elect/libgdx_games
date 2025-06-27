#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec4 u_outlineColor;
uniform vec2 u_pixelSize;

void main()
{
    // get alpha of surrounding pixels
    float surroundingA = 0.0;
    for (int x = -1; x<=1; x++) {
        for (int y = -1; y<=1; y++) {
            if (x==0 && y==0) {
                continue;
            }

            surroundingA += texture2D(u_texture, v_texCoords + vec2(x, y) * u_pixelSize).a;
        }
    }

    // if one of the surrounding pixels is transparent then this pixel will be an outline pixel
    vec4 pixel = texture2D(u_texture, v_texCoords);
    if (8.0 * pixel.a - surroundingA > 0.0) {
        gl_FragColor = u_outlineColor;
    } else {
        gl_FragColor = vec4(0.0);
    }
}
