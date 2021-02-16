package de.structuremade.ms.actitvityservice.utils.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "activities")
@Getter
@Setter
public class Activities {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String text;

    @Column
    private boolean survey;

    @Column
    private int yes;

    @Column
    private int no;

    @Column
    private boolean deleteNextLesson;

    @Column
    private Date validThru;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "teacher", foreignKey = @ForeignKey(name = "fk_teacher"))
    private User user;

    @ManyToOne(targetEntity = LessonRoles.class)
    @JoinColumn(name = "lesson", foreignKey = @ForeignKey(name = "fk_lesson"))
    private LessonRoles lesson;

    @ManyToMany(targetEntity = Activities.class)
    @JoinTable(name = "userwatched",schema = "services", joinColumns = @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "fk_userid"))
            , inverseJoinColumns = @JoinColumn(name = "activity", foreignKey = @ForeignKey(name = "fk_activity")))
    List<Activities> watched;

}
