#version 440 core

uniform sampler2D texSampler;

out Vec4 fragColor;

in Vec4 uv;

void main()
{
    Vec4 color = texture(texSampler, uv.st);
    if(color.x == 0.0 && color.y == 0.0, color.z == 0.0){
    	discard;
    }
    fragColor = color;
}
