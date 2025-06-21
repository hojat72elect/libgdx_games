attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform mat3 u_normalMatrix;
varying vec3 v_normal;

uniform vec4 u_diffuseUVTransform;
varying vec2 v_diffuseUV;

#ifdef diffuseHeightTextureFlag
varying float v_worldHeight;
#endif

void main(void) {
    // Apply offsetU/V and scaleU/V to get texture coordinates
    v_diffuseUV = u_diffuseUVTransform.xy + a_texCoord0 * u_diffuseUVTransform.zw;

    // Convert the vertex normal to camera view space
    vec3 normal = normalize(u_normalMatrix * a_normal);
    v_normal = normal;

    // position
    vec4 worldPos = u_worldTrans * vec4(a_position, 1.0);

    #ifdef diffuseHeightTextureFlag
        v_worldHeight = worldPos.y;
    #endif

    gl_Position = u_projViewTrans * worldPos;
}
