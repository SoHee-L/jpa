package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    //양방향 연관관계
    //연결에서 나는 뭐랑 연결되있지? 라는 의미(나는 team으로 맵핑이 되있는 애야)
    @OneToMany(mappedBy = "team") //mappedBy로 Member에 있는 team 변수랑 연결 일대다
    private List<Member> members = new ArrayList<>(); //이렇게 new ArrayList<>()로 초기화하는 게 관례 그래야 add할때 nullPointException이 뜨지 않음

    //Team에 member를 집어 넣을 경우. (둘중에 하나를 정하면 된다.)
    //Member와 Team 두개 모두 넣으면 최악의 경우 무한루프에 걸릴 수 있음.
//    public void addMember(Member member){
//        member.setTeam(this);
//        members.add(member);
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
