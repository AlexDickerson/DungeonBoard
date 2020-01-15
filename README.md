<#
A fork of McAJBen's DungeonBoard that I have modified to suit my tastes and needs as a DM

### Changes Overview
#### Major changes
* Stripped core functionality down to what was originally called the 'Paint' utility The Layer, Image, and Loading tools were removed as I had no use for them 
* Added video playback, enabled by VLCJ, for the display of animated maps (see Dynamic Dungeons) 
* Added ability to display 1 inch grid over either static of dynamic maps 
* Added ability to display, scale, and move arbitrary images over displayed map These 'tokens' are intended for use with images of monsters but can display any image Can be quickly scaled to 1 inch size to align with battle map 
* Added DM screen feature to show a mirror image of what is displayed on the 'player' screen Inteded to enable easy manipulation of the token images without having to mouse over to the larger monitor/TV the players use 
* Files are now selected via a file browser rather than being prelocated in folders;  
* Added initative tracker; player characters are stored in in json and loaded on startup and enemies are added when monster tokens are added. 
* Added ability to private message players on discord easily, requires addition of discord bot to server. 

#### Quality of Life Changes 
* Refactored code to suit my prefrences 
* Reworked UI with darker, flat theme>
### Prerequisites

Java

VLC, for video playback

## Built With
* [JavaTuples](https://www.javatuples.org/) - Tuple data structures
* [FlatLaf](https://github.com/JFormDesigner/FlatLaf) - Flat UI themes
* [JDA](https://github.com/DV8FromTheWorld/JDA) - Discord Intergration
* [VLCJ](https://github.com/caprica/vlcj) - Video playback library
* [Maven](https://maven.apache.org/) - Dependency Management


## Acknowledgments

* Thanks to McAJBen for providing the framework here
>
# 
