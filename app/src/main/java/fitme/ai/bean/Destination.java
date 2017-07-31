package fitme.ai.bean;

/**
 * Created by blw on 2017/1/9.
 */

public class Destination {
    //地址field
    private String destinationFieldId;
    //地址value
    private String destinationValueId;

    public Destination(String destinationFieldId, String destinationValueId) {
        this.destinationFieldId = destinationFieldId;
        this.destinationValueId = destinationValueId;
    }

    public String getDestinationFieldId() {
        return destinationFieldId;
    }

    public String getDestinationValueId() {
        return destinationValueId;
    }

    public void setDestinationFieldId(String destinationFieldId) {
        this.destinationFieldId = destinationFieldId;
    }

    public void setDestinationValueId(String destinationValueId) {
        this.destinationValueId = destinationValueId;
    }
}
