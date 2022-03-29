package sg.just4fun.tgasdk.modle;

public class UsersinputBean {
    private Integer team;
    private String role;
    private Integer userId;
    private String nickname;
    private String headerImg;
    private String userType;
    private String ticket;

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public UsersinputBean(Integer team, String role, Integer userId, String nickname, String headerImg, String userType, String ticket) {
        this.team = team;
        this.role = role;
        this.userId = userId;
        this.nickname = nickname;
        this.headerImg = headerImg;
        this.userType = userType;
        this.ticket = ticket;
    }
}
