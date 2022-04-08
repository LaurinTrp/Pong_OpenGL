#version 440 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 inColor;

uniform vec2 offset;
uniform mat4 model;

out vec4 color;

void main(){
    
    gl_Position = vec4(position.xy + offset.xy, position.zw);
    color = inColor;
}