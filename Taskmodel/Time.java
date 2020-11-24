package Taskmodel;

public class Time {
	
	private long value;
	private static long tickPerSecond 	= 100000000;	//default 100MHz;
	private static long nanoseconds 	= 1000000000;	//nanoseconds in second
	
	/**
	 * Constructor to create a new time value 
	 * @param _value ticks 
	 */
	public Time(long _value) {
		value = _value;
	}
	
	/**
	 * Constructor to create a new time value
	 * @param _timeString time string with unit
	 */
	public Time(String _timeString) {
		String[] tmp = _timeString.split("(?<=\\d)\\s*(?=[a-zA-Z])");
		long tmpValue = Long.parseLong(tmp[0]);
		String unit = tmp[1];
		
		if(unit.toLowerCase().equals("ns")) 		tmpValue = tmpValue * 1;
		else if(unit.toLowerCase().equals("us")) 	tmpValue = tmpValue * 1000;
		else if(unit.toLowerCase().equals("ms")) 	tmpValue = tmpValue * 1000000;
		else if(unit.toLowerCase().equals("s")) 	tmpValue = tmpValue * 1000000000;
		else tmpValue = -1;
		
		if(tmpValue != -1) {
			value = (long)(Math.round((double)tmpValue * (double)tickPerSecond / (double)nanoseconds));
		}
	}
	
	/**
	 * Returns the current ticks per second value of the class
	 * @return
	 */
	public static long getTickPerSecond() {
		return Time.tickPerSecond;
	}
	
	/**
	 * Returns the current value in ticks
	 * @return
	 */
	public long getValue() {
		return value;
	}
	
	/**
	 * Set the ticks per second of the timebase for the class
	 * @param tickPerSecond
	 */
	public static void setTickPerSecond(long tickPerSecond) {
		Time.tickPerSecond = tickPerSecond;
	}
	
	/**
	 * Set the time value
	 * @param value
	 */
	public void setValue(long value) {
		this.value = value;
	}
	
	/**
	 * Returns the time string of the current value
	 * @return
	 */
	public String getTimeString() {
		String s = "";
		
		long timeValue = (long)Math.round(((double)value / (double) Time.tickPerSecond) * (double)1000000000);	//calculate the time in ns
		
		if(timeValue % 1000000000 == 0){	//s
			s = Long.toString(timeValue / 1000000000) + " s";
		}else if(timeValue % 1000000 == 0){	//ms
			s = Long.toString(timeValue / 1000000) + " ms";
		}else if(timeValue % 1000 == 0){	//us
			s = Long.toString(timeValue / 1000) + " us";
		}else{							//ns
			s = Long.toString(timeValue) + " ns";
		}
		
		return s;
	}
	
	/**
	 * Print the string representation of the time value
	 */
	@Override
	public String toString() {
		return this.getTimeString() + " (" + value + " ticks)";
	}
	
	/**
	 * Print the string representation of the time value without the ticks value
	 */
	public String toStringShort() {
		return this.getTimeString();
	}
	
	/**
	 * Set the clock speed according to the given string [MHz, GHz, Hz]
	 * @param input input string with unit
	 */
	public static void setClockSpeed(String input) {
		String[] tmp = input.split("(?<=\\d)\\s*(?=[a-zA-Z])");
		
		long value = Long.parseLong(tmp[0]);
		String unit = tmp[1];
		
		if (unit.equals("MHz"))	Time.tickPerSecond = value * 1000000;
		if (unit.equals("GHz"))	Time.tickPerSecond = value * 1000;
		if (unit.equals("Hz"))	Time.tickPerSecond = value * 1;
	}
	
	/**
	 * Computes the GCD for two long variables
	 * @param a
	 * @param b
	 * @return
	 */
	private static long gcd(long a, long b)
	{
	    while (b > 0)
	    {
	    	long temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	/**
	 * Computes the LCM of two long variables
	 * @param a
	 * @param b
	 * @return
	 */
	private static long lcm(long a, long b)
	{
	    return a * (b / gcd(a, b));
	}

	/**
	 * Computes the LCM for an array of Time objects
	 * @param input
	 * @return
	 */
	public static Time compLCM(Time[] _input)
	{
		long input[]= new long[_input.length];
		
		for (int i = 0; i < _input.length; i++) {
			input[i] = _input[i].getValue();
		}
		long result = input[0];
	    for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
	    
	    return new Time(result);
	}
	
	public static Time compGCD(Time[] _input) {
		long input[]= new long[_input.length];
		
		for (int i = 0; i < _input.length; i++) {
			input[i] = _input[i].getValue();
		}
		long result = input[0];
	    for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
	    
	    return new Time(result);
	}
	
	/**
	 * This creates a copy of the time value
	 * @return
	 */
	public Time assign() {
		return new Time(value);
	}
	
	public boolean largerThan(Time t) {
		if (value > t.getValue()) return true;
		else return false;
	}
	
	public boolean largerThanEqual(Time t) {
		if (value >= t.getValue()) return true;
		else return false;
	}
	
	public boolean smallerThan(Time t) {
		if (value < t.getValue()) return true;
		else return false;
	}
	
	public boolean smallerThanEqual(Time t) {
		if (value <= t.getValue()) return true;
		else return false;
	}
	
	public boolean equal(Time t) {
		if (value == t.getValue()) return true;
		else return false;
	}
}
