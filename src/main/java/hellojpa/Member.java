package hellojpa;

import javax.persistence.*;

@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")//직접적어줌 멤버는 멤버랑 팀을 레퍼런스로 가져가야되는데 그게아닌 DB에 맞춰서 모델링 한것이기 때문
//    private Long teamId;

    //객체지향주의
    //Team과 Member의 관계가 일대일인지 다대일인지 알려줘야됨 Member -> N/ Team -> 1
    @ManyToOne
    @JoinColumn(name = "TEAM_ID") //조인하는 컬럼 맵핑
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team){
        this.team = team;
    }

}