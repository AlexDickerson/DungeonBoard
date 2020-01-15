package DungeonBoard.display;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class DisplayVideo extends EmbeddedMediaPlayerComponent{

    public DisplayVideo() { 
        final MediaPlayerEventListener mpListener = new MediaPlayerEventAdapter() {
            public void positionChanged(final MediaPlayer mediaPlayer, final float newPosition) {
                if (newPosition > .95) mediaPlayer.controls().setPosition(.01f);
            }
        };

        mediaPlayer().events().addMediaPlayerEventListener(mpListener);
    }

    public void playVideo(final String fileLocation) {
        if (!mediaPlayer().audio().isMute()) {
            mediaPlayer().audio().mute();
        }

        mediaPlayer().media().play(fileLocation);
    }    
    private static final long serialVersionUID = 1L;
}