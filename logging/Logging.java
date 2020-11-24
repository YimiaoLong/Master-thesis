package logging;

public class Logging {

	private static Logging instance;
	private boolean state = false;
	
	private Logging() {
		state = true;
	}
	
	private static Logging getInstance() {
		if (instance == null) {
			instance = new Logging();
		}
		return instance;
	}
	
	public static void disable() {
		Logging log = Logging.getInstance();
		log.state = false;
	}
	
	public static void enable() {
		Logging log = Logging.getInstance();
		log.state = true;
	}
	
	public static void log(String _str) {
		Logging log = Logging.getInstance();
		if (log.state) System.out.println(_str);
	}
	
	public static void logIterations(int _iter, String _rep, String _str) {
		Logging log = Logging.getInstance();
		
		if (log.state) {
			for(int i = 0; i < (_iter * _rep.length()) - 1; i++) {
				System.out.print(" ");
			}
			if( _iter != 0 ) System.out.print("|" + _rep);
			System.out.println(_str);
		}
	}
}

