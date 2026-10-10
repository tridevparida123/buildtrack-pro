package com.novaconstruct.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_diary_entries")
public class DailyDiaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(name = "day_number")
    private Integer dayNumber = 1;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "weather_condition", length = 50)
    private String weatherCondition = "Clear";

    @Column(name = "temperature_celsius")
    private Integer temperatureCelsius = 28;

    @Column(name = "work_summary", columnDefinition = "TEXT")
    private String workSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @Column(name = "photo_1")
    private String photo1;

    @Column(name = "photo_1_caption", length = 200)
    private String photo1Caption;

    @Column(name = "photo_2")
    private String photo2;

    @Column(name = "photo_2_caption", length = 200)
    private String photo2Caption;

    @Column(name = "photo_3")
    private String photo3;

    @Column(name = "photo_3_caption", length = 200)
    private String photo3Caption;

    @Column(length = 30)
    private String status = "Approved";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public DailyDiaryEntry() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public Integer getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Integer temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }

    public String getWorkSummary() { return workSummary; }
    public void setWorkSummary(String workSummary) { this.workSummary = workSummary; }

    public User getSupervisor() { return supervisor; }
    public void setSupervisor(User supervisor) { this.supervisor = supervisor; }

    public String getPhoto1() { return photo1; }
    public void setPhoto1(String photo1) { this.photo1 = photo1; }

    public String getPhoto1Caption() { return photo1Caption; }
    public void setPhoto1Caption(String photo1Caption) { this.photo1Caption = photo1Caption; }

    public String getPhoto2() { return photo2; }
    public void setPhoto2(String photo2) { this.photo2 = photo2; }

    public String getPhoto2Caption() { return photo2Caption; }
    public void setPhoto2Caption(String photo2Caption) { this.photo2Caption = photo2Caption; }

    public String getPhoto3() { return photo3; }
    public void setPhoto3(String photo3) { this.photo3 = photo3; }

    public String getPhoto3Caption() { return photo3Caption; }
    public void setPhoto3Caption(String photo3Caption) { this.photo3Caption = photo3Caption; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
