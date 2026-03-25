package uz.snow.clinic.common.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message);
    }
    public static NotFoundException of(String entityName,Object identifier) {
        return new NotFoundException(entityName + " not found with id " + identifier);
    }
}
