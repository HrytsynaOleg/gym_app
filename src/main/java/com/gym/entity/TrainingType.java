package com.gym.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@Builder
@NoArgsConstructor
@Table(name = "training_type")
public class TrainingType {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.ALL)
    private List<Trainer> trainerList;

}
