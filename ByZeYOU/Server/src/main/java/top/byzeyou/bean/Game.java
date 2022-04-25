package top.byzeyou.bean;

public class Game {
    private String GID;
    private String name;
    private String icon;
    private String installer;
    private String introduction;
    private String createTime;
    private String modifyTime;

    public Game(String name, String icon, String installer, String introduction) {
        this.name = name;
        this.icon = icon;
        this.installer = installer;
        this.introduction = introduction;
    }

    public String getGID() {
        return GID;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getInstaller() {
        return installer;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Game{" +
                "GID='" + GID + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", installer='" + installer + '\'' +
                ", introduction='" + introduction + '\'' +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
