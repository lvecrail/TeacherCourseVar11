package VSUET.teachcours;

public class Course {
    private int id;
    private final String name;
    private final int hours;

    public Course(String name, int hours) {
        this.name = name;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getHours() {
        return hours;
    }


}
