package de.structuremade.ms.actitvityservice.utils.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lessonsubstitutes", schema = "services", indexes = {
        @Index(name = "id_lessonrolesid", columnList = "id", unique = true)})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonSubstitutes {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String substituteRoom;

    @Column(name = "dateofsubstitute")
    private Date date;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "substitute_teacher", foreignKey = @ForeignKey(name = "fk_substituteTeacherid"))
    private User substituteTeacher;

    @ManyToOne(targetEntity = School.class)
    @JoinColumn(name = "school")
    private School school;

    @ManyToOne
    @JoinColumn(name = "editor", foreignKey = @ForeignKey(name = "fk_userid"))
    private User user;
}
