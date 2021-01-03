package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;


@Entity
@Table(name = "requests_results")
public class RequestResults {

    @Id
    @Column(name = "request_id")
    private int request_id;

    @Column(name = "results",columnDefinition = "TEXT")
    private String results;

    @Column(name = "request_status",nullable = false,length = 16)
    private String request_status;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @MapsId
    @JoinColumn(name = "request_id",columnDefinition = "INT(10) UNSIGNED")
    private Request request;

    public RequestResults(int request_id, String results, String request_status) {
        this.request_id = request_id;
        this.results = results;
        this.request_status = request_status;
    }

    public RequestResults() {
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    @Override
    public String toString() {
        return "RequestResults{" +
                "request_id=" + request_id +
                ", results='" + results + '\'' +
                ", request_status='" + request_status + '\'' +
                '}';
    }
}
