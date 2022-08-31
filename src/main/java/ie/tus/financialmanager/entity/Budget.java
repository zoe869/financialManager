package ie.tus.financialmanager.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;//

    private Integer type;//预算类型 1 百分比 2 金额
    private Double budget;//预算额度

    private Double income;//总收入

    private Double payment;//总支出

    private String time;//创建时间 前端传sysdate

    private String endDate;//通过设置时间推算
    private String startDate;//开始时间时间推算

    private String message;//提示消息<li>拼接

//    private String title;//使用title查询bills

    private Integer status;//使用title查询bills 0-init-save/1-alert-on effect/2-noticed-expired

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id")
    private UserInfo userInfo;

    @Transient
    private String title;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="record_title_id")
    private RecordTitle recordTitle;//

}
