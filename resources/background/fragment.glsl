#version 440 core

uniform sampler2D texSampler;

uniform bool showTimer;

in Vec4 uvCoord;

out Vec4 fragColor;

void main() {
	if (showTimer) {
		Vec4 color = texture(texSampler, uvCoord.st);

		fragColor = color;
	}else{
		fragColor = Vec4(1.0);
	}
}
