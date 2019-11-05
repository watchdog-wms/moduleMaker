package logic.util;

/**
 * Debugger class. It prints output only if debugmodus is set to true
 * @author Amrei Menzel
 *
 */
public class Debugger {

	public static boolean enable = false;

    public static boolean isEnabled() {
        return enable;
    }

    public static void log(Object o) {
        System.out.println(o.toString()); 
    }
}