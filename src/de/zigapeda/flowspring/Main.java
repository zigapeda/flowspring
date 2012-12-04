package de.zigapeda.flowspring;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Painter;
import javax.swing.UIManager;

import jxgrabkey.HotkeyConflictException;
import jxgrabkey.HotkeyListener;
import jxgrabkey.JXGrabKey;
import jxgrabkey.X11KeysymDefinitions;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import de.zigapeda.flowspring.controller.Database;
import de.zigapeda.flowspring.controller.Settings;
import de.zigapeda.flowspring.controller.Tagreader;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.data.Title;
import de.zigapeda.flowspring.gui.MainWindow;
import de.zigapeda.flowspring.gui.ReadWindow;
import de.zigapeda.flowspring.gui.Splash;

public class Main {
	private static final String DIRECTORY = "/.flowspring/";
//	private static final String DIRECTORY = "/.flowspringdev/";
	
	private static Database database;
	private static MainWindow window;
	private static ReadWindow readwindow;
	private static JFrame ontopwindow;
	private static String pid;
	private static Splash splash;
	private static String appdata;

	public static void main(String[] args) {
		if(Main.checkInstance() == false) {
			Main.showSplash();
			Main.setupAppdataDir();
			Main.setupLookandfeel();
//			new File(appdata + "flowspring.lck").delete();
//			new File(appdata + "flowspring.log").delete();
//			new File(appdata + "flowspring.properties").delete();
//			new File(appdata + "flowspring.script").delete();
			Main.setupDatabase();
			Main.setupVLC();
	        Main.setupApplication();
			Main.setupMediakeylistener();
			Main.window.setVisible(true);
			Main.splash.setVisible(false);
			Main.checkArgs(args);
			Main.setupOpenlistener();
		} else {
			Main.createOpenfile(args);
		}
	}

	public static Connection getDatabase() {
		return Main.database.getConnection();
	}
	
	public static MainWindow getWindow() {
		return Main.window;
	}

	public static ReadWindow getReadWindow() {
		return Main.readwindow;
	}
	
	public static void setReadWindow(ReadWindow window) {
		Main.readwindow = window;
	}
	
	public static String getAppdata() {
		return Main.appdata;
	}
	
	private static boolean checkInstance() {
		File temppath = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + DIRECTORY);
		if(temppath.exists() == false) {
			temppath.mkdir();
		}
		File pidfile = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + DIRECTORY + "pid.lock");
		boolean running = false;
		if(pidfile.exists()) {
			String pid = null;
			String exec;
			try {
				BufferedReader br = new BufferedReader(new FileReader(pidfile));
				pid = br.readLine();
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				exec = System.getenv("windir") +"/system32/"+"tasklist.exe";
			} else {
				exec = "ps -e";
			}
			if(pid != null) {
			    try {
			        String line;
			        Process p = Runtime.getRuntime().exec(exec);
			        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        while ((line = input.readLine()) != null) {
			            if(line.contains(" " + pid + " ")) {
			            	running = true;
			            	Main.pid = pid;
			            }
			        }
			        input.close();
			    } catch (Exception err) {
			        err.printStackTrace();
			    }
			}
		}
		if(running == false) {
			String pid = ManagementFactory.getRuntimeMXBean().getName();
			Main.pid = pid.substring(0, pid.indexOf("@"));
			FileWriter fw;
			try {
				fw = new FileWriter(pidfile);
				fw.write(Main.pid);
				fw.close();
				pidfile.deleteOnExit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return running;
	}

	private static void showSplash() {
		Main.splash = new Splash();
	}
	
	private static void setupDatabase() {
        Main.database = new Database();
	}

	private static void setupVLC() {
		String vlcPath = Settings.loadSettings("vlc");
		if(vlcPath == null) {
			if(new NativeDiscovery().discover() == false) {
				if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
					int opt = JOptionPane.showConfirmDialog(null,
							"The player can't find an installation of VLC. May flowspring download VLC automatically?", "No VLC found",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(opt == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(null,
								"This can take a few minutes. flowspring will start automatically after the download is succeeded.", "Download VLC",
								JOptionPane.PLAIN_MESSAGE);
						if(downloadVlc() == true) {
							Settings.saveSettings("vlc", "vlc");
							System.setProperty("jna.library.path", "vlc");
						}
					} else {
						JFileChooser chooser = new JFileChooser(".");
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						if(chooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
							Settings.saveSettings("vlc", chooser.getSelectedFile().toString());
							System.setProperty("jna.library.path", chooser.getSelectedFile().toString());
						}
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"The player can't find an installation of VLC. May you select the path where VLC is installed.", "No VLC found",
							JOptionPane.PLAIN_MESSAGE);
					JFileChooser chooser = new JFileChooser(".");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if(chooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
						Settings.saveSettings("vlc", chooser.getSelectedFile().toString());
						System.setProperty("jna.library.path", chooser.getSelectedFile().toString());
					}
				}
			} else {
				Settings.saveSettings("vlc", "discovery");
			}
		} else {
			if(vlcPath.equals("discovery")) {
				new NativeDiscovery().discover();
			} else {
				System.setProperty("jna.library.path", vlcPath);
			}
		}
		try {
			LibVlcVersion.getVersion();
		} catch (Throwable e) {
			Settings.saveSettings("vlc", null);
			JOptionPane.showMessageDialog(null,
					"The player can't find an installation of VLC. Please restart flowspring to configure VLC again.", "No VLC found",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
	
	private static boolean downloadVlc() {
		final String url = "http://download.videolan.org/pub/videolan/vlc/last/win";
		try {
			URL u;
			if (System.getProperty("os.arch").contains("64")) {
				u = new URL(url + "64/");
			} else {
				u = new URL(url + "32/");
			}
			URLConnection uc = u.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String s = null;
			while ((s = br.readLine()) != null) {
				if (s.contains("zip</a>")) {
					int pos = s.indexOf("\"");
					if (pos > -1) {
						URL vlc = new URL(u.toString() + s.substring(pos + 1, s.indexOf("\"", pos + 1)));
						try {
							uc = vlc.openConnection();
							ReadableByteChannel rbc = Channels.newChannel(uc.getInputStream());
							FileOutputStream fos = new FileOutputStream("vlc.zip");
							fos.getChannel().transferFrom(rbc, 0, Long.valueOf(s.substring(s.lastIndexOf(" ") + 1)));
							fos.close();
							return extractVlc();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean extractVlc() {
		File destDir = new File("vlc");
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		
		ZipFile zipFile;
		try {
			zipFile = new ZipFile("vlc.zip");
			Enumeration<?> entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				
				String entryFileName = entry.getName();
				if (!entry.isDirectory()) {
					if(entryFileName.contains("plugins")) {
						String dest = "vlc/" + entryFileName.substring(entryFileName.indexOf("plugins"));
						int pos = dest.lastIndexOf("\\");
						if(pos == -1) {
							pos = dest.lastIndexOf("/");
						}
						File f = new File(dest.substring(0, pos));
						if(!f.exists()) {
							f.mkdirs();
						}
						extract(zipFile, entry, new File(dest));
					} else if(entryFileName.endsWith("libvlc.dll")) {
						extract(zipFile, entry, new File("vlc/libvlc.dll"));
					} else if(entryFileName.endsWith("libvlccore.dll")) {
						extract(zipFile, entry, new File("vlc/libvlccore.dll"));
					}
				}
			}
			zipFile.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void extract(ZipFile zipFile, ZipEntry entry, File file) {
		try {
			byte[] buffer = new byte[16384];
			int len;
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
			while ((len = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setupAppdataDir() {
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			Main.appdata = new File(System.getenv("appdata")).getAbsolutePath();
		} else {
			Main.appdata = new File(System.getProperty("user.home")).getAbsolutePath();
		}
		Main.appdata = Main.appdata + DIRECTORY;
		File f = new File(Main.appdata);
		if(f.exists() == false) {
			f.mkdir();
			Main.copyResource("libJXGrabKey32.so");
			Main.copyResource("libJXGrabKey64.so");
		}
	}
	
	private static void copyResource(String resname) {
		File file = new File(Main.appdata + resname); 
		if(!file.exists()) {
			InputStream in = Main.class.getClass().getResourceAsStream("/de/zigapeda/flowspring/res/" + resname);
			BufferedInputStream bufIn = new BufferedInputStream(in);
			BufferedOutputStream bufOut = null;
			try {
				bufOut = new BufferedOutputStream(new FileOutputStream(Main.appdata + resname));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			byte[] inByte = new byte[4096];
			int count = -1;
			try {
				while ((count = bufIn.read(inByte))!=-1) {
					bufOut.write(inByte, 0, count);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				bufOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bufIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void setupLookandfeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("Tree.collapsedIcon", new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/collapsed.png")));
            UIManager.put("Tree.expandedIcon", new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/expanded.png")));
            UIManager.put("Tree[Enabled].collapsedIconPainter", new Painter<JComponent>() {
				public void paint(Graphics2D g, JComponent object, int width,
						int height) {
					Path2D path = getCollapsedPath();
			        g.setPaint(new Color(0,0,0));
			        g.fill(path);
			        g.setPaint(new Color(0,255,0));
			        g.drawLine(3, 1, 3, 5);
			        g.drawLine(1, 3, 5, 3);
				}
			});
            UIManager.put("Tree[Enabled].expandedIconPainter", new Painter<JComponent>() {
				public void paint(Graphics2D g, JComponent object, int width,
						int height) {
					Path2D path = getExpandedPath();
			        g.setPaint(new Color(0,0,0));
			        g.fill(path);
			        g.setPaint(new Color(0,255,0));
			        g.drawLine(1, 3, 5, 3);
				}
			});
            UIManager.put("Tree[Enabled+Selected].collapsedIconPainter", new Painter<JComponent>() {
				public void paint(Graphics2D g, JComponent object, int width,
						int height) {
					Path2D path = getCollapsedPath();
			        g.setPaint(new Color(0,0,0));
			        g.fill(path);
			        g.setPaint(new Color(0,255,0));
			        g.drawLine(3, 1, 3, 5);
			        g.drawLine(1, 3, 5, 3);
				}
			});
            UIManager.put("Tree[Enabled+Selected].expandedIconPainter", new Painter<JComponent>() {
				public void paint(Graphics2D g, JComponent object, int width,
						int height) {
					Path2D path = getExpandedPath();
			        g.setPaint(new Color(0,0,0));
			        g.fill(path);
			        g.setPaint(new Color(0,255,0));
			        g.drawLine(1, 3, 5, 3);
				}
			});
            UIManager.put("control", new Color(32,32,32));
            UIManager.put("nimbusBase", new Color(16,16,16));
            UIManager.put("nimbusBlueGrey", new Color(32,32,32));
            UIManager.put("nimbusFocus", new Color(0,255,0));
            UIManager.put("nimbusLightBackground", new Color(32,32,32));
            UIManager.put("nimbusOrange", new Color(0,255,0));
            UIManager.put("nimbusSelectedText", new Color(0,0,0));
            UIManager.put("nimbusSelectionBackground", new Color(0,255,0));
            UIManager.put("Table.gridColor", new Color(32,32,32));
            UIManager.put("text", new Color(0,255,0));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private static Path2D getCollapsedPath() {
    	Path2D path = new Path2D.Float();
        path.reset();
        path.moveTo(0, 2);
        path.lineTo(0, 5);
        path.lineTo(2, 5);
        path.lineTo(2, 7);
        path.lineTo(5, 7);
        path.lineTo(5, 5);
        path.lineTo(7, 5);
        path.lineTo(7, 2);
        path.lineTo(5, 2);
        path.lineTo(5, 0);
        path.lineTo(2, 0);
        path.lineTo(2, 2);
        path.lineTo(0, 2);
        path.closePath();
        return path;
    }

    private static Path2D getExpandedPath() {
    	Path2D path = new Path2D.Float();
        path.reset();
        path.moveTo(0, 2);
        path.lineTo(7, 2);
        path.lineTo(7, 5);
        path.lineTo(0, 5);
        path.lineTo(0, 2);
        path.closePath();
        return path;
    }
	
	private static void setupApplication() {
        Main.window = new MainWindow();
        if(Main.window.getControlllayout().getComponent(0) instanceof JComboBox<?>) {
        	((JComboBox<?>)Main.window.getControlllayout().getComponent(0)).setSelectedIndex(1);
        }
	}
	
	private static void setupMediakeylistener() {
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			try {
				JIntellitype.getInstance().addIntellitypeListener(new IntellitypeListener() {
					public void onIntellitype(int acommand) {
						switch(acommand) {
							case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
								Main.getWindow().getPlayercontroller().play();
								break;
							case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
								Main.getWindow().getPlayercontroller().next();
								break;
							case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
								Main.getWindow().getPlayercontroller().previous();
								break;
							case JIntellitype.APPCOMMAND_MEDIA_STOP:
								Main.getWindow().getPlayercontroller().stop();
								break;
						}
					}
				});
			} catch(Exception e) {
				for(StackTraceElement b: e.getStackTrace()) {
					System.out.println(b.toString());
				}
			}
		} else {
			try {
				if(System.getProperty("os.arch").contains("64")) {
					System.load(new File(Main.appdata + "libJXGrabKey64.so").getCanonicalPath());
				} else if(System.getProperty("os.arch").contains("86") || System.getProperty("os.arch").contains("32")) {
					System.load(new File(Main.appdata + "libJXGrabKey32.so").getCanonicalPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				JXGrabKey.getInstance().registerX11Hotkey(0, 0, X11KeysymDefinitions.AUDIO_PLAY);
				JXGrabKey.getInstance().registerX11Hotkey(1, 0, X11KeysymDefinitions.AUDIO_PAUSE);
				JXGrabKey.getInstance().registerX11Hotkey(2, 0, X11KeysymDefinitions.AUDIO_NEXT);
				JXGrabKey.getInstance().registerX11Hotkey(3, 0, X11KeysymDefinitions.AUDIO_PREV);
				JXGrabKey.getInstance().registerX11Hotkey(4, 0, X11KeysymDefinitions.AUDIO_STOP);
			} catch (HotkeyConflictException e) {
				e.printStackTrace();
			}
			JXGrabKey.getInstance().addHotkeyListener(new HotkeyListener() {
				public void onHotkey(int id) {
					switch(id) {
						case 0:
						case 1:
							Main.getWindow().getPlayercontroller().play();
							break;
						case 2:
							Main.getWindow().getPlayercontroller().next();
							break;
						case 3:
							Main.getWindow().getPlayercontroller().previous();
							break;
						case 4:
							Main.getWindow().getPlayercontroller().stop();
							break;
					}
				}
			});
		}
	}

	private static void setupOpenlistener() {
		while (true) {
			try {
				Path dir = Paths.get(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + DIRECTORY);
				WatchService watcher = dir.getFileSystem().newWatchService();
				dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
				WatchKey watchkey;
				watchkey = watcher.take();
				List<WatchEvent<?>> events = watchkey.pollEvents();
				for(WatchEvent<?> event : events) {
					if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						if(event.context().toString().equals("open.lock")) {
							File f = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + DIRECTORY + "open.lock");
							if(f.exists()) {
								BufferedReader br = new BufferedReader(new FileReader(f));
								String line = br.readLine();
								if(line.equals(Main.pid)) {
									while((line = br.readLine()) != null) {
										Title temp = new Tagreader(new File(line).toPath()).getTitle();
										Main.window.getPlaylist().addTrack(new PlaylistTrack(temp.getArtist() + " - " + temp.getName(), temp.getInt(), line));
									}
								}
								br.close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void createOpenfile(String[] args) {
		if(args.length > 0) {
			File of = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + DIRECTORY + "open.lock");
			try {
				FileWriter fw = new FileWriter(of);
				fw.write(Main.pid + "\n");
				for(String s: args) {
					fw.write(s + "\n");
				}
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void checkArgs(String[] args) {
        if(args.length > 0) {
        	for(String s: args) {
        		File file = new File(s);
        		if(file.exists()) {
					Title temp = new Tagreader(file.toPath()).getTitle();
					Main.window.getPlaylist().addTrack(new PlaylistTrack(temp.getArtist() + " - " + temp.getName(), temp.getInt(), s));
        		}
        	}
        }
	}

	public static JFrame getOntopwindow() {
		if(ontopwindow != null) {
			if(ontopwindow.isShowing() == false) {
				ontopwindow = null;
			}
		}
		return ontopwindow;
	}

	public static void setOntopwindow(JFrame ontopwindow) {
		Main.ontopwindow = ontopwindow;
	}

}
