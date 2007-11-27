/**
 * 
 */
package dataClass;

/**
 * @author Niels Thykier
 *
 */
public interface CourseSkemaData {

	public static class InternalSkema {
		public static int parseDTUskema(String[] DTUdata, String courseLength[]) {
			int data = 0;
			
			for(int i = 0; i < DTUdata.length ; i++ ){
				if(DTUdata[i].startsWith("E")) {
					data |= INTERNAL_SEASON_AUTUMN;
					data |= parseDTUskemaDay(DTUdata[i])<<12;
				} else if(DTUdata[i].startsWith("F")) {
					data |= INTERNAL_SEASON_SPRING;
					data |= parseDTUskemaDay(DTUdata[i]);
				}
			}
			
			if( courseLength[0] != null) {
				for(int i = 0 ; i < courseLength.length ; i++ ) {
					if(courseLength[i].equalsIgnoreCase("januar")) {
						data |= INTERNAL_SEASON_AUTUMN_SHORT;
					}else if(courseLength[i].equalsIgnoreCase("juni")) {
						data |= INTERNAL_SEASON_SPRING_SHORT;
					}
				}
			}
			
			return data;
		}
		
		public static String internalSkemaToExternString(int internalRepresentation) {
			int flag;
			String toReturn = "";
			for(int i = 0 ; i < 10 ; i++) {
				flag = 0x1 << i;
				if(0 != (internalRepresentation & flag)) {
					toReturn += "F" + Skema.intToEnum(i).getDTUplan() + " ";
				}
			}
			
			for(int i = 0 ; i < 10 ; i++) {
				flag = 0x1000 << i;
				if(0 != (internalRepresentation & flag)) {
					toReturn += "E" + Skema.intToEnum(i).getDTUplan() + " ";
				}
			}
			
			return toReturn;
		}
		
		public static int parseDTUskemaDay(String day) {
			Skema[] dat  = Skema.values();
			for(int j = 0 ; j < dat.length ; j++ ) {
				if(dat[j].isSameDTUSkema(day)) {
					return dat[j].getInteralRepresentation();
				}
			}
			return 0;
		}
	}

	public static final int INTERNAL_SEASON_SPRING_SHORT = 0x00000800;
	public static final int INTERNAL_SEASON_AUTUMN_SHORT = 0x00800000;
	public static final int INTERNAL_SEASON_SPRING = 0x00000400;
	public static final int INTERNAL_SEASON_AUTUMN = 0x00400000;
	public static final int INTERNAL_SEASON_ALL = 0x00C00C00;
	
	public static final int INTERNAL_SEASON_SPRING_DAYS = 0x000003ff;
	public static final int INTERNAL_SEASON_AUTUMN_DAYS = 0x003ff000;
	
	public static final int INTERNAL_MANDAG_FORMIDDAG = 0x00000001;
	public static final int INTERNAL_MANDAG_EFTERMIDDAG = 0x00000002;
	public static final int INTERNAL_TIRSDAG_FORMIDDAG = 0x00000004;
	public static final int INTERNAL_TIRSDAG_EFTERMIDDAG = 0x00000008;
	public static final int INTERNAL_ONSDAG_FORMIDDAG = 0x00000010;
	public static final int INTERNAL_ONSDAG_EFTERMIDDAG = 0x00000020;
	public static final int INTERNAL_TORSDAG_FORMIDDAG = 0x00000040;
	public static final int INTERNAL_TORSDAG_EFTERMIDDAG = 0x00000080;
	public static final int INTERNAL_FREDAG_FORMIDDAG = 0x00000100;
	public static final int INTERNAL_FREDAG_EFTERMIDDAG = 0x00000200;
	
	public static final int INTERNAL_ALL_DAYS = 0x000002ff;
	public static final int INTERNAL_ALL_SEASONS = 0x30000000;
	
	enum Skema {
	MANDAG_FORMIDDAG("1A", "Mandag Formiddag", INTERNAL_MANDAG_FORMIDDAG),
	MANDAG_EFTERMIDDAG("2A", "Mandag Eftermiddag", INTERNAL_MANDAG_EFTERMIDDAG),
	TIRSDAG_FORMIDDAG("3A", "Tirsdag Formiddag", INTERNAL_TIRSDAG_FORMIDDAG),
	TIRSDAG_EFTERMIDDAG("4A", "Tirsdag Eftermiddag", INTERNAL_TIRSDAG_EFTERMIDDAG),
	ONSDAG_FORMIDDAG("5A", "Onsdag Formiddag", INTERNAL_ONSDAG_FORMIDDAG),
	ONSDAG_EFTERMIDDAG("5B", "Onsdag Eftermiddag", INTERNAL_ONSDAG_EFTERMIDDAG),
	TORSDAG_FORMIDDAG("2B", "Torsdag Formiddag", INTERNAL_TORSDAG_FORMIDDAG),
	TORSDAG_EFTERMIDDAG("1B", "Torsdag Eftermiddag", INTERNAL_TORSDAG_EFTERMIDDAG),
	FREDAG_FORMIDDAG("4B", "Fredag Formiddag", INTERNAL_FREDAG_FORMIDDAG),
	FREDAG_EFTERMIDDAG("3B", "Fredag Eftermiddag",INTERNAL_FREDAG_EFTERMIDDAG);

	private String DTUplan;
	private String day;
	private int internal;
	
	Skema(String DTUplan, String day, int internal) {
		this.DTUplan = DTUplan;
		this.day = day;
		this.internal = internal;
	}
	
	public boolean isSameSkema(String Day) {
		return this.day.equalsIgnoreCase(Day);
	}
	
	public boolean isSameDTUSkema(String DTUplan) {
		if(DTUplan.length() == 3) {
			DTUplan = DTUplan.substring(1);
		}
		return this.DTUplan.equalsIgnoreCase(DTUplan);
	}
	
	public int getInteralRepresentation() { 
		return internal;
	}
	
	public String getDTUplan() {
		return DTUplan;
	}
	
	public String getDay() {
		return day;
	}
	
	
	public static Skema intToEnum(int ordinal) {
		Skema skema[] = Skema.values();
		if(ordinal < 0 || ordinal > skema.length)
			throw new IllegalArgumentException();
		
		return skema[ordinal];
	}
	
	}

}
