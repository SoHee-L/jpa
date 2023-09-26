package hellojpa;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity /*이걸 꼭 넣어야지 jpa가 인식하고 내가 관리해야되는 애라는것을 안다.*/
public class Member {
    @Id /*JPA에 PK가 뭔지 알려줘야되기 때문에 이것도 필수로 작성*/
    private Long id;
    private String name;

    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
} /*자바class에 작성해서 관리할 수 있다.*/