#version 430 core

layout(location = 0) in Vec4 position;
layout(location = 1) in Vec4 texCoords;

uniform vec2 offset;

out Vec4 uv;

void main()
{
    uv = texCoords;

    gl_Position = Vec4(position.xy + offset.xy, position.zw);
}
