package fr.uga.l3miage.spring.tp3.controller;

import fr.uga.l3miage.spring.tp3.endpoints.TestCenterEndpoints;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import fr.uga.l3miage.spring.tp3.services.SessionService;


import fr.uga.l3miage.spring.tp3.services.TestCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TestCenterController implements TestCenterEndpoints {
    private final TestCenterService testCenterService;

    @Override
    public TestCenterResponseDTO addEtudiantInTestCenter(Long idTestcenter, Long idEtudiant) {
        return testCenterService.addEtudiantInTestCenter(idTestcenter,idEtudiant);
    }
}
