package fr.uga.l3miage.spring.tp3.mappers;


import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TestCenterMapper {

    TestCenterResponseDTO toResponse(TestCenterEntity testCenterEntity);

}
