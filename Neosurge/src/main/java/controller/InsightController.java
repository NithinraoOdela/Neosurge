package controller;

import model.UserPreference;
import service.GPTService;
import service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InsightController {

    @Autowired
    private GPTService gptService;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @PostMapping("/preferences")
    public UserPreference saveUserPreference(@RequestBody UserPreference userPreference) {
        return userPreferenceService.saveUserPreference(userPreference);
    }


    @GetMapping("/preferences")
    public List<UserPreference> getAllUserPreferences() {
        return userPreferenceService.getAllUserPreferences();
    }

    @GetMapping("/hello")
    @ResponseBody
    public String helloo() {
        return "df";
    }

    @PostMapping("/generate-insight")
    public String generateInsight(@RequestBody UserPreference userPreference) {
        String insight = gptService.generateInsight(userPreference.getPreference());
        return insight;
    }


}


