#version 440 core

layout(location = 0) in Vec4 position;
layout(location = 1) in Vec4 inColor;

uniform vec2 offset;
uniform Mat4 model;

out Vec4 color;

void main(){
    
    gl_Position = Vec4(position.xy + offset.xy, position.zw);
    color = inColor;
}