#version 440 core

uniform sampler2D texSampler;

uniform bool showTimer;

in vec4 uvCoord;

out vec4 fragColor;

void main() {
	if (showTimer) {
		vec4 color = texture(texSampler, uvCoord.st);

		fragColor = color;
	}else{
		fragColor = vec4(1.0);
	}
}
