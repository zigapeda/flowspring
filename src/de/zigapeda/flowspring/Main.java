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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Painter;
import javax.swing.UIManager;

import jxgrabkey.HotkeyConflictException;
import jxgrabkey.HotkeyListener;
import jxgrabkey.JXGrabKey;
import jxgrabkey.X11KeysymDefinitions;

import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import de.zigapeda.flowspring.controller.Database;
import de.zigapeda.flowspring.controller.Tagreader;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.data.Title;
import de.zigapeda.flowspring.gui.MainWindow;
import de.zigapeda.flowspring.gui.ReadWindow;
import de.zigapeda.flowspring.gui.Splash;

public class Main {
	private static final String DIRECTORY = "/.flowspring/";
	
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
			Main.setupVLC();
			Main.setupAppdataDir();
	        Main.setupLookandfeel();
//			new File(appdata + "flowspring.lck").delete();
//			new File(appdata + "flowspring.log").delete();
//			new File(appdata + "flowspring.properties").delete();
//			new File(appdata + "flowspring.script").delete();
	        Main.setupApplication(args);
			Main.setupMediakeylistener();
			Main.window.setVisible(true);
			Main.splash.setVisible(false);
	        Main.setupOpenlistener();
		} else {
			Main.createOpenfile(args);
		}
	}

	private static void setupVLC() {
		boolean arch64 = false;
		if(System.getProperty("os.arch").contains("64")) {
			arch64 = true;
		}
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			if(arch64) { 
				System.setProperty("jna.library.path", "lib/vlcwin64");
			} else {
				System.setProperty("jna.library.path", "lib/vlcwin32");
			}
		} else {
			if(arch64) { 
				System.setProperty("jna.library.path", "lib/vlclin64");
			} else {
				System.setProperty("jna.library.path", "lib/vlclin32");
			}
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
	
	private static void setupApplication(String[] args) {
        Main.database = new Database();
        Main.window = new MainWindow();
        if(Main.window.getControlllayout().getComponent(0) instanceof JComboBox<?>) {
        	((JComboBox<?>)Main.window.getControlllayout().getComponent(0)).setSelectedIndex(1);
        }
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
				Path dir = Paths.get(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/flowspring");
				WatchService watcher = dir.getFileSystem().newWatchService();
				dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
				WatchKey watchkey;
				watchkey = watcher.take();
				List<WatchEvent<?>> events = watchkey.pollEvents();
				for(WatchEvent<?> event : events) {
					if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						if(event.context().toString().equals("open.lock")) {
							File f = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/flowspring/open.lock");
							if(f.exists()) {
								BufferedReader br = new BufferedReader(new FileReader(f));
								String line = br.readLine();
								if(line.equals(Main.pid)) {
									while((line = br.readLine()) != null) {
										Title temp = new Tagreader(new File(line).toPath()).getTitle();
										Main.window.getPlaylist().addTrack(new PlaylistTrack(temp.getArtist() + " - " + temp.getName(), temp.getInt(), line));
									}
								}
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
			File of = new File(new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/flowspring/open.lock");
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
