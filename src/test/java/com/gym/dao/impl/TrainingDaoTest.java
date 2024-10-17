package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainingDao;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.utils.DateUtils;
import com.gym.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@RunWith(SpringRunner.class)
class TrainingDaoTest {
    private final ITrainingDao dao;
    private final TrainingModel serviceInputTrainingModel;

    @Autowired
    TrainingDaoTest(ITrainingDao dao) {
        this.dao = dao;
        this.serviceInputTrainingModel = JsonUtils.parseResource("training.json", new TypeReference<>() {
        });
    }

    @Test
    void createTrainingTest() {

        TrainingModel newTrainingModel = dao.create(serviceInputTrainingModel);
        TrainingModel testNewTrainingModel = dao.get(newTrainingModel.getId());

        assertNotNull(testNewTrainingModel);
        assertEquals(newTrainingModel.getId(), testNewTrainingModel.getId());
        assertEquals(serviceInputTrainingModel.getTrainerId(), testNewTrainingModel.getTrainerId());
        assertEquals(serviceInputTrainingModel.getTraineeId(), testNewTrainingModel.getTraineeId());
        assertEquals(serviceInputTrainingModel.getTrainingName(), testNewTrainingModel.getTrainingName());
        assertEquals(serviceInputTrainingModel.getTrainingType(), testNewTrainingModel.getTrainingType());
        assertEquals(serviceInputTrainingModel.getTrainingDate(), testNewTrainingModel.getTrainingDate());
        assertEquals(serviceInputTrainingModel.getDuration(), testNewTrainingModel.getDuration());
    }

    @Test
    void getTest() {
        TrainingModel trainingModel = dao.get(856);
        assertNotNull(trainingModel);
        assertEquals(856, trainingModel.getId());
        assertEquals(116, trainingModel.getTrainerId());
        assertEquals(234, trainingModel.getTraineeId());
        assertEquals("new training", trainingModel.getTrainingName());
        assertEquals(TrainingTypeEnum.YOGA, trainingModel.getTrainingType());
        assertEquals(LocalDate.of(2024, 9, 12), trainingModel.getTrainingDate());
        assertEquals(20, trainingModel.getDuration());
    }

    @Test
    void getIfNotExistTest() {
        TrainingModel trainingModel = dao.get(180);
        assertNull(trainingModel);
    }

    @Test
    void getTrainerTrainingListByParametersTest() {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDateFrom = LocalDate.of(2024, 9, 11);
        LocalDate localDateTo = LocalDate.of(2024, 9, 15);

        parameters.put("trainer", "Kerry.King");
        parameters.put("trainee", "Bruce.Dickinson");
        parameters.put("startDate", DateUtils.localDateToDate(localDateFrom));
        parameters.put("endDate", DateUtils.localDateToDate(localDateTo));
        List<TrainingModel> resultList = dao.getTrainerTrainingListByParameters(parameters);

        assertEquals(2, resultList.size());
    }

    @Test
    void getTraineeTrainingListByParametersTest() {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDateFrom = LocalDate.of(2024, 9, 11);
        LocalDate localDateTo = LocalDate.of(2024, 9, 15);

        parameters.put("trainer", "Kerry.King");
        parameters.put("trainee", "Bruce.Dickinson");
        parameters.put("startDate", DateUtils.localDateToDate(localDateFrom));
        parameters.put("endDate", DateUtils.localDateToDate(localDateTo));
        parameters.put("trainingType", 10);
        List<TrainingModel> resultList = dao.getTraineeTrainingListByParameters(parameters);

        assertEquals(2, resultList.size());
    }
}