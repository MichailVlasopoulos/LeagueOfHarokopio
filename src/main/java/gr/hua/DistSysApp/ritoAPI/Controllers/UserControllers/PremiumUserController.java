package gr.hua.DistSysApp.ritoAPI.Controllers.UserControllers;

import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Services.PremiumUserService;
import gr.hua.DistSysApp.ritoAPI.Services.PremiumUserServiceException;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PremiumUserController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @Autowired
    private PremiumUserService premiumUserService;

    @GetMapping(path="/premiumUser/showLiveMatchStats")
    public String showLiveMatchStats () throws JSONException, PremiumUserServiceException, ResourceNotFoundException {
        try {
            JSONObject responce = premiumUserService.showLiveMatchStats();
            if (responce==null) throw new ResourceNotFoundException("Data not found");
            return  responce.toString();
        }catch (PremiumUserServiceException e){
            throw new PremiumUserServiceException("Internal Server Exception while getting exception");
        }
    }

    @GetMapping(path="/premiumUser/requestTopPlayersProfiles")
    public String requestTopPlayersProfiles () throws JSONException, PremiumUserServiceException, ResourceNotFoundException { return premiumUserService.requestTopPlayersProfiles().toString(); }

    @GetMapping(path="/premiumUser/showTopPlayersProfiles")
    @ResponseBody
    public String showTopPlayersProfiles (@RequestParam int requestId) throws JSONException, PremiumUserServiceException, ResourceNotFoundException { return premiumUserService.showRequestResults(requestId).toString(); }

    @GetMapping(path="/premiumUser/requestGeneralChampionStats")
    public String requestGeneralChampionStats () throws JSONException, PremiumUserServiceException, ResourceNotFoundException { return premiumUserService.requestGeneralChampionStats().toString(); }

    @GetMapping(path="/premiumUser/showGeneralChampionStats")
    @ResponseBody
    public String showGeneralChampionStats (@RequestParam int requestId) throws JSONException, PremiumUserServiceException, ResourceNotFoundException { return premiumUserService.showRequestResults(requestId).toString(); }

    @GetMapping(path="/premiumUser/PremiumCancel")
    public String requestPremiumCancel () throws JSONException, PremiumUserServiceException, ResourceNotFoundException { return premiumUserService.requestPremiumCancel().toString();}
}
