#version 440 core

uniform sampler2D texSampler;

out vec4 fragColor;

in vec4 uv;

void main()
{
    vec4 color = texture(texSampler, uv.st);
    if(color.x == 0.0 && color.y == 0.0, color.z == 0.0){
    	discard;
    }
    fragColor = color;
}
