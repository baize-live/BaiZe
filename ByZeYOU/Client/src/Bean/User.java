package Bean;

public class User {
    private final String id;
    private String name;
    private String identity;
    private static User instance;
    private String imgPath;

    private User(String id) {
        this.id = id;
    }

    public static void createUser(String id) {
        instance = new User(id);
    }

    public static User getInstance() {
        return instance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentity() {
        return identity;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

