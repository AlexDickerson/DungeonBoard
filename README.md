<A fork of McAJBen's DungeonBoard that I have modified to suit my tastes and needs as a DM.

Major changes:
  Stripped core functionality down to what was originally called the 'Paint' utility
      The Layer, Image, and Loading tools were removed as I had no use for them
  Added video playback, enabled by VLCJ, for the display of animated maps (see Dynamic Dungeons)
  Added ability to display 1 inch grid over either static of dynamic maps
  Added ability to display, scale, and move arbitrary images over displayed map
    These 'tokens' are intended for use with images of monsters but can display any image 
    Can be quickly scaled to 1 inch size to align with battle map
  Added DM screen feature to show a mirror image of what is displayed on the 'player' screen
    Inteded to enable easy manipulation of the token images without having to mouse over to the larger monitor/TV the players use
  Files are now selected via a file browser rather than being prelocated in folders; program generally requires no outside       folder/resources to run, though video playback requires VLC to be installed, still seeing that dependency can be eliminated
    Added initative tracker; player characters are stored in in json and loaded on startup and enemies are added when monster tokens are added. 
    Added ability to private message players on discord easily, requires addition of discord bot to server. I mainly use this so I dont have to tab over to discord to message them things
    
Quality of Life Changes
  Refactored code to suit my prefrences
  Reworked UI with darker, flat theme>
