#!/bin/bash
display_usage() {
  echo "play <vlc executable optionally with path> <playlist file with path>"
  echo "Example on MacOS:"
  echo "-----------------"
  echo "play /Applications/VLC.app/Contents/MacOS/VLC /tmp/deejay.txt"
}

if [  $# -le 1 ]
	then
		display_usage
		exit 1
fi

EXECUTABLE=$1
PLAYLISTFILE=$2

tail -f $PLAYLISTFILE | while read -r p || [ -n "$p" ]
do
        echo "Now Playing $p"

        # Uncomment below to use cvlc/vlc
        $EXECUTABLE --no-osd --no-video --play-and-exit "$p" > /dev/null

        # Uncomment below to use mpv player
        # $EXECUTABLE --no-video "$p" > /dev/null
done