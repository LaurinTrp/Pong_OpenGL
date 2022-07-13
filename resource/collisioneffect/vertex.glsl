#version 440 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 inColor;

out vec4 myPosition;

void main(){
    gl_Position = position;
    myPosition = position;
}