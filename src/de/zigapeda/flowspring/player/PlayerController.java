package de.zigapeda.flowspring.player;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.gui.Playlist;
import de.zigapeda.flowspring.gui.Progressbar;

public class PlayerController implements MediaPlayerEventListener {
	private Progressbar progressbar;
	private Playlist playlist;
	private Integer bytelength;
	private String audioformat;
	private long milliseconds;
	private EmbeddedMediaPlayerComponent empc;
	
	public PlayerController(Progressbar progressbar, Playlist playlist, EmbeddedMediaPlayerComponent empc) {
		this.progressbar = progressbar;
		this.playlist = playlist;
		this.empc = empc;
		this.empc.getMediaPlayer().addMediaPlayerEventListener(this);
	}
	
	public void play() {
		if(this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_NothingSpecial 
				|| this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Ended
				|| this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Stopped) {
			PlaylistTrack plt = this.playlist.getCurrent();
			if(plt != null) {
				this.empc.getMediaPlayer().playMedia(plt.getPath());
				Main.getWindow().setPlaybuttonpause(true);
			} else {
				Main.getWindow().setPlaybuttonpause(false);
			}
			Main.getWindow().setTitle("flowspring - [" + plt.toString() + "]");
		} else if(this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Playing) {
			this.empc.getMediaPlayer().pause();
			Main.getWindow().setPlaybuttonpause(false);
		} else if(this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Paused) {
			this.empc.getMediaPlayer().play();
			Main.getWindow().setPlaybuttonpause(true);
		}
	}
	
	public void stop() {
		this.empc.getMediaPlayer().stop();
		Main.getWindow().setPlaybuttonpause(false);
		Main.getWindow().setTitle("flowspring");
	}
	
	public boolean isStopped() {
		if(this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_NothingSpecial 
				|| this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Ended
				|| this.empc.getMediaPlayer().getMediaPlayerState() == libvlc_state_t.libvlc_Stopped) {
			return true;
		}
		return false;
	}
	
	public void next() {
		this.empc.getMediaPlayer().stop();
		PlaylistTrack plt = this.playlist.getNext();
		if(plt != null) {
			this.empc.getMediaPlayer().playMedia(plt.getPath());
			Main.getWindow().setPlaybuttonpause(true);
		} else {
			Main.getWindow().setPlaybuttonpause(false);
		}
	}
	
	public void previous() {
		this.empc.getMediaPlayer().stop();
		PlaylistTrack plt = this.playlist.getPrevious();
		if(plt != null) {
			this.empc.getMediaPlayer().playMedia(plt.getPath());
			Main.getWindow().setPlaybuttonpause(true);
		} else {
			Main.getWindow().setPlaybuttonpause(false);
		}
	}

	public void seek(double percent) {
//        long bytes = (long) Math.round(this.bytelength / percent);
//		player.seek(bytes);
	}
	
	public void setGain(int gain) {
//		double g;
//		g = 100 - gain;
//		if(g == 0.0) {
//			g = 1.0;
//		}
//		this.player.setGain(Math.log(g)/Math.log(10)*-0.5+1);
	}
	
	public void setPan(int pan) {
//		double p = 50;
//		p = p - pan;
//		this.player.setPan(p / -50);
	}

	@Override
	public void backward(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void buffering(MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void endOfSubItems(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void error(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void finished(MediaPlayer arg0) {
		PlaylistTrack plt = this.playlist.getNext();
		if(plt != null) {
			this.empc.getMediaPlayer().playMedia(plt.getPath());
		} else {
			Main.getWindow().setPlaybuttonpause(false);
		}
	}

	@Override
	public void forward(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void lengthChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaFreed(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void newMedia(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void opening(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pausableChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void paused(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void playing(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void positionChanged(MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void seekableChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void snapshotTaken(MediaPlayer arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void stopped(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void subItemFinished(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void subItemPlayed(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void timeChanged(MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void titleChanged(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void videoOutput(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}

}
