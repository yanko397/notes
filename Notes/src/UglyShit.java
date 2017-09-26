import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class UglyShit {

	@SuppressWarnings("restriction")
	public static void behinderterHaesslicherKack() {
		long lhwnd = com.sun.glass.ui.Window.getWindows().get(0).getNativeWindow();
		Pointer lpVoid = new Pointer(lhwnd);
		HWND hwnd = new HWND(lpVoid);
		final User32 user32 = User32.INSTANCE;
		int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
		int newStyle = oldStyle | 0x00020000;// WS_MINIMIZEBOX
		user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
	}
	
	public static void restartApplication(String[] args) {
		
		try {
			StringBuilder cmd = new StringBuilder();
			cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
			
			for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments())
				cmd.append(jvmArg + " ");
			
			cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
			cmd.append(Main.class.getName()).append(" ");
			
			for (String arg : args)
				cmd.append(arg).append(" ");

			Runtime.getRuntime().exec(cmd.toString());
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR - restart failed");
		}
	}
}
