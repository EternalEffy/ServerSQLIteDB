public class User {
    private String username;
    private String age, score, level;

    public User(String username,String age, String score, String level){
        this.username=username;
        this.age=age;
        this.score=score;
        this.level=level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String requestAdd(){
        return "INSERT INTO 'users' ('username', 'age', 'score', 'level') VALUES ('"+username+"', '"+age+"', '"+score+"', '"+level+"'); ";
    }

    public String requestGetId(){
        return null;
    }

    public String requestGetName(){
        return null;
    }

    public String requestEdit(){
        return null;
    }
}

