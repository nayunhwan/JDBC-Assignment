/**
 * Created by nayunhwan on 2017. 6. 8..
 */
public class LoginStatus {
    String name;
    String grade;

    LoginStatus(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return this.name;
    }

    public String getGrade() {
        return this.grade;
    }
}
