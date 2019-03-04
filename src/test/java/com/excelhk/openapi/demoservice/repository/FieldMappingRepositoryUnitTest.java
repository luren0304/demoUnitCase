package com.excelhk.openapi.demoservice.repository;

import com.excelhk.openapi.demoservice.MongoTestConfiguration;
import com.excelhk.openapi.demoservice.bean.FieldMapping;
import com.excelhk.openapi.demoservice.utils.constants.DemoConstants;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = { MongoTestConfiguration.class })
public class FieldMappingRepositoryUnitTest {

    @Autowired
    private FieldMappingRepository fieldMappingRepository;

    @Test
    public void whenFindAllByProductOrderByOrder_thenReturnFieldMappings(){
        // When
        List<FieldMapping>  fieldMappings = fieldMappingRepository.findAllByProductOrderByOrder(DemoConstants.PROD_TYPE_LOANS);

        // Then
        Assert.assertEquals(10, fieldMappings.size());
        MatcherAssert.assertThat(fieldMappings, IsCollectionWithSize.hasSize(10));
        int i = -1;
        for (FieldMapping fieldMapping : fieldMappings) {
            System.out.println(fieldMapping);
            Assert.assertEquals(++i, fieldMapping.getOrder());
        }
    }

    @Test
    public void whenFindAllByProductAndShowOrderByOrder_thenReturnFieldMappings(){
        // When
        List<FieldMapping>  fieldMappings = fieldMappingRepository.findAllByProductAndShowOrderByOrder(DemoConstants.PROD_TYPE_LOANS, false);

        // Then
        Assert.assertEquals(0, fieldMappings.size());
        MatcherAssert.assertThat(fieldMappings, IsCollectionWithSize.hasSize(0));
        int i = -1;
        for (FieldMapping fieldMapping : fieldMappings) {
            System.out.println(fieldMapping);
            Assert.assertEquals(++i, fieldMapping.getOrder());
        }

        // When
         fieldMappings = fieldMappingRepository.findAllByProductAndShowOrderByOrder(DemoConstants.PROD_TYPE_LOANS, true);

        // Then
        Assert.assertEquals(10, fieldMappings.size());
        MatcherAssert.assertThat(fieldMappings, IsCollectionWithSize.hasSize(10));
        i = -1;
        for (FieldMapping fieldMapping : fieldMappings) {
            System.out.println(fieldMapping);
            Assert.assertEquals(++i, fieldMapping.getOrder());
        }
    }
}