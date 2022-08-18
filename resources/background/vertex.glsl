#version 440 core

layout(location = 0) in Vec4 position;
layout(location = 1) in Vec4 texCoord;

out Vec4 uvCoord;
out Vec4 myPosition;

void main(){
	gl_Position = position;
	uvCoord = texCoord;
}
