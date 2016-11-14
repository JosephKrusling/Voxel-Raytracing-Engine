Warning: This is an old project from a couple years back. You will find that this code is often lousy, inefficient, and poorly documented. We're still keeping it up because it's once of our favorite projects.
# BattlePrism
Battleprism is a voxel game engine framework and experimental ray-tracing renderer written principally in Java. This software is developed by Joseph Krusling (University of Cincinnati) and Thomas Skinner (Brandeis University). LWJGL skeleton kindly provided by integeruser.

#### Screenshots
Coming Soon (TM)

#### Rendering
Scenes are drawn by computing the intersections of per-pixel rays (sent from the camera) with the game world (represented as a sparse voxel octree). Our implementation of the sparse voxel octree allows us to compute these intersections very quickly, which allows us to render complicated scenes in real time.

###### Strategy
- If any geometry in a chunk (known as a RootNode) has changed, rebuild that chunk's graphics buffer by recursing through the chunk's children and encoding their information. This is inefficient but not an immediate concern.
- Send that graphics buffer to the GPU as a 1-D buffer texture
- Inside the fragment shader, use the data in that buffer to compute intersection with the SVO. We only need to recurse through the children that are intersected, so that saves a lot of time.
- When you've reached a leaf node (or you just get bored of recursing all day), return that node's color. If you wanted some nifty lighting effects (reflections, transparency, etc), you could do that here by computing secondary rays from the final point of intersection. If we were still working on this, that's something we would explore.

#### Networking
We experimented with Netty for our core networking. We chose this library because it looked the most scalable, and we were concerned that Java's thread overhead would cause problems when we had lots of connections. But we designed things so poorly here that the difference doesn't even matter. Whoops.

In its current state, the server is able to send out a root chunk to clients and broadcast to all clients whenever a voxel is destroyed. You can connect with multiple clients, destroy a block on one, and see the changes on all of them. Unfortunately, that's about it. The bones are there to make more interesting features like placing/editing voxels if you desired.

#### Technologies
LWJGL - Used for OpenGL bindings, graphics window, input.

Netty - Used for client-server communication, concurrent voxel manipulation across multiple clients.