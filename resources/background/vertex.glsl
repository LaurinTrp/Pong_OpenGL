#version 440 core

layout (location=0) in vec4 position;
layout (location=1) in vec4 texCoord;

out vec4 uvCoord;
out vec4 myPosition;

void main(){
	gl_Position = position;
	uvCoord = texCoord;
}
