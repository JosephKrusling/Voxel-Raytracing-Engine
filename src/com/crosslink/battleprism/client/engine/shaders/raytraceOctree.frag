#version 330

in vec2 colorCoord;

out vec4 outputColor;

uniform isamplerBuffer bufferTex;

uniform Projection
{
	mat4 view;
	mat4 projection;
	mat4 invViewProjection;
	ivec4 viewPort;
};


uniform Camera
{
vec3 cameraPos;
vec3 cameraForward;
vec3 cameraUp;
vec3 cameraRight;
vec2 viewPlaneApothem;
};


uniform vec3[8] octantIndex  = vec3[](               vec3(0, 0, 0),        // - - -
                                                   vec3( 0, 0, 1),        // - - +
                                                   vec3(0, 1, 0),        // - + -
                                                  vec3(0, 1, 1),        // - + +
                                                   vec3(1, 0, 0),    // + - -
                                                   vec3( 1, 0, 1),    // + - +
                                                   vec3( 1, 1, 0),    // + + -
                                                  vec3( 1, 1, 1) ); //+ + +



struct Ray
{
    vec3 pos;
    vec3 dir;
    vec3 invDir;
    bvec3 isPos;
    vec3 isPosf;
};
struct RayResult
{
    int facet;
    float distance;
};
struct BoundingBox
{
    float size;
    vec3 min;
};

uniform  vec3 rootChunkPosition = vec3(0,0,0);
uniform float rootChunkSize = 32;
uniform vec3 rootChunkMax = vec3(0,0,0) + vec3(64);
uniform int levelOffsett[8]  = int[](0,1,9,73, 73 + 8*8*8, 73 + 8*8*8 + 8*8*8*8, 73 + 8*8*8 + 8*8*8*8 + 8*8*8*8*8, 73 + 8*8*8 + 8*8*8*8 + 8*8*8*8*8 + 8*8*8*8*8*8);
uniform int levelWidthh[8]  = int[](1,2,4,8,16,32,64,128);
uniform float levelWidthInv[8]  = float[](1.0/1,1.0/2,1.0/4,1.0/8,1.0/16,1.0/32,1.0/64,1.0/128);
uniform float nodeSizeInvv[8]  = float[](8.0/float(1),8.0/float(2),8.0/float(4),8.0/float(8),8.0/float(16),8.0/float(32),8.0/float(64),8.0/float(128));


    void RayBoxIntersect(Ray ray, BoundingBox box, out float tmin, out float tmax, out vec3 tenter, out vec3 texit)
    {
                vec3 t1 = (box.min - ray.pos) * ray.invDir;
                vec3 t2 = ((box.min + box.size) - ray.pos) * ray.invDir;

                vec3 t12min = min(t1,t2);
                vec3 t12max = max(t1,t2);

                tmin = max(t12min.z,max(t12min.x, t12min.y));
                tmax = min(t12max.z,min(t12max.x, t12max.y));

                tenter = t12min; texit = t12max;

                return;

    }

    void getRayBoxIntersectMaxTime(Ray ray, BoundingBox box, out float tmax)
    {
                vec3 t1 = (box.min - ray.pos) * ray.invDir;
                vec3 t2 = t1 + (box.size * ray.invDir);
                vec3 t12max = max(t1, t2); //knowing if ray is negative may be able to make this faster cuz if it's negative then t2 is always min

                tmax = min(t12max.z,min(t12max.x, t12max.y));

                return;

    }

BoundingBox createChildBox(in BoundingBox parent, vec3 posSide) {
    BoundingBox newBox;
    newBox.size = parent.size/2;
    newBox.min = parent.min + posSide * newBox.size;
    return newBox;
}
//BoundingBox[8] createChildBox(in BoundingBox parent) {
//    BoundingBox newBox[8];
//    newBox.apothem = parent.apothem /2;
//    newBox.pos = parent.pos + (newBox.apothem * octantIndex[octant]);
//    newBox.min = newBox.pos - newBox.apothem; newBox.max = newBox.pos + newBox.apothem;
//    return newBox;
//}
BoundingBox createParentBox(in BoundingBox node, int octantOfParent) {
    BoundingBox newBox;
    newBox.size = node.size * 2;
    vec3 posSideOfParent = vec3(octantOfParent >> 2, (octantOfParent >> 1) & 1, octantOfParent & 1);
    newBox.min = node.min - (node.size * posSideOfParent);
    return newBox;
}
vec3 previousEnterTime(vec3 tenter, vec3 deltaTime, int octantOfParent, Ray ray) {
    bvec3 posSideOfParent = bvec3((octantOfParent >> 2) & 1, (octantOfParent >> 1) & 1, octantOfParent & 1);
     vec3 oldTenter = tenter - (deltaTime * vec3(equal(posSideOfParent, ray.isPos)));
     return oldTenter;
 }
void getNeighborPoint(Ray ray, inout vec3 relNodeTracePoint, inout float minTime, int level) {


        //vec3 timeToExit = distance(ray.isPosf, fract(relNodeTracePoint))*levelWidthInv[level]*rootChunkSize*ray.invDir

        vec3 distanceToExit = abs(ray.isPosf -fract(relNodeTracePoint));
        vec3 texit = abs(distanceToExit*ray.invDir); //not correct time to exit, but the exit axis minimum will be correct
        float exitTime = min(texit.x, min(texit.y, texit.z));
        minTime += exitTime / (1 << level) * rootChunkSize; //exitTime*levelWidthInv[level]*rootChunkSize;
        vec3 displacement = exitTime * ray.dir + sign(ray.dir) * 0.0001; //bias
        relNodeTracePoint = relNodeTracePoint + displacement;
    }
int convert3DcoordTo1D(ivec3 coord, int cubeWidth) {
     return int(coord.x * (cubeWidth * cubeWidth) + coord.y * cubeWidth + coord.z);
}
int getNewFetchLoc(vec3 relNodeTracePoint, int level) {
    return levelOffsett[level] + convert3DcoordTo1D(ivec3(relNodeTracePoint), 1 << level );
}

void main()
{

    vec3 viewPoint = vec3((colorCoord.x*2)-1,(colorCoord.y*2)-1, -1);
    viewPoint = vec3(invViewProjection * vec4(viewPoint, 1));
    Ray theRay;
    theRay.pos = cameraPos;
    theRay.dir = normalize(viewPoint - cameraPos);
    theRay.dir.x = theRay.dir.x == 0 ? 0.00001 : theRay.dir.x; //alg may break for 0 rays
    theRay.dir.y = theRay.dir.y == 0 ? 0.00001 : theRay.dir.y;
    theRay.dir.z = theRay.dir.z == 0 ? 0.00001 : theRay.dir.z;
    
    theRay.invDir = 1 / theRay.dir;
    theRay.isPos = greaterThan(theRay.dir, vec3(0));
    theRay.isPosf = vec3(theRay.isPos);
    float distance = 0;

    BoundingBox currentNode;
    currentNode.min = rootChunkPosition;
    currentNode.size = rootChunkSize;
    outputColor =  vec4(0);
    float tmin, tmax;
    vec3 tenter;
    vec3 texit;
    vec3 deltaTime = abs(vec3(currentNode.size) * theRay.invDir);
    RayBoxIntersect(theRay, currentNode, tmin, tmax, tenter, texit);
    distance = max(0, tmin);


    int fetch;
    int fetchLoc = 0;
    bool nullParent;


    int leafLevel = 5;
    int level = 0;

    vec3 relNodeTracePoint = ((theRay.pos + theRay.dir * (distance +0.001))- rootChunkPosition)/rootChunkSize;
    float nodeWidthInv = 1/rootChunkSize;

    int x = 0;
    while(distance +0.001 < tmax ) { //while loop is better than an unrolled for loop. Why is this?  //null parent, alg could not find an intersecting voxel
        fetch = texelFetch(bufferTex, fetchLoc / 8).r;
        bool fetchVal = ((fetch >> (fetchLoc%8)) & 1) == 1;
        x++;
        //if(x==10) {outputColor = vec4(1,1,1,1); break;}
        //make functoin that takes mindistance and the ray, computes the point, then gets the fetch loc based on the point and the level

        if(fetchVal) {

            if(level >= leafLevel ) {outputColor = vec4(floor(relNodeTracePoint)/(1 << level)*1,1); break;}
            level++;
            relNodeTracePoint *= 2;

        }
        else { //leave null node and get neighbor

            getNeighborPoint(theRay, relNodeTracePoint, distance, level); //bias cuz fp precision


        }
        fetchLoc = getNewFetchLoc(relNodeTracePoint, level);


    }
}