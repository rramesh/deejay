## deejay - An automated DJ player for song requests

Download the latest release from [here](https://github.com/rramesh/deejay/releases)

### Prerequisites
1. A *nix / MacOS system - preferably Raspberry Pi
2. JRE 1.8+ installed
3. VLC app installed (cvlc in linux systems)
4. A Speaker connected to the above system, wired or through Bluetooth
5. Optionally slack account for Slack command integration

### Introduction
Deejay was developed as a fun project to automate the Friday afternoon DJ hour at work place. Employees would post youtube URL requests on a Slack channel and one of the employees would take charge of playing the role of a DJ to play requested songs in the order of request. This app was built to replace the manual requirement.

### How to run it
The application is a web service with a simple web page to post the request. It can also be called via API through curl or integrated with Slack (see Slack Integration section).

The bash script `play.sh` is present under `src/main/resources/script`. Download this [file](https://raw.githubusercontent.com/rramesh/deejay/master/src/main/resources/script/play.sh) to a location on the same server where the web service is running. Use browser's save as to save this file and ensure you modify the file permission to make it executable.

```
$chmod +x play.sh
```

The application appends requests to a playlist file on the system where the service runs. By default the location is `/tmp/deejay.txt` assuming `/tmp` is globally writeable. This property is defined in `application.properties` that is packaged as part of the application. In case this needs to be different location/file, the filename with path can be included as environment variable which overrides the path provided in the `application.properties` file. So it can be run as,

```
DEEJAY_PLAYLIST_FILE=~/deejay/playlist.txt java -jar deejay.jar
```

The application runs by default on port `9092`. It can be changed by specifying `service.port` property in `application.properties` or by setting `DEEJAY_SERVICE_PORT` environment variable before running or passing when running the application, like below.

```
DEEJAY_SERVICE_PORT=7833 java -jar deejay.jar
```

Once the application runs, start the player script (`play.sh`). For example on Mac OSX, like below.

```
$./play.sh /Applications/VLC.app/Contents/MacOS/VLC /tmp/deejay.txt
```

Please note, replace the vlc path with cvlc on linux systems and also if the playlist file is in a different path adjust that as well.
### Using the service
The service can simply be used on the browser by opening `http://<server ip>:9092/deejay/index.html`. Fill the name and Youtube URL and submit.

Alternatively it can be called via `curl` like below,

```
curl -d "name=Rama&youtubeUrl=http://youtu.be/XC32__32B" http://<server ip>:9092/deejay/play
```

### Slack Integration
Integrating the Deejay application with Slack makes it slick and makes the whole experience user friendly. This is relevant especially if your team uses Slack and you would want to host a DJ and songs played in the background. 
Since slack hook requires that Slack is able to communicate with deejay API from the internet to your local network, the following is a requirement.

**A domain/sub domain that can be routed to your internal network via port forwarding**
##### **OR**
**Use [ngrok](https://ngrok.com/) which provides public url for exposing local web server**


> Help on setting up either of the above is beyond the scope of this documentation. In either of the above options use caution to ensure you do not expose your internal network intentionally or unintentionally than what is required. Ensure you turn off the service when not in use

deejay provides a separate URL endpoint to support slack. Using the Slack's [slash command](https://api.slack.com/interactivity/slash-commands) it is quite easy to hook our deejay to Slack. Follow the below steps to enable the integration.

1. Login to your slack account in a browser. Open a new tab and open Slack API URL - <https://api.slack.com/apps>
2. Click `Create New App` button. In the model window that appears enter `deejay` for the App name and select your workspace from the dropdown. Click `Create App` button.
3. In the following page that appears, on the menu select `Slash Commands` and click `Create New Command` in the page that appears
4. In the form that appears fill the following,

   Command `/deejay`   
   Request URL `https://<your-domain.com>:23567/deejay/slackbot`   
   Short Description `A Cool DJ Service`   
   Usage Hint `Copy Paste Youtube URL`   
   Check the box that says `Escape channels, users, and links sent to your app`   
   
5. Click Save
6. Once the application is saved, from the menu on the left click `Install App`
7. That's it no more steps, you are ready to try deejay on slack

If everything went well, the output should look like below with a cool response from deejay service with link preview of the Youtube video posted.

![alt Sample Output](https://raw.githubusercontent.com/rramesh/deejay/master/src/main/resources/public/assets/sample_output.png)

Use deejay either from your own personal thread or in a channel or in a group. Using deejay in a channel or a group allows others to see what you have requested and can follow through the songs in queue or use the link to save for later listening.

### Caveats
1. The deejay web service and the bash script are mutually exclusive. The web service simply appends URL's to a file. The bash script simply tails the playlist file and picks up the latest entry and passes it to VLC. You could use the bash script to run an independently organized playlist.
2. The bash script may open the VLC app on the system where it runs but the parameters passed ensure that only audio is played. You could play around and use it to stream video and audio on a TV/projector.
3. The playlist file is not cleared upon every new start, so ensure if you want to start clean the playlist is cleared or the file removed or use a different playlist file every time you run.
4. There is no validation whatsoever of the URL's posted or the text submitted as URL. Also there is no check for duplicates.
 
