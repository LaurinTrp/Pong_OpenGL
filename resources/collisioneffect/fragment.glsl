#version 440 core

in Vec4 myPosition;
out Vec4 fragColor;

vec2 resolution = vec2(1.0, 1.25);

uniform vec2 collisionPosition;

uniform int player;
uniform bool wallCollision;

Vec4 color;

void main(){
//	fragColor = Vec4(1.0);
    vec2 uv = myPosition.xy / resolution;
    if(player != -1){
        vec2 collisionPos = (collisionPosition/resolution);

        vec2 st = uv - collisionPos;

        float d = 1.0 * 0.08 / length(st);

        if(d < 0.1 || (player == 0 && uv.x < collisionPos.x)
            || (player == 1 && uv.x > collisionPos.x))
            discard;

        Vec4 myColor = Vec4(d);

        if(wallCollision){
            color = Vec4(0.0f, 1.0f, 1.0f, 1.0f);
        }else{
            color = Vec4(1.0f, 0.0f, 1.0f, 1.0f);
        }

        fragColor = myColor * color;
    }else{
        discard;
    }
}
