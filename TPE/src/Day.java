public class Day {

    public static final Day LU = new Day(0);
    public static final Day MA = new Day(1);
    public static final Day MI = new Day(2);
    public static final Day JU = new Day(3);
    public static final Day VI = new Day(4);
    public static final Day SA = new Day(5);
    public static final Day DO = new Day(6);

    private static final Day days[] = {LU, MA, MI, JU, VI, SA, DO};
    private static final int TOTAL_DAYS = days.length;
    private final int daysIndex;

    private Day(int index) {
        daysIndex = index;
    }

    public Day getDay(String day) {
        switch (day) {
            case "Lu":
                return Day.LU;
            case "Ma":
                return Day.MA;
            case "Mi":
                return Day.MI;
            case "Ju":
                return Day.JU;
            case "Vi":
                return Day.VI;
            case "Sa":
                return Day.SA;
            case "Do":
                return Day.DO;
        }
        throw new IllegalArgumentException("Not valid day name");
    }

    public Day nextDay() {
        int i = (daysIndex + 1) % TOTAL_DAYS;
        return days[i];
    }

    public Day previousDay() {
        int i = daysIndex > 0 ? (daysIndex - 1) : TOTAL_DAYS - 1;
        return days[i];
    }

    public int getDaysDifference(Day d1, Day d2) { //TODO mejor nombre
        return Math.abs(d2.daysIndex - d1.daysIndex);
    }

    @Override
    public String toString() {
        switch (daysIndex) {
            case 0:
                return "Lu";
            case 1:
                return "Ma";
            case 2:
                return "Mi";
            case 3:
                return "Ju";
            case 4:
                return "Vi";
            case 5:
                return "Sa";
            case 6:
                return "Do";
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        return daysIndex == day.daysIndex;
    }

    @Override
    public int hashCode() {
        return daysIndex;
    }
}
