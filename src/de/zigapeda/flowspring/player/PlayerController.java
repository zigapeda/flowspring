package de.zigapeda.flowspring.player;

import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.gui.Playlist;
import de.zigapeda.flowspring.gui.Progressbar;

public class PlayerController implements BasicPlayerListener {
	private Progressbar progressbar;
	private Playlist playlist;
	private Player player;
	private Integer bytelength;
	private String audioformat;
	private long milliseconds;
	
	public PlayerController(Progressbar progressbar, Playlist playlist) {
		this.progressbar = progressbar;
		this.playlist = playlist;
		this.player = new Player();
		this.player.addListener(this);
	}
	
	public void play() {
		if(this.player.getStatus() == BasicPlayer.STOPPED || this.player.getStatus() == BasicPlayer.UNKNOWN) {
			PlaylistTrack plt = this.playlist.getCurrent();
			if(plt != null) {
				player.play(plt.getPath());
				Main.getWindow().setPlaybuttonpause(true);
			} else {
				Main.getWindow().setPlaybuttonpause(false);
			}
			Main.getWindow().setTitle("flowspring - [" + plt.toString() + "]");
		} else if(this.player.getStatus() == BasicPlayer.PLAYING) {
			this.player.pause();
			Main.getWindow().setPlaybuttonpause(false);
		} else if(this.player.getStatus() == BasicPlayer.PAUSED) {
			this.player.play();
			Main.getWindow().setPlaybuttonpause(true);
		}
	}
	
	public void stop() {
		this.player.stop();
		Main.getWindow().setPlaybuttonpause(false);
		Main.getWindow().setTitle("flowspring");
	}
	
	public boolean isStopped() {
		if(this.player.getStatus() == BasicPlayer.UNKNOWN || this.player.getStatus() == BasicPlayer.STOPPED) {
			return true;
		}
		return false;
	}
	
	public void next() {
		this.player.stop();
		PlaylistTrack plt = this.playlist.getNext();
		if(plt != null) {
			player.play(plt.getPath());
			Main.getWindow().setPlaybuttonpause(true);
		} else {
			Main.getWindow().setPlaybuttonpause(false);
		}
	}
	
	public void previous() {
		this.player.stop();
		PlaylistTrack plt = this.playlist.getPrevious();
		if(plt != null) {
			player.play(plt.getPath());
			Main.getWindow().setPlaybuttonpause(true);
		} else {
			Main.getWindow().setPlaybuttonpause(false);
		}
	}

	public void seek(double percent) {
        long bytes = (long) Math.round(this.bytelength / percent);
		player.seek(bytes);
	}
	
	public void setGain(int gain) {
		double g;
		g = 100 - gain;
		if(g == 0.0) {
			g = 1.0;
		}
		this.player.setGain(Math.log(g)/Math.log(10)*-0.5+1);
	}
	
	public void setPan(int pan) {
		double p = 50;
		p = p - pan;
		this.player.setPan(p / -50);
	}

	@SuppressWarnings("rawtypes")
	public void opened(Object stream, Map properties) {
        if (properties != null) {
            if (properties.containsKey("audio.length.bytes")) {
                this.bytelength = (Integer) properties.get("audio.length.bytes");
                this.audioformat = (String) properties.get("audio.type");
                this.milliseconds = getTimeLengthEstimation(properties);
                this.progressbar.setDuration(this.milliseconds);
            }
        }
	}

	@SuppressWarnings("rawtypes")
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
		int secondsAmount = -1;
        long total = -1;
        float progress = -1.0f;
        if (total <= 0) {
        	total = (long) Math.round(this.milliseconds / 1000);
        }
        if (total <= 0) {
        	total = -1;
        }
        if ((bytesread > 0) && ((bytelength > 0))) {
        	progress = bytesread * 1.0f / bytelength * 1.0f;
        }
        if (this.audioformat.equalsIgnoreCase("mp3"))  {
            if (total > 0) {
            	secondsAmount = (int) (total * progress);
            } else {
            	secondsAmount = -1;
            }
        } else {
            secondsAmount = Math.round(microseconds / 1000000);
        }
		if (total > 0) {
			secondsAmount = (int) (total * progress);
		}
		
		this.progressbar.setProgress(total, secondsAmount);
	}

	public void stateUpdated(BasicPlayerEvent event) {
		if(event.getCode() == BasicPlayerEvent.EOM) {
			PlaylistTrack plt = this.playlist.getNext();
			if(plt != null) {
				player.play(plt.getPath());
			} else {
				Main.getWindow().setPlaybuttonpause(false);
			}
		}
	}

	public void setController(BasicController controller) {
		
	}

	@SuppressWarnings("rawtypes")
    public long getTimeLengthEstimation(Map properties)
    {
        long milliseconds = -1;
        int byteslength = -1;
        if (properties != null)
        {
            if (properties.containsKey("audio.length.bytes"))
            {
                byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();
            }
            if (properties.containsKey("duration"))
            {
                milliseconds = (int) (((Long) properties.get("duration")).longValue()) / 1000;
            }
            else
            {
                int bitspersample = -1;
                int channels = -1;
                float samplerate = -1.0f;
                int framesize = -1;
                if (properties.containsKey("audio.samplesize.bits"))
                {
                    bitspersample = ((Integer) properties.get("audio.samplesize.bits")).intValue();
                }
                if (properties.containsKey("audio.channels"))
                {
                    channels = ((Integer) properties.get("audio.channels")).intValue();
                }
                if (properties.containsKey("audio.samplerate.hz"))
                {
                    samplerate = ((Float) properties.get("audio.samplerate.hz")).floatValue();
                }
                if (properties.containsKey("audio.framesize.bytes"))
                {
                    framesize = ((Integer) properties.get("audio.framesize.bytes")).intValue();
                }
                if (bitspersample > 0)
                {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * channels * (bitspersample / 8)));
                }
                else
                {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * framesize));
                }
            }
        }
        return milliseconds;
    }

}
