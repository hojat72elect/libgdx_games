#ifdef GL_ES
#define LOW lowp
#define MED mediump
#define HIGH highp
precision highp float;
#else
#define MED
#define LOW
#define HIGH
#endif

// We always expect these values
varying vec2 v_diffuseUV;
varying vec3 v_normal;
uniform sampler2D u_diffuseBaseTexture;

#ifdef diffuseHeightTextureFlag
// Height
uniform sampler2D u_diffuseHeightTexture;
varying float v_worldHeight;
#endif

#ifdef diffuseSlopeTextureFlag
// Slope
uniform float u_minSlope;
uniform sampler2D u_diffuseSlopeTexture;
#endif

#ifdef lightingFlag
#if numDirectionalLights > 0
struct DirectionalLight
{
    vec3 color;
    vec3 direction;
};
uniform DirectionalLight u_dirLights[numDirectionalLights];
#endif // numDirectionalLights

#ifdef ambientLightFlag
uniform vec3 u_ambientLight;
#endif // ambientLightFlag
#endif // lighting flag

float normalizeRange(float value, float minValue, float maxValue) {
    float weight = max(minValue, value);
    weight = min(maxValue, weight);
    weight -= minValue;
    weight /= maxValue - minValue; // Normalizes to 0.0-1.0 range
    return weight;
}

void main(void) {
    vec4 diffuse = texture2D(u_diffuseBaseTexture, v_diffuseUV);

    #ifdef diffuseHeightTextureFlag
        // Height blending
        float minHeight = 5.0; // The world height blending begins
        float maxHeight = 15.0; // The world height where blending is 1.0

        float blend = normalizeRange(v_worldHeight, minHeight, maxHeight);
        diffuse = mix(diffuse, texture2D(u_diffuseHeightTexture, v_diffuseUV), blend);
    #endif

    #ifdef diffuseSlopeTextureFlag
        // Slope blending
        float minSlope = u_minSlope; // Higher == more slope texture visible
        float maxSlope = 0.99; // lower == less slope texture visible

        float slopeWeight = normalizeRange(v_normal.y, minSlope, maxSlope);
        diffuse = mix(texture2D(u_diffuseSlopeTexture, v_diffuseUV), diffuse, slopeWeight);
    #endif

    #ifdef lightingFlag
    vec3 diffuseLight = vec3(0);

        #if numDirectionalLights > 0
            // The dot product gives us the angle between the light direction and surface normal
            // The closer the dot product is to 1.0, the less the angle is between vectors, and the brighter the light
            // will be
            diffuseLight = u_dirLights[0].color * (dot(normalize(-u_dirLights[0].direction), v_normal));
        #endif

        #ifdef ambientLightFlag
            // ambient light
            diffuseLight += u_ambientLight;
        #endif

        // Apply light
        diffuse.rgb *= diffuseLight;

    #endif

    gl_FragColor = diffuse;
}