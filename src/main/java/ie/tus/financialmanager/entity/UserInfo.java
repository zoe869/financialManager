package ie.tus.financialmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table
@DynamicUpdate
public class UserInfo implements Serializable {


    private static final long serialVersionUID = 42L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String username;    // 用户名
    private String password;    // 密码
    private String realname;
    private String iconUrl;     // 图片路径
    private String signature;   // 个性签名
    private String verity;     // 验证码

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="house_id")
//    private House house;    // 所属家庭编号  系统管理员为null

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="role_id")
    private Role role;     // 所属角色id  1-系统管理员 2-家主 3-家庭成员

    @Transient
    private Integer roleid;

//    @OneToMany(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "user_id")
//    private Set<RecordTitle> recordTitles = new HashSet<>();

}


