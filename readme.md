SPSDK stand for Structural and Proxy SDK

It has 2 main objectives:

1°)
Help build an efficient application structure with a good mix between this ideas:
- Modifiable: if the needs changes it should be easy to adapt the code that was already written
- Ease of access: A new developer should not be lost too long when arriving, and be able to create to new feature fast
- Fast development: A new standard feature to develop should be sdk-guided to be extremely fast
- Adaptable: Every specific and unexpected feature should be possible to implement without hacks.


It comes with this pattern in mind:
- Losely coupling :  strong coupling is the ennemy of reusable, modifiable and adaptable code
- Less configuration as possible: config is the ennemy of code and of specificities



2°)
Provide access to always required functionnalities such as Network access, Image loading, Bus, serialization...

For the most efficiency related critical sdk ( I name query & image) this SDK should not force a mate-SDK but let the developer choose.
To do so, the SPSDK will expose only a proxy to the other SDK, it will not do anything itself.