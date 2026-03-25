package uz.snow.clinic.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ApiResponse<T> {
    private final boolean success;
    private final String message;

    // T instead of Object — now type-safe!
    // ApiResponse<UserResponse>, ApiResponse<List<UserResponse>>, etc.
    private final T data;

    // ✅ Static factory methods — clean way to build responses
    // Instead of: new ApiResponse(true, "Success", data)
    // Now write: ApiResponse.success("User created", data)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
