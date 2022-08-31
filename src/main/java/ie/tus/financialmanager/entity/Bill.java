package ie.tus.financialmanager.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String title;

    private Double money;
    private String remark;
    private String time;
    private String startTime;
    private String endTime;


    private Integer typeid;   //账单类型： 1支出 2收入
    private String type;   //账单类型： 1 支出 2收入


    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id")
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "payway_id")
    private Payway payway;

//    @OneToOne(cascade = CascadeType.DETACH)
//    @JoinColumn(name = "house_id")
//    private House house;
}
