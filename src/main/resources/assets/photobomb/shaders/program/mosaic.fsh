#version 110

uniform sampler2D DiffuseSampler;
uniform sampler2D MaskSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform float MosaicSize;

void main() {
    vec2 uv = texCoord;
    vec4 maskRaw = texture2D(MaskSampler, uv);
    gl_FragColor = maskRaw;
    //return;

    vec4 colorRaw = texture2D(DiffuseSampler, uv);
    if (colorRaw.a > 0.0) {
        vec2 mosaicInSize = InSize / MosaicSize;
        uv = floor(uv * mosaicInSize) / mosaicInSize;
        vec4 color = texture2D(DiffuseSampler, uv);
        gl_FragColor = vec4(color.rgb, 1.0);
    } else {
        gl_FragColor = vec4(colorRaw.rgb, 1.0);
    }
}
