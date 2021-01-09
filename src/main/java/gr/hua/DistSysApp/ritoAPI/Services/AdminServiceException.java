package gr.hua.DistSysApp.ritoAPI.Services;

public class AdminServiceException extends Exception{
    private static final long serialVersionUID = -470180207923010368L;

    public AdminServiceException() {
        super();
    }

    public AdminServiceException(final String message) {
        super(message);
    }
}
