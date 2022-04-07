#version 440 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 inColor;

vec2 resolution = vec2(800.0, 600.0);

out vec4 myPosition;

void main(){
    gl_Position = position;
    myPosition = position;
}