Warning: This is an old project from a couple years back. You will find that this code is often lousy, inefficient, and poorly documented. We're still keeping it up because it's once of our favorite projects.
# BattlePrism
Battleprism is a voxel game engine framework and experimental ray-tracing renderer written principally in Java. This software was developed by Joseph Krusling (University of Cincinnati) and Thomas Skinner (Brandeis University). LWJGL skeleton provided by integeruser. Development is currently frozen.

#### Screenshots
Coming Soon (TM)

#### Rendering
Scenes are drawn by computing the intersections of per-pixel rays (sent from the camera) with the game world (represented as a sparse voxel octree). Our implementation of the sparse voxel octree allows us to compute these intersections very quickly, which allows us to render complicated scenes in real time.

###### Strategy
- If any geometry in a root chunk has changed, rebuild that chunk's graphics buffer by recursing through the chunk's children and encoding their information. This is inefficient but not an immediate concern.
- Send that graphics buffer to the GPU as a 1-D buffer texture
- Inside the fragment shader, use the data in that buffer to compute intersection with the SVO. We only need to recurse through the children that are intersected, so that saves a lot of time.
- When you've reached a leaf node (or you have reached an adequate depth), return that node's color. If you wanted some nifty lighting effects (reflections, transparency, etc), you could do that here by computing secondary rays from the final point of intersection. If we were still working on this, that's something we would explore.

#### Networking
We experimented with Netty for our core networking. We chose this library because it looked the most scalable, and we were concerned that Java's thread overhead would cause problems when we had lots of connections. But we designed things so poorly here that the difference doesn't even matter. Whoops.

In its current state, the server is able to send out a root chunk to clients and broadcast to all clients whenever a voxel is destroyed. You can connect with multiple clients, destroy a block on one, and see the changes on all of them. Unfortunately, that's about it. The bones are there to make more interesting features like placing/editing voxels if you desired.

#### Technologies
LWJGL - Used for OpenGL bindings, graphics window, input.

Netty - Used for client-server communication, concurrent voxel manipulation across multiple clients.

#### Hindsight
We chose Java for its portability and because we thought its performance was "good enough". We were wrong.

Our biggest obstacle on the software side of things was memory usage. Napkin math suggested that we'd need up to 4 GB of RAM just to store the world geometry at the detail we desired. We also wanted to be able to quickly load and deload chunks as the character moved or changed perspective. Being largely at the mercy of the garbage collector didn't help here. If we had access to proper pointers, we could have stored geometry more cleverly. We could also possibly access nodes with less recursion, and quickly discard chunks at will rather than wait on the GC.

If we wrote this again, we would write the core SVO implementation, networking, and engine in C/C++. If we made a game, we would probably embed something like LUA or Python for the actual game scripting.

