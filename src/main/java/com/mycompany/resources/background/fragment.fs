#version 440 core

in vec4 myPosition;
out vec4 fragColor;

vec2 resolution = vec2(1.0, 1.25);

uniform vec2 collisionPosition;

uniform int player;
uniform bool wallCollision;

vec4 color;

void main(){
    vec2 uv = myPosition.xy / resolution;
    if(player != -1 || wallCollision){
        
        vec2 collisionPos = (collisionPosition/resolution);

        vec2 st = uv - collisionPos;

        float d = 1.0 * 0.08 / length(st);

        if(d < 0.1 || (player == 0 && uv.x < collisionPos.x)
            || (player == 1 && uv.x > collisionPos.x))
            discard;

        vec4 myColor = vec4(d);

        if(wallCollision){
            color = vec4(0.0f, 1.0f, 1.0f, 1.0f);
        }else{
            color = vec4(1.0f, 0.0f, 1.0f, 1.0f);
        }

        fragColor = myColor * color;
    }else{
        discard;
    }
}
