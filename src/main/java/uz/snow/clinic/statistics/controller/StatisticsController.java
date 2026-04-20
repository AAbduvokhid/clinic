package uz.snow.clinic.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.snow.clinic.statistics.dto.StatisticsResponse;
import uz.snow.clinic.statistics.service.StatisticsService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    // GET /api/v1/statistics
    // Returns full statistics — today + overall + top doctors + monthly
    @GetMapping
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
        StatisticsResponse stats = statisticsService.getStatistics();
        return ResponseEntity.ok(
                ApiResponse.success("Statistics fetched successfully", stats));
    }

    // GET /api/v1/statistics/range?start=...&end=...
    // Returns statistics for a custom date range
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatisticsByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        StatisticsResponse stats =
                statisticsService.getStatisticsByDateRange(start, end);
        return ResponseEntity.ok(
                ApiResponse.success("Statistics fetched successfully", stats));
    }
}