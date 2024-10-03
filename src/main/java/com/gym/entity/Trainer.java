package com.gym.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "trainer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<Training> trainingList = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<TrainerTrainee> trainerTraineeList = new java.util.ArrayList<>();
}
