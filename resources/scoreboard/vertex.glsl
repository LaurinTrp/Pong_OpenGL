#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 texCoords;

uniform vec2 offset;

out vec4 uv;

void main()
{
    uv = texCoords;

    gl_Position = vec4(position.xy + offset.xy, position.zw);
}
