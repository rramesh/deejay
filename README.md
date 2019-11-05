### deejay - An automated DJ player for song requests

#### Prerequisites
1. A *nix / MacOS system - preferably Raspberry Pi
2. VLC app installed (cvlc in linux systems)
3. A Speaker connected to the above system, wired or through Bluetooth
4. Optionally slack account for Slack command integration

#### Introduction
Deejay was developed as a side project to automate the Friday afternoon DJ hour at work place. Employees would post youtube URL requests on a Slack channel and one of the employees would take charge of playing the role of a DJ to play requested songs in the order of request. This app was built to replace the manual requirement.

#### Stack
1. Kotlin
2. Spark web framework
3. Dagger 2
4. Mockk with JUnit 5 for tests
5. Bash script for playing the files

#### How to run it
The application is a web service with a simple web page to post the request. It can also be called via API through curl or integrated with Slack (see Slack Integration section).

The bash script `play.sh` is present under `src/main/resources/script`. Download this file to a location on the same server where the web service is running.

The application appends requests to a playlist file on the system where the service runs. By default the location is `/tmp/deejay.txt` assuming `/tmp` is globally writeable. This property is defined in `application.properties` that is packaged as part of the application. In case this needs to be different location/file, the filename with path can be included as environment variable which overrides the path provided in the `application.properties` file. So it can be run as,

`
DEEJAY_PLAYLIST_FILE=~/deejay/playlist.txt bin/deejay
`

The application runs on port `9092`. Once the application runs starts the player script (`play.sh`). For example on Mac OSX, like below.

`
$./play.sh /Applications/VLC.app/Contents/MacOS/VLC /tmp/deejay.txt
`

Please note, replace the vlc path with cvlc on linux systems and also if the playlist file is in a different path adjust that as well.
#### Using the service
The service can simply be used on the browser by opening `http://<server ip>:9092/deejay/index.html`. Fill the name and Youtube URL and submit.

Alternatively it can be called via `curl` like below,

`
curl -d "name=Rama&youtubeUrl=http://youtu.be/XC32__32B" http://<server ip>:9092/deejay/play
`

#### Slack Integration
**_TODO - Document to be updated**_