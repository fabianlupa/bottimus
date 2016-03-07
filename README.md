# bottimus [![Build Status](https://travis-ci.org/flaiker/bottimus.svg?branch=master)](https://travis-ci.org/flaiker/bottimus)

Bottimus is a music bot for [Discord](discordapp.com). It can play anything that the "Play network stream"-option in
VLC-player can play as it uses exactly that.

### Requirements
- Java 8 (tested with OracleJDK and OpenJDK, OpenJDK also needs OpenJFX as JDA somehow requires it)
- VLC player

### Installation
- Download the latest jar release from [releases](https://github.com/flaiker/bottimus/releases)
- Transfer it to your server or use it locally
- Run it `java -jar bottimus.jar`
- There should now be a generated configuration file next to it with default values. Stop the program and adjust it as
  necessary. You should specify the botuser you want to use and also join your guild / server beforehand.
- Rerun the bot. Tmux or screen are recommended.

### Usage
- `!hello` - Get a greeting in the text channel
- `!greeting` - Get a greeting in the voice channel
- `!play <url>` - Add a youtube video / radiostation or anything streamable by VLC to the playlist
- `!next` - Skip to the next song in the playlist
- `!previous` - Go to the previous song in the playlist
- `!volume <volume>` - Set the volume (advised between 0.0 and 1.0)
- `!mode <mode>` - Set the playlist mode (`normal` or `looping`)
- `!status` - Get the current status including the playlist

### Used third party libraries
| Name | Version |
|------|---------|
| [JDA](https://github.com/DV8FromTheWorld/JDA)  | 1.2.2_139 |
| [reflections.org](https://github.com/ronmamo/reflections) | 0.9.9 |
| [VLCJ](https://github.com/caprica/vlcj) | 3.8.0 |
