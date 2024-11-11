package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
class TrainerDaoTest {

    private final ITrainerDao dao;
    private final TrainerModel serviceInputTrainerModel;

    @Autowired
    TrainerDaoTest(ITrainerDao dao) {
        this.dao = dao;
        this.serviceInputTrainerModel = JsonUtils.parseResource("trainer.json", new TypeReference<>() {
        });
    }

    @Test
    @DirtiesContext
    void createTrainerTest() {
        TrainerModel newTrainer =  dao.create(serviceInputTrainerModel);
        TrainerModel checkTrainer = dao.get(newTrainer.getId());
        assertNotNull(checkTrainer);
        assertEquals(serviceInputTrainerModel.getFirstName(), checkTrainer.getFirstName());
        assertEquals(serviceInputTrainerModel.getLastName(), checkTrainer.getLastName());
        assertEquals(serviceInputTrainerModel.getUserName(), checkTrainer.getUserName());
        assertEquals(serviceInputTrainerModel.getPassword(), checkTrainer.getPassword());
        assertEquals(serviceInputTrainerModel.getIsActive(), checkTrainer.getIsActive());
        assertEquals(serviceInputTrainerModel.getTrainingType(), checkTrainer.getTrainingType());
    }

    @Test
    void getTrainerModelByIdTest(){
        TrainerModel trainerModel = dao.get(116L);
        assertNotNull(trainerModel);
        assertEquals("Kerry", trainerModel.getFirstName());
        assertEquals("King", trainerModel.getLastName());
        assertEquals("Kerry.King", trainerModel.getUserName());
        assertTrue(trainerModel.getIsActive());
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());
    }

    @Test
    void getTrainerIfNotExistTest(){
        assertThrows(NoSuchElementException.class, () -> dao.get(2L));
    }


    @Test
    void getTrainerByUserNameTest(){
        TrainerModel trainerModel = dao.getByUserName("Kerry.King");
        assertNotNull(trainerModel);
        assertEquals("Kerry", trainerModel.getFirstName());
        assertEquals("King", trainerModel.getLastName());
        assertEquals("Kerry.King", trainerModel.getUserName());
        assertTrue(trainerModel.getIsActive());
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());
    }

    @Test
    void getTrainerByUserNameIfNotExistTest(){
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> dao.getByUserName("112"));
    }

    @Test
    void updateTrainerTest(){
        TrainerModel trainerModel = dao.getByUserName("Dave.Lombardo");
        trainerModel.setFirstName("Jan");
        trainerModel.setLastName("Holm");
        dao.update(trainerModel);
        TrainerModel updatedTrainer = dao.getByUserName("Dave.Lombardo");

        assertEquals("Jan", updatedTrainer.getFirstName());
        assertEquals("Holm", updatedTrainer.getLastName());
    }

    @Test
    void getNotAssignedTrainerList(){
        List<TrainerModel> notAssignedTrainerList = dao.getNotAssignedTrainerList();
        assertEquals(1, notAssignedTrainerList.size());
    }

    @Test
    void getAssignedTraineeList(){
        List<TraineeModel> assignedTraineeList = dao.getAssignedTraineeList(117);
        assertEquals(3, assignedTraineeList.size());
    }
}