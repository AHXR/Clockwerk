# Clockwerk
![Preview](http://i.imgur.com/WYwiMVX.jpg)
Clockwerk is a Dota 2 rune and camp stacking timer. This program allows a user to keep track and to be reminded of any runes that are spawned or about to be spawned on the map. The user is allowed to set shortcut keys to calibrate the timer with the ingame timer. 

This program was designed to be used for beginner Dota 2 players who have a tendency to not pay attention to the current time. This habit commonly occurs with newer players as they are too focused on what's occuring on the map, thereby causing them to miss their stack times or forget that there is a rune that is about to be spawned.

Clockwerk does not have any memory reading or setting functionalities. Nothing is injected. This is a standalone timer that is toggled through the user's shortcut key.

The sounds are from http://www.dotasb.com/

# JAVA Features
![Preview](http://i.imgur.com/jq7M69k.png)

The settings allow the user to decide whether they want the timer to work for every bounty rune or camp time stack. Both can be toggled on or off. Even though that is the case, if the user wants to turn the timer off there is a mute shortcut key that can be adjusted in the "Shortcuts" menu. The Shortcut menu allows the user to input any key they want.

The user is also allowed set the warning intervals. They can choose to let the program warn you every 30 or 15 seconds. If the "On Time" option is selected, the notification will occur at the exact moment a rune or camp spawns.

The volume is adjusted through a simple scrollbar. The timer at the bottom left shows the current game time according to the application.

# JAVA Themes
![Preview](http://i.imgur.com/1E9zR0g.png)

In version 1.1, themes were introduced. Users can create themes and load them directly into Clockwerk. By default, I have included 3 themes (Default Clockwerk Theme, Crystal Maiden and Invoker). Below is an example from "crystal maiden.ini"

```ini
[theme]
background = cm/bg.jpg
sounds = /cm/sounds/
clock = #FF00FF
gameclock = #0000FF
```

**background** is the path where the background file is located. 

**sounds** is the path where you have stored your sound replacements. Make sure the files match the same pattern:
* start.wav - Timer toggled on
* end.wav - Timer toggled off
* trigger.wav - Timer hits 0:00
* warning.wav - Warning sound
* silence.wav - Mute sound
* unmute.wav - Unmute sound

**clock** - The color of the clock in hex.

**gameclock** - The color of the game clock in hex.

# WEB Features
I've uploaded a web version of Clockwerk that works exactly how the Java version works. Unfortunately, javascript cannot detect keys while the browser isn't focused. Therefore the hotkeys only work when the user presses the hotkey in the browser.

![Preview](http://i.imgur.com/dhaN9dQ.jpg)
![Preview](http://i.imgur.com/Cg7Sf8k.jpg)

![Preview](http://i.imgur.com/wmVgq1C.jpg)

# To-Do List

- [x] Themes
- [x] Save/Load Settings
- [ ] (Possible) Dota 2 Clock Memory Reader
- [ ] Shift/Control/Alt + Shortcut Key Combo
