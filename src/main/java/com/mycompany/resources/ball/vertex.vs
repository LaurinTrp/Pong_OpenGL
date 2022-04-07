#version 440 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 inColor;

uniform vec2 offset;
uniform mat4 model;
uniform vec4 myColor;

out vec4 color;

void main(void){
    
    gl_Position = vec4(position.xy + offset.xy, position.zw);
    color = myColor;
}