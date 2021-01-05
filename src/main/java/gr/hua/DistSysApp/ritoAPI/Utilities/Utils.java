package gr.hua.DistSysApp.ritoAPI.Utilities;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class Utils {


    public static boolean isExistingPendingRequest(int userId , String requestType, RequestRepository requestRepository, RequestResultsRepository requestResultsRepository){

        int existentRequestId = requestRepository.findRequestIDByUseridAndRequestTypeOrdered(userId,requestType);
        RequestResults pendingRequestResult = requestResultsRepository.findRequestByRequest_id(existentRequestId);
        if (pendingRequestResult.getRequest_status().equalsIgnoreCase("Pending")){
            return true;
        }else return false;
    }
}
