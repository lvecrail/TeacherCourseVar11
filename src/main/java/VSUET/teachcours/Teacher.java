package VSUET.teachcours;

public class Teacher {
    private int id;
    private String fullName;
    private int age;

    public Teacher(String fullName, int age) {
        this.fullName = fullName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }


}
