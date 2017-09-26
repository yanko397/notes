import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

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
}
