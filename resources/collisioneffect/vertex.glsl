#version 440 core

layout(location = 0) in Vec4 position;
layout(location = 1) in Vec4 inColor;

out Vec4 myPosition;

void main(){
    gl_Position = position;
    myPosition = position;
}