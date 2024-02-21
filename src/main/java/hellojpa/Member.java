package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "MBR") //DB 테이블 MBR이라는 테이블이랑 맵핑을 함 그래서 실제 INSERT할때 INSERT INTO NBR로 나간다.
public class Member {
    @Id
    private Long id;
    //객체에는 username이라고 쓰고싶은데 DB에는 name이라고 써야되면 @Column(name = "name") 사용
    @Column(name = "name", nullable = false)
    private String username;

    private Integer age;
    //JPA 객체에서 Enum 타입을 쓰고 싶으면 @Enumerated 사용
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP) //자바의 DATETYPE은 날짜, 시간 다있지만 DB에는 DATE / TIME / TIMESTAMP 날짜, 시간, 날짜 시간으로 세가지로 구분해서 쓴다.
    private Date lastModifiedDate;

    //최신 버전은 이렇게 두개처럼 사용하고 과거 버전을 써야하고 데이트 타입을 써야하면
    //@Temporal Date 타입을 사용.
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob //DB에 VARCHAR를 넘어서는 큰 컨텐츠를 쓰고 싶으면 @Lob을 쓴다.
    private String description;

    public Member(){
    }

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}